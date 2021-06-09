package me.wobblyyyy.pathfinder2.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class ConditionSet implements BooleanSupplier {
    private final List<BooleanSupplier> conditions;

    public ConditionSet() {
        this(new ArrayList<>());
    }

    public ConditionSet(List<BooleanSupplier> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean getAsBoolean() {
        boolean isTrue = true;

        for (BooleanSupplier condition : conditions) {
            if (!condition.getAsBoolean()) isTrue = false;
        }

        return isTrue;
    }

    public boolean isTrue() {
        return getAsBoolean();
    }

    public boolean isFalse() {
        return !getAsBoolean();
    }

    public void require(BooleanSupplier condition) {
        this.conditions.add(condition);
    }

    public void unrequire(BooleanSupplier condition) {
        this.conditions.remove(condition);
    }
}
