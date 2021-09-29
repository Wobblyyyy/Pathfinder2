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
 * An expansive set of geometry functionality utilized by Pathfinder. This
 * part of Pathfinder can be very challenging to effectively document, because
 * there's so many niche methods that do so many niche things. I'd suggest
 * you read through the documentation for each of the classes you use, even
 * if you just skim over it, so you can get a general idea of what methods
 * you have at your disposal.
 *
 * <p>
 * The most commonly-used parts of the geometry package are {@code PointXY},
 * {@code PointXYZ}, and {@code Angle}. Rectangles, triangles, and shapes
 * in general are made available, but you won't need to use them to get
 * the library up and running.
 * </p>
 *
 * <p>
 * This is also the only part of the Pathfinder2 project that is not meant
 * exclusively for use within Pathfinder2 - none of the code here is very
 * specific and can be used just about anywhere.
 * </p>
 *
 * <p>
 * This isn't really an API guide or anything, but you're more than welcome
 * to add exhaustive tests. In order for this library to function properly,
 * the geometry portion has to function perfectly, and adding more tests can
 * never hurt, right? Right.
 * </p>
 */
package me.wobblyyyy.pathfinder2.geometry;
