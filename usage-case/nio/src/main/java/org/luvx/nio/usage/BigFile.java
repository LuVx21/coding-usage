package org.luvx.nio.usage;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.google.common.base.Stopwatch;

import lombok.SneakyThrows;

/**
 * 非常大的文件读取方式
 *
 * @author Ren, Xie
 */
public class BigFile {
    private static final String path = "/Users/renxie/Desktop/r.csv";

    /**
     * 理论上比m1稍快
     */
    @SneakyThrows
    private static void m0() {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)))) {
            byte[] data = new byte[1024];
            for (int len = in.read(data); len > 0; len = in.read(data)) {
                // System.out.println(Arrays.toString(data));
            }
        }
    }

    @SneakyThrows
    private static void m1() {
        // 最快的方式
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            for (String line = reader.readLine(); isNotEmpty(line); line = reader.readLine()) {
                // System.out.println(line);
            }
        }
        // 比上面稍慢
        // Files.lines(Paths.get(path), StandardCharsets.UTF_8).forEachOrdered(System.out::println);
    }

    /**
     * 比上面的慢挺多
     */
    @SneakyThrows
    private static void m2() {
        try (RandomAccessFile br = new RandomAccessFile(path, "r")) {
            for (String line = br.readLine(); isNotEmpty(line); line = br.readLine()) {
                System.out.println(line);
            }
        }
    }

    @SneakyThrows
    private static void m3() {
        try (LineIterator it = FileUtils.lineIterator(FileUtils.getFile(path), "UTF-8")) {
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
            }
        }
    }

    public static void main(String[] args) {
        Stopwatch watch = Stopwatch.createStarted();
        // m0();
        // m1();
        // m2();
        m3();
        System.out.println(watch.stop());
    }
}
