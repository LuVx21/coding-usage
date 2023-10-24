package org.luvx.coding.jdk.concurrent.notify.piped;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Pipe {
    private PipedWriter writer = new PipedWriter();
    private PipedReader reader = new PipedReader();

    {
        try {
            writer.connect(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class WriterThread implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println("this is writer");
            while (true) {
                String s = LocalDateTime.now().toString();
                log.info("写:{}", s);
                writer.write(s);
                writer.flush();
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    class ReaderThread implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println("this is reader");
            int receive = 0;
            while (true) {
                char[] a = new char[1024];
                reader.read(a);
                log.info("读:{}", String.valueOf(a));
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Pipe p = new Pipe();
        new Thread(p.new ReaderThread()).start();
        Thread.sleep(1000);
        new Thread(p.new WriterThread()).start();
    }
}