package org.luvx.nio.channel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

@Slf4j
public class FileCopy {

    /**
     * 分散和聚集
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        RandomAccessFile file1 = new RandomAccessFile("1.txt", "rw");
        FileChannel channel1 = file1.getChannel();
        // 分配指定大小的缓冲区,分散读取
        ByteBuffer[] array = {ByteBuffer.allocate(100), ByteBuffer.allocate(1024)};
        channel1.read(array);
        Arrays.stream(array)
                .peek(ByteBuffer::flip)
                .forEach(
                        b -> {
                            log.info("{}", new String(b.array(), 0, b.limit()));
                            log.info("=======================");
                        }
                );

        // 聚集写入
        RandomAccessFile file2 = new RandomAccessFile("2.txt", "rw");
        FileChannel channel2 = file2.getChannel();

        channel2.write(array);
    }

    /**
     * 简单易行 推荐！
     * 通道之间的数据传输(直接缓冲区)
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        FileChannel in = FileChannel.open(Paths.get("1.txt"),
                StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get("4.txt"),
                StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

        // in.transferTo(0, in.size(), out);
        out.transferFrom(in, 0, in.size());

        in.close();
        out.close();
    }

    /**
     * 使用直接缓冲区完成文件的复制(内存映射文件)
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileChannel in = FileChannel.open(Paths.get("1.txt"),
                StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get("3.txt"),
                StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

        //内存映射文件
        MappedByteBuffer from = in.map(MapMode.READ_ONLY, 0, in.size());
        MappedByteBuffer to = out.map(MapMode.READ_WRITE, 0, in.size());

        //直接对缓冲区进行数据的读写操作
        byte[] bytes = new byte[from.limit()];
        from.get(bytes);
        to.put(bytes);

        in.close();
        out.close();
    }

    /**
     * 利用通道完成文件的复制（非直接缓冲区）
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        // ①获取通道
        FileChannel in = new FileInputStream("1.txt").getChannel();
        FileChannel out = new FileOutputStream("3.txt").getChannel();

        // ②分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        // ③将通道中的数据存入缓冲区中
        while (in.read(buf) != -1) {
            // 切换读取数据的模式
            buf.flip();
            // ④将缓冲区中的数据写入通道中
            out.write(buf);
            // 清空缓冲区
            buf.clear();
        }

        in.close();
        out.close();
    }
}
