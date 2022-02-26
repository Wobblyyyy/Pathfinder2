package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.spline.AdvancedSplineTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAdvancedSplineTrajectory {
    private Pathfinder pathfinder;
    private SimulatedOdometry odometry;
    private SplineBuilderFactory factory;

    @BeforeEach
    public void before() {
        pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        odometry = (SimulatedOdometry) pathfinder.getOdometry();
        factory = new SplineBuilderFactory()
                .setStep(0.1)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5));
    }

    @Test
    public void testLinearSpline() {
        Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
                .setStep(0.1)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(10, 10, 0))
                .add(new PointXYZ(12, 12, 0))
                .build();

        pathfinder.followTrajectory(trajectory);
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        Assertions.assertEquals(
                Translation.fromPointXY(
                        PointXY.ZERO.inDirection(0.5, Angle.fromDeg(45))),
                pathfinder.getTranslation()
        );
        PointXYZ current = pathfinder.getPosition();
        Assertions.assertEquals(0.5, trajectory.speed(current));
        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        Assertions.assertEquals(
                Translation.fromPointXY(
                        PointXY.ZERO.inDirection(0.5, Angle.fromDeg(45))),
                pathfinder.getTranslation()
        );
        Assertions.assertEquals(0.5, trajectory.speed(current));
        odometry.setRawPosition(new PointXYZ(12, 12, 0));
        pathfinder.tick();
        Assertions.assertEquals(
                new Translation(0, 0, 0),
                pathfinder.getTranslation()
        );
        Assertions.assertFalse(pathfinder.isActive());
    }

    @Test
    public void testNonLinearSplineSpeed() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        SimulatedOdometry odometry = (SimulatedOdometry) pathfinder.getOdometry();
        Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
                .setStep(0.1)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .add(new PointXYZ(0, 0, 0))
                .setSpeed(0.75)
                .add(new PointXYZ(10, 10, 0))
                .setSpeed(1)
                .add(new PointXYZ(11, 12, 0))
                .build();

        pathfinder.followTrajectory(trajectory);
        pathfinder.tick();
        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        pathfinder.tick();
        odometry.setRawPosition(new PointXYZ(11, 12, 0));
        pathfinder.tick();
    }

    @Test
    public void testArcSpline() {
        Trajectory trajectory = factory.builder()
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(10, 0, 0).inDirection(10, Angle.fixedDeg(135)))
                .add(new PointXYZ(10, 10, 0))
                .build();

        pathfinder.followTrajectory(trajectory);
    }
}
