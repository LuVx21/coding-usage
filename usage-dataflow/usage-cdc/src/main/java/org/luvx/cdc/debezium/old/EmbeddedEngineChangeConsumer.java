package org.luvx.cdc.debezium.old;

import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;

/**
 * @author: Ren, Xie
 */
public class EmbeddedEngineChangeConsumer implements EmbeddedEngine.ChangeConsumer {
    @Override
    public void handleBatch(List<SourceRecord> list, DebeziumEngine.RecordCommitter<SourceRecord> recordCommitter) throws InterruptedException {
    }
}
