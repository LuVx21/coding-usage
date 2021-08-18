package org.luvx.nio.usage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteFile {
    private static final String r = "/Users/renxie/Desktop/r.csv";
    private static final String w = "/Users/renxie/Desktop/w.csv";

    @SneakyThrows
    private static void m1() {
        String json = "";
        Files.writeString(Paths.get(w), json, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
    }

    @SneakyThrows
    private static void m2() {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("你好你好你好你好你好");
        try (FileChannel channel = new FileOutputStream(w).getChannel()) {
            log.info("limit: {}, capacity: {}", buffer.limit(), buffer.capacity());
            int length;
            while ((length = channel.write(buffer)) > 0) {
                log.info("写入长度: {}", length);
            }
        }
    }

    @SneakyThrows
    public static void m3() {
        try (FileChannel inChannel = new FileInputStream(r).getChannel();
                FileChannel outChannel = new FileOutputStream(w).getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            log.info("limit: {}, capacity: {}, position: {}", buffer.limit(), buffer.capacity(), buffer.position());
            int inLen;
            while ((inLen = inChannel.read(buffer)) != -1) {
                // 将当前位置置为limit, 然后设置当前位置为0, 也就是从0到limit这块, 都写入到管道中
                buffer.flip();
                int outLen;
                while ((outLen = outChannel.write(buffer)) != 0) {
                    log.info("读:{}, 写:{}", inLen, outLen);
                }
                // 将当前位置置为0, 然后设置limit为容量, 也就是从0到limit(容量)这块,
                // 都可以利用, 通道读取的数据存储到
                // 0到limit这块
                buffer.clear();
            }
        }
    }

    public static void main(String[] args) {
        // m1();
        // m2();
        m3();
    }
}
