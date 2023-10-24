/**
 * 建造者模式
 */
public class BuilderDemo {
    public static void main(String[] args) {
        ResourcePoolConfig ceshi = new ResourcePoolConfig.Builder()
                .setName("ceshi")
                .setMinIdle(10)
                .setMaxIdle(2)
                .setMaxTotal(3)
                .build();

    }
}
