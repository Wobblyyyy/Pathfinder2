package me.wobblyyyy.pathfinder2.pi.pins;

/**
 * Simulated implementation of {@link DigitalIn}, used for testing.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class SimulatedIn implements DigitalIn {
    private boolean state;

    public SimulatedIn() {
        this(false);
    }

    public SimulatedIn(boolean state) {
        this.state = state;
    }

    @Override
    public boolean get() {
        return state;
    }

    public SimulatedIn set(boolean state) {
        this.state = state;

        return this;
    }
}
