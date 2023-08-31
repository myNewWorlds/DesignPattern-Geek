import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class RandomIdGenerator implements LogTraceIdGenerator {
    //不读取数据 | 不参与业务逻辑的执行 | 不影响代码逻辑的正确性
    //不需要进行单元测试，也不必进行依赖注入
    private static final Logger logger = LoggerFactory.getLogger(RandomIdGenerator.class);

    @Override
    public String generate() {
        String substrOfHostName = getLastfieldOfHostName();
        long currentTimeMillis = System.currentTimeMillis();
        String radomString = generateRandomAlphameric(8);
        return String.format("%s-%d-%s", substrOfHostName, currentTimeMillis, radomString);
    }

    @VisibleForTesting
    protected String generateRandomAlphameric(int length) {
        char[] randomChars = new char[length];
        int count = 0;
        //用解释性变量替代魔数
        int maxAscii = 'z';
        Random random = new Random();
        while (count < length) {
            int radomAscii = random.nextInt(maxAscii);
            //语义更加明确
            boolean isDigit = radomAscii >= '0' && radomAscii <= '9';
            boolean isUppercase = radomAscii >= 'A' && radomAscii <= 'Z';
            boolean isLowercase = radomAscii >= 'a' && radomAscii <= 'z';
            if (isDigit || isUppercase || isLowercase) {
                randomChars[count] = (char) radomAscii;
                count++;
            }
        }
        return new String(randomChars);
    }

    private String getLastfieldOfHostName() {
        String substrOfHostName;
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            substrOfHostName = getLastSubstrSplittedByDot(hostName);
            return substrOfHostName;
        } catch (UnknownHostException e) {
            logger.warn("Failed to get the host name.", e);
        }
        return null;
    }

    /**
     * 将getLastfieldOfHostName的主要代码抽取出来后
     * 以下的方法不依赖本地的主机名，可单独进行单元测试
     */
    @VisibleForTesting
    protected String getLastSubstrSplittedByDot(String hostName) {
        String[] tokens = hostName.split("\\.");
        return tokens[tokens.length - 1];
    }
}