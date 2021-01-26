package org.luvx.pattern.behavioral.command;

import org.junit.Test;

public class Client {
    @Test
    public void test1() {
        Soldier soldier = new Soldier();
        Command command = new RocketCommand(soldier);
        Captain captain = new Captain(command);
        captain.invoke();
    }
}