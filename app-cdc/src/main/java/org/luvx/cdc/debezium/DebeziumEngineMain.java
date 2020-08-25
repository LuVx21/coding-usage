package org.luvx.cdc.debezium;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.luvx.cdc.debezium.utils.DebeziumRecordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DebeziumEngineMain {
    public static void main(String[] args) {
        DebeziumEngineMain main = new DebeziumEngineMain();
        main.exec();
    }

    private void exec() {
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(DebeziumRecordUtils.config())
                // .notifying(record -> {
                //     System.out.println("------------------");
                //     System.out.println(record);
                // })
                .notifying(new DebeziumEngineChangeConsumer())
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        DebeziumRecordUtils.shutdownHook(engine);
        DebeziumRecordUtils.awaitTermination(executor);
    }
}
