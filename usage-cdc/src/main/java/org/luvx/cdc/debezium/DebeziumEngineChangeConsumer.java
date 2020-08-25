package org.luvx.cdc.debezium;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: Ren, Xie
 * RecordChangeEvent<SourceRecord>
 */
@Slf4j
public class DebeziumEngineChangeConsumer implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    /**
     * {@link io.debezium.embedded.EmbeddedEngineChangeEvent}
     *
     * @param list
     * @param recordCommitter
     * @throws InterruptedException
     */
    @SneakyThrows
    @Override
    public void handleBatch(List<ChangeEvent<String, String>> list, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> recordCommitter) throws InterruptedException {
        for (ChangeEvent<String, String> event : list) {
            System.out.println("-------------");
            System.out.println("key: " + event.key());
            System.out.println("value: " + event.value());

            Class<? extends ChangeEvent> clazz = event.getClass();
            Field field = clazz.getDeclaredField("sourceRecord");
            field.setAccessible(true);
            SourceRecord o = (SourceRecord) field.get(event);
            System.out.println("sourceRecord: " + o);
        }
    }
}

