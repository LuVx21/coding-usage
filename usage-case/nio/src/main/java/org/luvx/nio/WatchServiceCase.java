package org.luvx.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

/**
 * @author Ren, Xie
 */
@Slf4j
public class WatchServiceCase {

    static String url = "E:\\logs";

    public static void main(String[] args) throws InterruptedException, IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        final Path path = Paths.get(url);
        final WatchKey watchKey = path.register(watchService,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE
        );

        int count = 0;
        while (true) {
            final WatchKey wk = watchService.take();
            log.info("Loop count: {}", count);
            for (WatchEvent<?> event : wk.pollEvents()) {
                final Path changed = (Path) event.context();
                System.out.println(changed + ", " + event.kind());
                if (changed.endsWith("sample1.txt")) {
                    System.out.println("Sample file has changed");
                }
            }

            // reset the key
            boolean valid = wk.reset();
            if (!valid) {
                System.out.println("Key has been unregisterede");
            }
            count++;
        }
    }
}
