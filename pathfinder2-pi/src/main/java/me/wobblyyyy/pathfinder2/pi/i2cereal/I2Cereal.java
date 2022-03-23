package me.wobblyyyy.pathfinder2.pi.i2cereal;

/**
 * {@code I2Cereal} input and output implementation that makes use of
 * {@link I2CerealIn} and {@link I2CerealOut}.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class I2Cereal {
    private final I2CerealIn input;
    private final I2CerealOut output;

    public I2Cereal(I2CerealIn input, I2CerealOut output) {
        this.input = input;
        this.output = output;
    }

    public void update() {
        input.update();
        output.update();
    }

    public I2CerealIn getInput() {
        return input;
    }

    public I2CerealOut getOutput() {
        return output;
    }
}
