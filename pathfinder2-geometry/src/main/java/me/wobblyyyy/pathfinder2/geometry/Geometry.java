/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

/**
 * Various pseudo-constant values used within the geometry package of
 * Pathfinder.
 *
 * @author Colin Robertson
 * @since 1.1.0
 */
public class Geometry {
    public static double tolerancePointXY = 0.01;
    public static double tolerancePointXYZ = 0.01;
    public static Angle toleranceAngle = Angle.fromDeg(0.01);
    public static double toleranceCollinear = 0.01;
    public static double toleranceParallel = 0.01;
    public static double tolerancePerpendicular = 0.01;
    public static double toleranceIsVertical = 0.01;
    public static double toleranceRectangleReference = 0.01;
    public static double toleranceRectangle = 0.01;
    public static double toleranceTranslation = 0.01;
    public static String formatPointXY = "(%s, %s)";
    public static String formatPointXYZ = "(%s, %s, %s deg)";
    public static String formatAngle = "%s deg";
    public static String formatRange = "%s%s, %s%s";
    public static String formatTranslation = "(vx: %s, vy: %s, vz: %s)";

    private Geometry() {

    }
}
