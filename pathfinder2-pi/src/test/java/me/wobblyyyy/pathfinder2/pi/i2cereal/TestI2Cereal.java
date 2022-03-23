package me.wobblyyyy.pathfinder2.pi.i2cereal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.wobblyyyy.pathfinder2.pi.Block;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalIn;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalOut;
import me.wobblyyyy.pathfinder2.pi.pins.SimulatedIn;
import me.wobblyyyy.pathfinder2.pi.pins.SimulatedOut;
import org.junit.jupiter.api.Test;

public class TestI2Cereal {
    /*
     * ap --> arduino to pi
     * pa --> pi to arduino
     */

    private DigitalIn apSenderToReader = new SimulatedIn();
    private DigitalOut apReaderToSender = new SimulatedOut();
    private DigitalIn apA = new SimulatedIn();
    private DigitalIn apB = new SimulatedIn();
    private DigitalIn apC = new SimulatedIn();
    private DigitalIn apD = new SimulatedIn();

    private DigitalIn paReaderToSender = new SimulatedIn();
    private DigitalOut paSenderToReader = new SimulatedOut();
    private DigitalOut paA = new SimulatedOut();
    private DigitalOut paB = new SimulatedOut();
    private DigitalOut paC = new SimulatedOut();
    private DigitalOut paD = new SimulatedOut();

    private I2CerealIn input = new I2CerealIn(
        apSenderToReader,
        apReaderToSender,
        apA,
        apB,
        apC,
        apD
    );
    private I2CerealOut output = new I2CerealOut(
        paReaderToSender,
        paSenderToReader,
        paA,
        paB,
        paC,
        paD
    );
    private I2Cereal cereal = new I2Cereal(input, output);

    @Test
    public void testI2Cereal() {}

    @Test
    public void testTransmitSingleBlock() {
        Block block = new Block(false, false, true, false);
    }
}
