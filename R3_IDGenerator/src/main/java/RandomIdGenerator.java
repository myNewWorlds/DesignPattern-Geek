import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class RandomIdGenerator implements LogTraceIdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RandomIdGenerator.class);

    @Override
    public String generate() {
        String substrOfHostName = getLastfieldOfHostName();
        long currentTimeMillis = System.currentTimeMillis();
        String radomString = generateRadomAlphameric(8);
        return String.format("%s-%d-%s", substrOfHostName, currentTimeMillis, radomString);
    }

    private String generateRadomAlphameric(int length) {
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
            String[] tokens = hostName.split("\\.");
            substrOfHostName = tokens[tokens.length - 1];
            return substrOfHostName;
        } catch (UnknownHostException e) {
            logger.warn("Failed to get the host name.", e);
        }
        return null;
    }
}