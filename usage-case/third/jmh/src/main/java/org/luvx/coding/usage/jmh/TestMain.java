package org.luvx.coding.usage.jmh;

import org.luvx.coding.usage.jmh.primitive.Main;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class TestMain {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Main.class;
        Options options = new OptionsBuilder()
                .include(clazz.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
