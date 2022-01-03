/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.util;

public class Trio<A, B, C> {
    private final A a;
    private final B b;
    private final C c;

    public Trio(A a,
                B b,
                C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Trio(Trio<A, B, C> trio) {
        this(
                trio.getA(),
                trio.getB(),
                trio.getC()
        );
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
