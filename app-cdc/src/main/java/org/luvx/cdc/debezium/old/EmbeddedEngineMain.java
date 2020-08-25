package org.luvx.cdc.debezium.old;

import io.debezium.embedded.EmbeddedEngine;
import io.debezium.util.Clock;
import lombok.extern.slf4j.Slf4j;
import org.luvx.cdc.debezium.utils.DebeziumRecordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Ren, Xie
 * api 废弃
 */
@Slf4j
public class EmbeddedEngineMain {
    public static void main(String[] args) {
        EmbeddedEngineMain main = new EmbeddedEngineMain();
        main.exec();
    }

    private void exec() {
        EmbeddedEngine engine = EmbeddedEngine.create()
                .using(DebeziumRecordUtils.config)
                .using(this.getClass().getClassLoader())
                .using(Clock.SYSTEM)
                .notifying(record -> System.out.println(record))
                // .notifying(new MyChangeConsumer())
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        DebeziumRecordUtils.shutdownHook(engine);
        DebeziumRecordUtils.awaitTermination(executor);
    }
}
