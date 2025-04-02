package org.luvx.coding.usage.zstd;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import java.io.*;

public class ZstdUtils {
    public static void compressFile(String inputFile, String outputFile) throws IOException {
        try (InputStream in = new FileInputStream(inputFile);
             OutputStream out = new ZstdOutputStream(new FileOutputStream(outputFile))) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }

    public static void decompressFile(String inputFile, String outputFile) throws IOException {
        try (InputStream in = new ZstdInputStream(new FileInputStream(inputFile));
             OutputStream out = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }
}
