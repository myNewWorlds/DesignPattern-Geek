package src.main;

public class ISPDemo {
    static ConfigSource configSource = new ConfigSource();
    public static final RedisConfig redisConfig = new RedisConfig(configSource);
    public static final KafkaConfig kafkaConfig = new KafkaConfig(configSource);
    public static final MysqlConfig mysqlConfig = new MysqlConfig(configSource);

    public static void main(String[] args) {
        ScheduledUpdater redisUpdater = new ScheduledUpdater(3, 3, redisConfig);
        redisUpdater.run();
        ScheduledUpdater kafkaUpdater = new ScheduledUpdater(5, 5, kafkaConfig);
        kafkaUpdater.run();

        SimpleHttpServer simpleHttpServer = new SimpleHttpServer();
        simpleHttpServer.addViewers("/config", kafkaConfig);
        simpleHttpServer.addViewers("/config", mysqlConfig);
        simpleHttpServer.run();
    }
}
