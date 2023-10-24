package org.luvx.nio.channel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Main {

    /**
     * 字符集
     * Charset.availableCharsets()
     *
     * @throws IOException
     */
    @Test
    public void test6() throws IOException {
        Charset cs1 = Charset.forName("GBK");
        CharBuffer cBuf1 = CharBuffer.allocate(2048);
        cBuf1.put("字符集字符！").flip();

        ByteBuffer bBuf = cs1.newEncoder().encode(cBuf1);

        log.info(cs1.newDecoder().decode(bBuf).toString());
        log.info("------------------------------------------------------");

        Charset cs2 = StandardCharsets.UTF_8;
        bBuf.flip();
        CharBuffer cBuf3 = cs2.decode(bBuf);
        log.info(cBuf3.toString());
    }

}
