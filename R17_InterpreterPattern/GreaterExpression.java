import java.util.Map;

/**
 * 表示 > 运算符
 * key 为运算符前面的值(需要由用户输入)
 * value 为运算符后面的值
 * 运算 stats[key] > value 的结果
 */
public class GreaterExpression implements Expression{
    private final String key;
    private final long value;

    //直接传入key和value
    public GreaterExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    //解析字符串得到key和value
    public GreaterExpression(String strExpression) {
        String[] elements = strExpression.trim().split("\\s+");
        if (elements.length != 3 || !elements[1].trim().equals(">")) {
            throw new RuntimeException("Expression is invalid " + strExpression);
        }
        this.key = elements[0].trim();
        this.value = Long.parseLong(elements[2].trim());
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        return stats.get(key) > value;
    }
}
