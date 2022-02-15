package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.spline.AdvancedSplineTrajectoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAdvancedSplineTrajectory {
    @Test
    public void testLinearSpline() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        SimulatedOdometry odometry = (SimulatedOdometry) pathfinder.getOdometry();
        Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
                .setStep(0.1)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(10, 10, 0))
                .add(new PointXYZ(11, 12, 0))
                .build();

        pathfinder.followTrajectory(trajectory);
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        Assertions.assertEquals(
                new Translation(0.354, 0.356, -0.018),
                pathfinder.getTranslation()
        );
        PointXYZ current = pathfinder.getPosition();
        Assertions.assertEquals(0.5, trajectory.speed(current));
        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        Assertions.assertNotEquals(
                new Translation(0.354, 0.356, -0.018),
                pathfinder.getTranslation()
        );
        Assertions.assertEquals(0.5, trajectory.speed(current));
        odometry.setRawPosition(new PointXYZ(11, 12, 0));
        pathfinder.tick();
        Assertions.assertEquals(
                new Translation(0, 0, 0),
                pathfinder.getTranslation()
        );
        Assertions.assertFalse(pathfinder.isActive());
    }
}
