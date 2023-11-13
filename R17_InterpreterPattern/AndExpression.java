import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * && 运算
 */
public class AndExpression implements Expression {
    private final List<Expression> expressions = new ArrayList<>();

    public AndExpression(String strExpression) {
        String[] elements = strExpression.trim().split("&&");
        //按照&&，将整个语法规则分割成单个表达式
        for (String strExpr : elements) {
            if (strExpr.contains(">")) {
                expressions.add(new GreaterExpression(strExpr));
            } else if (strExpr.contains("<")) {
                expressions.add(new LessExpression(strExpr));
            } else if (strExpr.contains("==")) {
                expressions.add(new EqualExpression(strExpr));
            } else {
                throw new RuntimeException("Expression is invalid:" + strExpression);
            }
        }
    }

    public AndExpression(List<Expression> expressions) {
        this.expressions.addAll(expressions);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        for (Expression expression : expressions) {
            if (!expression.interpret(stats)) {
                return false;
            }
        }
        return true;
    }
}
