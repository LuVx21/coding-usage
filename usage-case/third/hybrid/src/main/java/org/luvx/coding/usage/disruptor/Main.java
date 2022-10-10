package org.luvx.coding.usage.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    // 必须是2的N次方
    static final int bufferSize = 1 << 4;

    public static void m1() throws Exception {
        Disruptor<QueueEvent> disruptor = new Disruptor<>(QueueEvent::new,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                // r -> new Thread(r, "simpleThread"),
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        disruptor.handleEventsWith((element, sequence, endOfBatch) ->
                log.info("Element: {}", element.getData())
        );
        disruptor.start();

        RingBuffer<QueueEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);

        for (int i = 0; true; i++) {
            // 1
            // int finalI = i;
            // ringBuffer.publishEvent((event, sequence, buffer) ->
            //         event.setValue(finalI)
            // );

            // 2
            // long sequence = ringBuffer.next();
            // try {
            //     ringBuffer.get(sequence).setValue(i);
            // } finally {
            //     ringBuffer.publish(sequence);
            // }

            // 3
            bb.putLong(0, i);
            ringBuffer.publishEvent((event, sequence, buffer) ->
                            event.setData(buffer.getLong(0)),
                    bb
            );
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Getter
    @Setter
    @ToString
    private static class QueueEvent {
        private long data;
    }

    public static void main(String[] args) throws Exception {
        m1();
    }
}
