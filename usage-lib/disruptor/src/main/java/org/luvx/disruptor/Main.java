package org.luvx.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    static final int bufferSize = 16;

    public static void main(String[] args) throws Exception {
        // 创建disruptor，采用单生产者模式
        EventFactory<Element> factory = Element::new;
        ThreadFactory threadFactory = r -> new Thread(r, "simpleThread");
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);
        // 设置EventHandler
        EventHandler<Element> handler = (element, sequence, endOfBatch) -> log.info("Element: {}", element.getValue());
        disruptor.handleEventsWith(handler);
        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; true; i++) {
            long sequence = ringBuffer.next();
            try {
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该位置元素的值
                event.setValue(i);
            } finally {
                ringBuffer.publish(sequence);
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    @Getter
    @Setter
    private static class Element {
        private int value;
    }
}
