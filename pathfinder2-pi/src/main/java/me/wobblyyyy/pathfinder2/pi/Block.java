package me.wobblyyyy.pathfinder2.pi;

import me.wobblyyyy.pathfinder2.pi.pins.DigitalIn;
import me.wobblyyyy.pathfinder2.pi.pins.DigitalOut;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

public class Block {
    private boolean a = false;
    private boolean b = false;
    private boolean c = false;
    private boolean d = false;

    public Block() {}

    public Block(boolean a, boolean b, boolean c, boolean d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public static void writeBlock(
        Block target,
        DigitalOut outA,
        DigitalOut outB,
        DigitalOut outC,
        DigitalOut outD
    ) {
        outA.set(target.a);
        outB.set(target.b);
        outC.set(target.c);
        outD.set(target.d);
    }

    public static Block readBlock(
        DigitalIn inA,
        DigitalIn inB,
        DigitalIn inC,
        DigitalIn inD
    ) {
        return readBlock(new Block(), inA, inB, inC, inD);
    }

    public static Block readBlock(
        Block target,
        DigitalIn inA,
        DigitalIn inB,
        DigitalIn inC,
        DigitalIn inD
    ) {
        target.setA(inA.get());
        target.setB(inB.get());
        target.setC(inC.get());
        target.setD(inD.get());

        return target;
    }

    public static byte convertToByte(Block a, Block b) {
        int result = 0;

        boolean[] bits = new boolean[] {
            a.a,
            a.b,
            a.c,
            a.d,
            b.a,
            b.b,
            b.c,
            b.d
        };

        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                result |= 1 << i;
            }
        }

        return (byte) result;
    }

    public boolean getA() {
        return a;
    }

    public Block setA(boolean a) {
        this.a = a;

        return this;
    }

    public boolean getB() {
        return b;
    }

    public Block setB(boolean b) {
        this.b = b;

        return this;
    }

    public boolean getC() {
        return c;
    }

    public Block setC(boolean c) {
        this.c = c;

        return this;
    }

    public boolean getD() {
        return d;
    }

    public Block setD(boolean d) {
        this.d = d;

        return this;
    }

    public void write(
        DigitalOut outA,
        DigitalOut outB,
        DigitalOut outC,
        DigitalOut outD
    ) {
        writeBlock(this, outA, outB, outC, outD);
    }

    @Override
    public String toString() {
        return StringUtils.format(
            "%s%s%s%s",
            a ? '1' : '0',
            b ? '1' : '0',
            c ? '1' : '0',
            d ? '1' : '0'
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            Block b = (Block) obj;

            boolean sameA = this.a == b.a;
            boolean sameB = this.b == b.b;
            boolean sameC = this.c == b.c;
            boolean sameD = this.d == b.d;

            return sameA && sameB && sameC && sameD;
        }

        return false;
    }
}
