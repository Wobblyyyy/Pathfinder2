package me.wobblyyyy.pathfinder2.wpilib;

import edu.wpi.first.math.controller.PIDController;
import me.wobblyyyy.pathfinder2.control.AbstractController;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A PID controller that utilizes the {@link PIDController} class from wpilib.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class WPIPIDController extends AbstractController {
    private final PIDController controller;

    /**
     * Create a new {@code WPIPIDController} by using an existing instance of
     * {@link PIDController}.
     *
     * @param controller the controller to use.
     */
    public WPIPIDController(PIDController controller) {
        this.controller = controller;
    }

    /**
     * Create a new {@code WPIPIDController} by also creating a new
     * {@link PIDController} with the provided coefficients.
     *
     * @param p the proportional coefficient.
     * @param i the integral coefficient.
     * @param d the derivative coefficient.
     * @see PIDController#PIDController(double, double, double)
     */
    public WPIPIDController(double p, double i, double d) {
        this(new PIDController(p, i, d));
    }

    /**
     * Create a new {@code WPIPIDController} by also creating a new
     * {@link PIDController} with the provided coefficients.
     *
     * @param p the proportional coefficient.
     * @param i the integral coefficient.
     * @param d the derivative coefficient.
     * @param f the feedforward coefficient.
     * @see PIDController#PIDController(double, double, double, double)
     */
    public WPIPIDController(double p, double i, double d, double f) {
        this(new PIDController(p, i, d, f));
    }

    @Override
    public double calculate(double value) {
        return controller.calculate(value, getTarget());
    }

    @Override
    public void reset() {
        controller.reset();
    }

    @Override
    public String toString() {
        return StringUtils.format(
            "(p: %s, i: %s, d: %s)",
            controller.getP(),
            controller.getI(),
            controller.getD()
        );
    }
}
