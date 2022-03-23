package me.wobblyyyy.pathfinder2.pi.i2cereal;

import java.util.ArrayDeque;
import java.util.Queue;
import me.wobblyyyy.pathfinder2.pi.Block;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalIn;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalOut;

/**
 * {@code I2Cereal} protocol input implementation.
 *
 * <p>
 * In this case, the Arduino is the sender and the Pi is the reciever.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class I2CerealIn {
    private boolean previousInput = false;
    private boolean previousOutput = false;

    private final Queue<Block> blocks = new ArrayDeque<>(50);

    private final DigitalIn senderToReader;
    private final DigitalOut readerToSender;

    private final DigitalIn inA;
    private final DigitalIn inB;
    private final DigitalIn inC;
    private final DigitalIn inD;

    public I2CerealIn(
        DigitalIn senderToReader,
        DigitalOut readerToSender,
        DigitalIn inA,
        DigitalIn inB,
        DigitalIn inC,
        DigitalIn inD
    ) {
        this.senderToReader = senderToReader;
        this.readerToSender = readerToSender;
        this.inA = inA;
        this.inB = inB;
        this.inC = inC;
        this.inD = inD;
    }

    public void update() {
        boolean input = senderToReader.get();

        if (input == previousInput) {
            return;
        }

        boolean output = !previousOutput;

        Block block = Block.readBlock(inA, inB, inC, inD);
        blocks.add(block);
        readerToSender.set(output);

        previousInput = input;
        previousOutput = output;
    }

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
