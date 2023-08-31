public class DemoTest {
    public static void main(String[] args) {
        LogTraceIdGenerator logTraceIdGenerator = new RandomIdGenerator();
        System.out.println(logTraceIdGenerator.generate());
    }
}
