import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * || 运算符
 */
public class OrExpression implements Expression {
    private final List<Expression> expressions = new ArrayList<>();

    public OrExpression(String strOrExpression) {
        String[] elements = strOrExpression.trim().split("\\|\\|");
        for (String strExpr : elements) {
            expressions.add(new AndExpression(strExpr));
        }
    }

    public OrExpression(List<Expression> expressions) {
        this.expressions.addAll(expressions);
    }


    @Override
    public boolean interpret(Map<String, Long> stats) {
        for (Expression expression : expressions) {
            if (expression.interpret(stats)) {
                return true;
            }
        }
        return false;
    }
}
