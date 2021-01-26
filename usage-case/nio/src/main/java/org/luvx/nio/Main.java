package org.luvx.nio;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Ren, Xie
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        final Path path = Paths.get("E:\\logs");
        System.out.println(path);
    }
}
