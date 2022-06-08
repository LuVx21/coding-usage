package org.luvx.concurrent.entity;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Ren, Xie
 */
@Slf4j
public class Task {
    @SneakyThrows
    public static String execute(int second) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < second; i++) {
            TimeUnit.SECONDS.sleep(1);
            log.info("耗时操作: {}/{}", i + 1, second);
        }
        long exec = System.currentTimeMillis() - start;
        // log.info("总耗时:{}ms", exec);
        return "耗时:" + exec + "ms";
    }
}
