package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
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
                .build();

        pathfinder.followTrajectory(trajectory);
        pathfinder.tick();

        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        pathfinder.tick();
        Assertions.assertFalse(pathfinder.isActive());
    }
}
