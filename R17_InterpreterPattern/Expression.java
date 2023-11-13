import java.util.Map;

/**
 * 语法规则接口
 * 模板：key1 > 100 && key2 < 1000 || key3 == 200
 * > < == 的第一个运算符一定是 key（由用户传入）
 * 用 || && 连接多个表达式
 *
 * 运用职责链模式：根据优先级先处理 || ，在其中调用 && ，再接着调用 > < ==
 */
public interface Expression {
    //根据用户输入的数据 stats，使用语法规则，判断返回true或false
    boolean interpret(Map<String, Long> stats);
}
