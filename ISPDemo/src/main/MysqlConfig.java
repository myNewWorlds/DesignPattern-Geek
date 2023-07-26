package src.main;

import src.main.inter.Viewer;

public class MysqlConfig implements Viewer {
    private final ConfigSource configSource;
    private String address;
    private String timeout;

    public MysqlConfig(ConfigSource configSource) {
        this.configSource = configSource;
    }

    public String getAddress() {
        return address;
    }

    public String getTimeout() {
        return timeout;
    }

    public void update() {
        System.out.println("MysqlConfig 更新");
        this.address = configSource.getAddress();
        this.timeout = configSource.getTimeout();
    }

    @Override
    public String outputInPlainText() {
        return "MysqlConfig 配置信息" + address + timeout;
    }
}
