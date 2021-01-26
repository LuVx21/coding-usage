package org.luvx.pattern.structural.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Aspect {

    /**
     * 打印前操作
     */
    public static void printBefore() {
        log.info("打印前操作...");
    }

    /**
     * 打印后操作
     */
    public static void printAfter() {
        log.info("打印后操作...");
    }
}
