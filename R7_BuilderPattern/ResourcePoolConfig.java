public class ResourcePoolConfig {
    private String name;
    private int maxTotal;
    private int maxIdel;
    private int minIdel;

    private ResourcePoolConfig(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdel = builder.maxIdle;
        this.minIdel = builder.minIdle;
    }

    //我们将Builder类设计成了ResourcePoolConfig的内部类。
    // 我们也可以将Builder类设计成独立的非内部类ResourcePoolConfigBuilder。
    public static class Builder {
        private static final int DEFAULT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;
        private String name;
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;

        //bulid()处理参数之间的依赖关系
        public ResourcePoolConfig build() {
            // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
            if (name == null || name.length() < 1) {
                throw new RuntimeException("");
            }
            if (maxIdle > maxTotal) {
                throw new RuntimeException("");
            }
            if (minIdle > maxIdle) {
                throw new RuntimeException("");
            }
            return new ResourcePoolConfig(this);
        }

        //每个set处理每个元素的单独逻辑
        public Builder setName(String name) {
            if (name == null) {
                throw new RuntimeException();
            }
            this.name = name;
            return this;
        }

        public Builder setMaxTotal(int maxTotal) {
            if (maxTotal < 0) {
                throw new RuntimeException();
            }
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder setMaxIdle(int maxIdle) {
            if (maxIdle < 0) {
                throw new RuntimeException();
            }
            this.maxIdle = maxIdle;
            return this;
        }

        public Builder setMinIdle(int minIdle) {
            if (minIdle < 0) {
                throw new RuntimeException();
            }
            this.minIdle = minIdle;
            return this;
        }
    }

}
