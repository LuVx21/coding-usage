package org.luvx.cdc.binlog;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.Event;

import java.io.IOException;

/**
 * @author: Ren, Xie
 */
public class Application {
    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient("luvx", 3306, "root", "1121");

        // EventDeserializer deserializer = new EventDeserializer();
        // deserializer.setCompatibilityMode(
        //         EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
        //         EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        // );
        // client.setEventDeserializer(deserializer);

        client.registerEventListener(
                new EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        System.out.println("---------------------------");
                        System.out.println(
                                JSON.toJSONString(event)
                        );
                    }
                });
        client.connect();
    }
}
