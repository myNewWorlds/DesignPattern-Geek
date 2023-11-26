package com.ljt.ratelimiter.rule.source;

import com.ljt.ratelimiter.rule.parser.JsonRuleConfigParser;
import com.ljt.ratelimiter.rule.parser.RuleConfigParser;
import com.ljt.ratelimiter.rule.parser.YamlRuleConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件解析配置规则
 * 根据文件类型，调用不同的解析器，解析配置规则。
 */
public class FileRuleConfigSource implements RuleConfigSource {

    private static final Logger log = LoggerFactory.getLogger(FileRuleConfigSource.class);

    //配置文件名
    public static final String API_LIMIT_CONFIG_NAME = "ratelimiter-rule";
    public static final String JSON_EXTENSION = "json";
    public static final String YML_EXTENSION = "yml";
    public static final String YAML_EXTENSION = "yaml";

    private static final String[] SUPPORT_EXTENSIONS =
            new String[]{JSON_EXTENSION, YML_EXTENSION, YAML_EXTENSION};
    private static final Map<String, RuleConfigParser> PARSER_MAP = new HashMap<>();
    static {
        PARSER_MAP.put(JSON_EXTENSION, new JsonRuleConfigParser());
        PARSER_MAP.put(YAML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(YML_EXTENSION, new YamlRuleConfigParser());
    }
    @Override
    public UniformRuleConfigMapping load() {
        for (String extension : SUPPORT_EXTENSIONS) {
            try (InputStream in = this.getClass().getResourceAsStream("/" + getFileNameByExt(extension))) {
                RuleConfigParser parser = PARSER_MAP.get(extension);
                return parser.parse(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String getFileNameByExt(String extension) {
        return API_LIMIT_CONFIG_NAME + "." + extension;
    }
}
