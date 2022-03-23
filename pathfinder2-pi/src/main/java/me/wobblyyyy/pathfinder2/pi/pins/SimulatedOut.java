package me.wobblyyyy.pathfinder2.pi.pins;

/**
 * Simulated implementation of {@link DigitalOut}, used for testing.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class SimulatedOut implements DigitalOut {
    private boolean state;

    public SimulatedOut() {
        this(false);
    }

    public SimulatedOut(boolean state) {
        this.state = state;
    }

    public boolean get() {
        return state;
    }

    @Override
    public void set(boolean state) {
        this.state = state;
    }
}
