/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

/**
 * Ahh. Path generation. This is also the part of Pathfinder that actually
 * finds paths. Internally, this uses the A star pathfinding algorithm
 * to find paths. This algorithm works based on integer points and a sort
 * of two-dimensional data structure ({@link java.util.HashMap} is used for
 * performance purposes), so decimal/double points need to be converted to
 * integers. How do you do this, you may be asking? I'll tell you how.
 * Use {@link me.wobblyyyy.pathfinder2.pathgen.LocalizedPathGen}! A lovely
 * class. Truly, just lovely. Fantastic, even. I'm currently in English class
 * and trying to kill time until the period ends, so I hope you're enjoying
 * this meaningless rambling.
 *
 * <p>
 * Interestingly enough, this code... sucks. If you can rewrite it so it's
 * cleaner and/or faster, you're more than welcome to do so.
 * </p>
 */
package me.wobblyyyy.pathfinder2.pathgen;
