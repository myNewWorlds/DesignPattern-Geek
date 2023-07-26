package src.main;

import src.main.inter.Updater;
import src.main.inter.Viewer;

public class KafkaConfig implements Updater, Viewer {
    private final ConfigSource configSource;
    private String address;
    private String timeout;

    public KafkaConfig(ConfigSource configSource) {
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
        System.out.println("KafkaConfig 更新-----------" + Thread.currentThread().getId());
        this.address = configSource.getAddress();
        this.timeout = configSource.getTimeout();
    }

    @Override
    public String outputInPlainText() {
        return "KafkaConfig 配置信息" + address + timeout;
    }
}
