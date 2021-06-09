package me.wobblyyyy.pathfinder2.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Dependent implements BooleanSupplier {
    private final ConditionSet conditionSet;

    public Dependent(List<Event> predecessors) {
        List<BooleanSupplier> suppliers = new ArrayList<>();

        for (Event event : predecessors) {
            suppliers.add(event::isDone);
        }

        this.conditionSet = new ConditionSet(suppliers);
    }

    public boolean canExecute() {
        return conditionSet.getAsBoolean();
    }

    @Override
    public boolean getAsBoolean() {
        return canExecute();
    }
}
