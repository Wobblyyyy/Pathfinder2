package me.wobblyyyy.pathfinder2.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Scheduler {
    private final List<Event> events = new ArrayList<>();

    public void execute() {
        List<Event> finished = new ArrayList<>();

        for (Event event : events) {
            if (event.isDone()) {
                finished.add(event);
            } else {
                event.execute();
            }
        }

        events.removeIf(finished::contains);
    }

    public void schedule(Event event) {
        this.events.add(event);
    }

    public void removeIf(Predicate<Event> shouldBeRemoved) {
        events.removeIf(shouldBeRemoved);
    }

    public void clearScheduler() {
        events.clear();
    }
}
