package me.wobblyyyy.pathfinder2.examples;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;

/**
 * An example implementation of {@link TimedRobot}. This is rather barebones:
 * obviously, you'll want to implement more functionality for an actual
 * robot.
 *
 * @author Colin Robertson
 * @since 0.15.0
 */
public class ExampleTimedRobot extends TimedRobot {
    private final Joystick leftJoystick = new Joystick(0);
    private final Joystick rightJoystick = new Joystick(1);

    private Controller controller = new ProportionalController(0.01);
    private Drive drive;
    private Odometry odometry;
    private Robot robot;
    private Pathfinder pathfinder;

    @Override
    public void robotInit() {
        // initialize everything. if this was a real implementation, you would
        // not want to use SimulatedDrive or SimulatedOdometry
        drive = new SimulatedDrive();
        odometry = new SimulatedOdometry();
        robot = new Robot(drive, odometry);
        pathfinder =
            new Pathfinder(robot, controller)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5));
    }

    @Override
    public void teleopPeriodic() {
        // during teleop, set the robot's translation by using some joystick
        // values. the left stick controls X/Y movement, and the right stick
        // controls the robot's rotation
        pathfinder.tick();

        double vx = leftJoystick.getX();
        double vy = leftJoystick.getY();
        double vz = rightJoystick.getX();

        Translation translation = new Translation(vx, vy, vz);
        pathfinder.setTranslation(translation);
    }

    @Override
    public void autonomousInit() {
        // clear it, just in case there's already a trajectory
        pathfinder.clear();

        // make the robot follow a spline: all we have to do now is tick it!
        pathfinder.splineTo(
            new PointXYZ(0, 0, 0),
            new PointXYZ(10, 0, 0),
            new PointXYZ(10, 10, 0),
            new PointXYZ(0, 10, 0),
            new PointXYZ(0, 0, 0)
        );
    }

    @Override
    public void autonomousPeriodic() {
        // simply tick pathfinder - that's all!
        pathfinder.tick();
    }
}
