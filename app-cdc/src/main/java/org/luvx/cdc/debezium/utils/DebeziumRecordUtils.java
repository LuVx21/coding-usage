package org.luvx.cdc.debezium.utils;

import io.debezium.config.Configuration;
import io.debezium.engine.DebeziumEngine;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: Ren, Xie
 */
@Slf4j
public class DebeziumRecordUtils {
    public static Configuration config = Configuration.from(config());

    public Properties config1() {
        Properties propConfig = new Properties();
        try (InputStream propsInputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            propConfig.load(propsInputStream);
        } catch (IOException e) {
            log.error("Couldn't load properties", e);
        }
        return propConfig;
    }

    public static Properties config() {
        Properties props = new Properties();
        props.setProperty("name", "debezium-mysql-connector");
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "offset.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("database.hostname", "luvx");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "1121");
        props.setProperty("database.server.id", "1");
        props.setProperty("database.server.name", "luvx");
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "dbhistory.dat");
        return props;
    }

    public static void shutdownHook(DebeziumEngine engine) {
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(() -> {
                            log.info("Requesting embedded engine to shut down");
                            try {
                                engine.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                );
    }

    public static void awaitTermination(ExecutorService executor) {
        try {
            while (!executor.awaitTermination(5L, TimeUnit.SECONDS)) {
                log.info("Waiting another 5 seconds for the embedded engine to shut down");
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }
}
