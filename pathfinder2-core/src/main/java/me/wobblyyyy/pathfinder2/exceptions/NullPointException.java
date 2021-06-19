/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.exceptions;

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Exception to be thrown whenever an operation is requested to be performed
 * on a null point.
 *
 * <p>
 * If you keep getting this exception, I'd suggest you check out the very
 * these very lovely methods.
 * <ul>
 *     <li>{@link PointXY#zeroIfNull(PointXY)}</li>
 *     <li>{@link PointXYZ#zeroIfNull(PointXYZ)}</li>
 * </ul>
 * Those methods will ensure that any point you pass is never null. If the
 * point would have been null, the method will return the result of the
 * {@link PointXY#zero()} method.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class NullPointException extends RuntimeException {
    public NullPointException(String s) {
        super(s);
    }
}