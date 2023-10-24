package org.luvx.coding.jdk.io;

import java.io.*;

public class Demo1 {

    // catch
    static String firstLineOfFile(String path, String defaultVal) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }

    static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[1024];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }

    public static void main(String[] args) throws IOException {
        String src = "/Users/renxie/OneDrive/Code/coding-usage/usage-case/api/src/main/java/org/luvx/1.txt";
        String dst = "/Users/renxie/OneDrive/Code/coding-usage/usage-case/api/src/main/java/org/luvx/2.txt";
        copy(src, dst);
    }
}
