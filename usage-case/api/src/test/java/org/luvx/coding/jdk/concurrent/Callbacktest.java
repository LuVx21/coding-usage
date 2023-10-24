package org.luvx.concurrent;

import org.junit.jupiter.api.Test;
import org.luvx.coding.jdk.concurrent.callback.callback.Callbackimpl;
import org.luvx.coding.jdk.concurrent.callback.callback.Callerimpl;

public class Callbacktest {

    @Test
    public void funtest() {

        Callerimpl caller = new Callerimpl();

        caller.call("test", new Callbackimpl());
    }
}
