package org.luvx.pattern.creational.singleton;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

@Slf4j
public class Singleton6Test {

    @Test
    public void run01() throws Exception {
        Singleton6 obj1 = Singleton6.getInstance();

        ObjectOutput oop = new ObjectOutputStream(new FileOutputStream("out.out"));
        oop.writeObject(obj1);
        oop.close();

        ObjectInput oip = new ObjectInputStream(new FileInputStream("out.out"));
        Singleton6 obj2 = (Singleton6) oip.readObject();
        oip.close();

        log.info("相同:{}", obj1 == obj2);
    }
}
