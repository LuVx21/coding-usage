package org.luvx.nio.buffer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

@Slf4j
public class Main {

    @Test
    public void test1() {
        String str = "abcde";
        //1. 分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        log.info("allocate: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        //2. 利用 put() 存入数据到缓冲区中
        buf.put(str.getBytes());
        log.info("put: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        //3. 切换读取数据模式
        buf.flip();
        log.info("flip: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        //4. 利用 get() 读取缓冲区中的数据
        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        log.info(new String(dst, 0, dst.length));
        log.info("get: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        //5. rewind() : 可重复读
        buf.rewind();
        log.info("rewind: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        //6. clear() : 清空缓冲区. 但是缓冲区中的数据依然存在, 但是处于“被遗忘”状态
        buf.clear();
        log.info("clear: {}-{}-{}", buf.position(), buf.limit(), buf.capacity());

        log.info("{}", (char) buf.get());
    }

    @Test
    public void test2() {
        String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();
        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        log.info(new String(dst, 0, 2));
        log.info("{}", buf.position());

        //mark() : 标记
        buf.mark();

        buf.get(dst, 2, 2);
        log.info(new String(dst, 2, 2));
        log.info("{}", buf.position());

        //reset() : 恢复到 mark 的位置
        buf.reset();
        log.info("{}", buf.position());

        //判断缓冲区中是否还有剩余数据
        if (buf.hasRemaining()) {
            //获取缓冲区中可以操作的数量
            log.info("{}", buf.remaining());
        }
    }

    @Test
    public void test3() {
        //分配直接缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        System.out.println(buf.isDirect());
    }
}
