package org.luvx.pattern.behavioral.command;

public class RocketCommand implements Command {

    private Soldier soldier;

    public RocketCommand(Soldier soldier) {
        this.soldier = soldier;
    }

    @Override
    public void execute() {
        soldier.fire();
    }
}
