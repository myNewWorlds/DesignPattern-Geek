package src.main;

import src.main.inter.Updater;

public class RedisConfig implements Updater {
    private final ConfigSource configSource;
    private String address;
    private String timeout;

    public RedisConfig(ConfigSource configSource) {
        this.configSource = configSource;
    }

    public String getAddress() {
        return address;
    }

    public String getTimeout() {
        return timeout;
    }

    @Override
    public void update() {
        System.out.println("RedisConfig 更新----------" + Thread.currentThread().getId());
        this.address = configSource.getAddress();
        this.timeout = configSource.getTimeout();
    }
}
