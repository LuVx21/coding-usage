package org.luvx.coding.usage.zstd;

import com.github.luben.zstd.Zstd;
import org.luvx.coding.common.more.MorePrints;

import java.nio.charset.StandardCharsets;

public class ZstdCase {

    public static void main(String[] args) {
        String originalData = "Zstd 是一种高性能的压缩算法，由 Facebook 开发，具有高压缩比和快速压缩/解压缩速度的特点.";
        originalData = originalData + originalData + originalData;

        byte[] input = originalData.getBytes(StandardCharsets.UTF_8);
        byte[] compressed = Zstd.compress(input, 10);
        MorePrints.println("原始大小: " + input.length, "压缩大小: " + compressed.length, "压缩比:", (double) compressed.length / (double) input.length);

        byte[] decompressed = Zstd.decompress(compressed, input.length);
        String decompressedData = new String(decompressed, StandardCharsets.UTF_8);
        System.out.println("数据是否相同: " + originalData.equals(decompressedData));
    }
}
