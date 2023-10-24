package org.luvx.concurrent.entity;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Ren, Xie
 */
@Slf4j
public class Task {
    @SneakyThrows
    public static String execute(int second) {
        String name = Thread.currentThread().getName();
        if (StringUtils.isEmpty(name)) {
            name = "未知" + RandomStringUtils.randomNumeric(4);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < second; i++) {
            TimeUnit.SECONDS.sleep(1);
            log.info("线程({})执行耗时操作: {}/{}", name, i + 1, second);
        }
        long exec = System.currentTimeMillis() - start;
        // log.info("总耗时:{}ms", exec);
        return "线程(" + name + ")耗时:" + exec + "ms";
    }
}
