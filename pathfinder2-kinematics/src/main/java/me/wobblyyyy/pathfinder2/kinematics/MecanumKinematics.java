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

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import org.ejml.simple.SimpleMatrix;

/**
 * Kinematics for a mecanum chassis.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class MecanumKinematics implements Kinematics<MecanumState> {
    private final PointXY frontLeftPosition;
    private final PointXY frontRightPosition;
    private final PointXY backLeftPosition;
    private final PointXY backRightPosition;
    private final SimpleMatrix forwardsKinematics;
    private SimpleMatrix inverseKinematics;
    private PointXY lastCenterOfRotation = new PointXY(0, 0);

    public MecanumKinematics(PointXY frontLeftPosition,
                             PointXY frontRightPosition,
                             PointXY backLeftPosition,
                             PointXY backRightPosition) {
        this.frontLeftPosition = frontLeftPosition;
        this.frontRightPosition = frontRightPosition;
        this.backLeftPosition = backLeftPosition;
        this.backRightPosition = backRightPosition;

        setInverseKinematics(
                frontLeftPosition,
                frontRightPosition,
                backLeftPosition,
                backRightPosition
        );

        forwardsKinematics = inverseKinematics.pseudoInverse();
    }

    public MecanumKinematics(double xSize,
                             double ySize) {
        this(
                new PointXY(-xSize / 2, ySize / 2),
                new PointXY(xSize / 2, ySize / 2),
                new PointXY(-xSize / 2, -ySize / 2),
                new PointXY(xSize / 2, -ySize / 2)
        );
    }

    public void setInverseKinematics(PointXY frontLeftPosition,
                                     PointXY frontRightPosition,
                                     PointXY backLeftPosition,
                                     PointXY backRightPosition) {
        double fl_x = frontLeftPosition.x();
        double fr_x = frontRightPosition.x();
        double bl_x = backLeftPosition.x();
        double br_x = backRightPosition.x();

        double fl_y = frontLeftPosition.y();
        double fr_y = frontRightPosition.y();
        double bl_y = backLeftPosition.y();
        double br_y = backRightPosition.y();

        SimpleMatrix temp = new SimpleMatrix(4, 3);
        temp.setRow(0, 0, 1, -1, (-fl_x + fl_y));
        temp.setRow(1, 0, 1, 1, (fr_x - fr_y));
        temp.setRow(2, 0, 1, 1, (bl_x - bl_y));
        temp.setRow(3, 0, 1, -1, -(br_x - br_y));
        inverseKinematics = temp.scale(1d / Math.sqrt(2));
    }

    public MecanumState calculate(Translation translation,
                                  PointXY centerOfRotation) {
        if (!centerOfRotation.equals(lastCenterOfRotation)) {
            PointXY fl = frontLeftPosition.subtract(centerOfRotation);
            PointXY fr = frontRightPosition.subtract(centerOfRotation);
            PointXY bl = backLeftPosition.subtract(centerOfRotation);
            PointXY br = backRightPosition.subtract(centerOfRotation);
            setInverseKinematics(fl, fr, bl, br);
            lastCenterOfRotation = centerOfRotation;
        }

        SimpleMatrix speedVector = new SimpleMatrix(3, 1);
        speedVector.setColumn(
                0,
                0,
                translation.vx(),
                translation.vy(),
                translation.vz()
        );

        SimpleMatrix wheelMatrix = inverseKinematics.mult(speedVector);

        double fl = wheelMatrix.get(0, 0);
        double fr = wheelMatrix.get(1, 0);
        double bl = wheelMatrix.get(2, 0);
        double br = wheelMatrix.get(3, 0);

        return new MecanumState(fr, fl, br, bl);
    }

    @Override
    public MecanumState calculate(Translation translation) {
        return calculate(translation, lastCenterOfRotation);
    }

    public Translation toTranslation(MecanumState state) {
        SimpleMatrix wheelSpeedsMatrix = new SimpleMatrix(4, 1);
        wheelSpeedsMatrix.setColumn(
                0,
                0,
                state.fl(),
                state.fr(),
                state.bl(),
                state.br()
        );
        SimpleMatrix speedVector = forwardsKinematics.mult(wheelSpeedsMatrix);
        return new Translation(
                speedVector.get(0, 0),
                speedVector.get(1, 0),
                speedVector.get(2, 0)
        );
    }
}
