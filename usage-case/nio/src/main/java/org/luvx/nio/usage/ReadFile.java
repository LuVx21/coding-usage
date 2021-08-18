package org.luvx.nio.usage;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.base.Stopwatch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadFile {
    private static final String path = "/Users/renxie/Desktop/r.csv";

    @SneakyThrows
    private static void m1() {
        Files.list(Paths.get(""))
                .forEachOrdered(System.out::println)
        ;
    }

    @SneakyThrows
    private static void m2() {
        // List<String> lines = Files.readAllLines(Paths.get(""), StandardCharsets.UTF_8);
        Files.lines(Paths.get(""), StandardCharsets.UTF_8)
                .forEachOrdered(System.out::println);
    }

    @SneakyThrows
    private static void m3() {
        try (FileChannel channel = new FileInputStream(path).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            log.info("limit: {}, capacity: {}, position: {}", buffer.limit(), buffer.capacity(), buffer.position());
            for (int len = channel.read(buffer); len != -1; len = channel.read(buffer)) {
                log.info("limit: {}, capacity: {}, position: {}", buffer.limit(), buffer.capacity(), buffer.position());
                buffer.clear();
                log.info("-limit: {}, capacity: {}, position: {}", buffer.limit(), buffer.capacity(),
                        buffer.position());
                byte[] bytes = buffer.array();
                String str = new String(bytes, 0, len);
                System.out.println(str);
            }
        }
    }

    public static void main(String[] args) {
        Stopwatch watch = Stopwatch.createStarted();
        // m1();
        // m2();
        m3();
        System.out.println(watch.stop());
    }
}
