/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.geometry.Translation;

public class SwerveDriveKinematics implements ForwardsKinematics<SwerveState> {
    private final double width;
    private final double height;
    private final double r;
    private final double widthR;
    private final double heightR;

    public SwerveDriveKinematics(double width,
                                 double height) {
        this.width = width;
        this.height = height;
        r = Math.hypot(width, height);
        widthR = width / r;
        heightR = height / r;
    }

    @Override
    public SwerveState calculate(Translation translation) {
        double x = translation.vx();
        double y = translation.vy();
        double z = translation.vz();

        double a = (x - z) * widthR;
        double b = (x + z) * widthR;
        double c = (y - z) * heightR;
        double d = (y + z) * heightR;

        double speedFr = Math.hypot(b, d);
        double speedFl = Math.hypot(b, c);
        double speedBr = Math.hypot(a, d);
        double speedBl = Math.hypot(a, c);

        return null;
    }
}
