package me.wobblyyyy.pathfinder2.scheduling;

import java.util.function.Supplier;

public class Event {
    private final Supplier<Boolean> canExecute;
    private final Supplier<Boolean> isDone;
    private final Runnable action;

    public Event(Supplier<Boolean> canExecute,
                 Supplier<Boolean> isDone,
                 Runnable action) {
        this.canExecute = canExecute;
        this.isDone = isDone;
        this.action = action;
    }

    public boolean canExecute() {
        return canExecute.get();
    }

    public boolean isDone() {
        return isDone.get();
    }

    public void execute() {
        if (canExecute()) action.run();
    }
}
