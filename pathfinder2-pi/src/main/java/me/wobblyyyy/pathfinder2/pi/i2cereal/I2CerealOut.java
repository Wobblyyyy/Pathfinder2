package me.wobblyyyy.pathfinder2.pi.i2cereal;

import java.util.ArrayDeque;
import java.util.Queue;
import me.wobblyyyy.pathfinder2.pi.Block;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalIn;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalOut;

/**
 * {@code I2Cereal} protocol output implementation.
 *
 * <p>
 * In this case, the Pi is the sender and the Arduino is the reciever.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class I2CerealOut {
    private boolean previousInput = false;
    private boolean previousOutput = false;

    private Queue<Block> blocks = new ArrayDeque<>(50);

    private DigitalIn readerToSender;
    private DigitalOut senderToReader;

    private DigitalOut outA;
    private DigitalOut outB;
    private DigitalOut outC;
    private DigitalOut outD;

    public I2CerealOut(
        DigitalIn readerToSender,
        DigitalOut senderToReader,
        DigitalOut outA,
        DigitalOut outB,
        DigitalOut outC,
        DigitalOut outD
    ) {
        this.readerToSender = readerToSender;
        this.senderToReader = senderToReader;
        this.outA = outA;
        this.outB = outB;
        this.outC = outC;
        this.outD = outD;
    }

    public void update() {
        boolean input = readerToSender.get();

        if (input == previousInput) {
            return;
        }

        boolean output = !previousOutput;

        Block block = blocks.remove();
        block.write(outA, outB, outC, outD);
        senderToReader.set(output);

        previousInput = input;
        previousOutput = output;
    }

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
