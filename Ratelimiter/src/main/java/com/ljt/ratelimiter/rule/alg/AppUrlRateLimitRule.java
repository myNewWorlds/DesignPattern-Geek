package com.ljt.ratelimiter.rule.alg;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.utils.UrlUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 使用Trie树存储 一个App的{@link ApiLimit}，保证线程安全
 * Trie tree to store {@link ApiLimit} for one app. This class is thread-safe.
 */
public class AppUrlRateLimitRule {
    private static final Logger log = LoggerFactory.getLogger(AppUrlRateLimitRule.class);

    private final Node root;

    public AppUrlRateLimitRule() {
        root = new Node("/");
    }

    public void addLimitInfo(ApiLimit apiLimit) throws InvalidUrlException {
        String urlPath = apiLimit.getApi();
        if (!urlPath.startsWith("/")) {
            throw new InvalidUrlException("the api is invalid:" + urlPath);
        }

        if (urlPath.equals("/")) {
            root.setApiLimit(apiLimit);
            return;
        }

        List<String> pathDirs = UrlUtils.tokenizeUrlPath(urlPath);
        if (pathDirs.isEmpty()) {
            log.warn("url [{}] is parsed to empty pathDirs", urlPath);
            return;
        }

        Node p = root;
        for (String pathDir : pathDirs) {
            ConcurrentHashMap<String, Node> children = p.getEdges();

            boolean isPattern = false;
            String pathDirPattern = pathDir;
            if (isUrlTemplateVarible(pathDir)) {
                pathDirPattern = getPathDirPattern(pathDir);
                isPattern = true;
            }

            Node newNode = new Node(pathDirPattern, isPattern);
            Node existNode = children.putIfAbsent(pathDirPattern, newNode);
            if (existNode == null) {
                p = newNode; //更新成功
            } else {
                p = existNode; //没有更新，返回已经存在的节点
            }
        }
        p.setApiLimit(apiLimit);
        log.info("url [{}] set limit info {}", urlPath, apiLimit);
    }

    /**
     * 根据url路径，得到最接近的匹配规则
     * /v1/m1/user 没有匹配规则 /v1/m1 存在匹配规则 rule1，则rule1作为结果返回
     */
    public ApiLimit getLimitInfo(String urlPath) throws InvalidUrlException {
        if (StringUtils.isBlank(urlPath)) {
            return null;
        }

        if (urlPath.equals("/")) {
            return root.getApiLimit();
        }

        List<String> pathDirs = UrlUtils.tokenizeUrlPath(urlPath);
        if (pathDirs.isEmpty()) {
            log.warn("url [{}] is parsed to empty pathDirs", urlPath);
            return null;
        }

        Node p = root;
        ApiLimit currentLimit = null;
        if (p.getApiLimit() != null) {
            currentLimit = p.getApiLimit();
        }
        for (String pathDir : pathDirs) {
            ConcurrentHashMap<String, Node> children = p.getEdges();
            Node matchNode = children.get(pathDir);
            if (matchNode == null) {
                //判断正则匹配
                for (Map.Entry<String, Node> entry : children.entrySet()) {
                    Node n = entry.getValue();
                    //entry.getKey() == n.getPathDir
                    if (n.isPattern() && Pattern.matches(n.getPathDir(), pathDir)) {
                        matchNode = n;
                    }

                }

            }

            if (matchNode != null) {
                p = matchNode;
                if (p.getApiLimit() != null) {
                    currentLimit = p.getApiLimit();
                }
            } else {
                break;
            }
        }

        log.info("url [{}] get limit info: {}", urlPath, currentLimit);
        return currentLimit;
    }

    /**
     * 判断urlPath路径是否为URL模板变量
     */
    private boolean isUrlTemplateVarible(String pathDir) {
        return pathDir.startsWith("{") && pathDir.endsWith("}");
    }

    /**
     * 得到URL路径的正则匹配规则
     * {username:(^[a-zA-Z]*$)} 的规则为 (^[a-zA-Z]*$)
     * {walletId} 其中不包含冒号，则默认为匹配ID
     */
    private String getPathDirPattern(String pathDir) {
        StringBuilder patternBuilder = new StringBuilder();
        int colonIdx = pathDir.indexOf(':');
        if (colonIdx == -1) {
            patternBuilder.append("(^[0-9]*$)"); //默认匹配ID
        } else {
            String pattern = pathDir.substring(colonIdx + 1, pathDir.length() - 1);
            patternBuilder.append('(');
            patternBuilder.append(pattern);
            patternBuilder.append(')');
        }
        return patternBuilder.toString();
    }

    /**
     * Trie树的节点
     */
    @Getter
    public static final class Node {
        @Setter
        private String pathDir;  //当前节点匹配的urlPath
        @Setter
        private boolean isPattern; //当前节点是否为正则规则匹配节点
        private final ConcurrentHashMap<String, Node> edges = new ConcurrentHashMap<>();
        @Setter
        private ApiLimit apiLimit; //从根节点到当前路径的url对应的apiLimit

        public Node() {
        }

        public Node(String pathDir) {
            this.pathDir = pathDir;
        }

        public Node(String pathDir, boolean isPattern) {
            this.pathDir = pathDir;
            this.isPattern = isPattern;
        }
    }
}
