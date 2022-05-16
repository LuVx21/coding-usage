package org.luvx.java;

import lombok.extern.slf4j.Slf4j;
import org.luvx.kotlin.A;

@Slf4j
public class B {

    public String method(String s) {
        return "hello " + s + " from java!";
    }

    public static void main(String[] args) {
        A a = new A();
        log.info("in java:{}" + a.method("world"));
    }
}
