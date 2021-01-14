package org.luvx.sqlparser.utils;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFileUtils {
    public static String readFile(String path) {
        String sql = null;
        try (InputStream in = getClassLoader(ReadFileUtils.class).getResourceAsStream(path)) {
            InputStreamReader isr = new InputStreamReader(in);
            sql = CharStreams.toString(isr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sql;
    }

    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = cls.getClassLoader();
        }
        return cl;
    }
}
