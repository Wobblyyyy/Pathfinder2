# Making a tank drive
A tank drive (also known as a "differential drive") is a type of drivetrain
that has "right" and "left" speeds, much like an actual tank. The right and
left sides of the drivetrain are controlled separately. This guide (or bit of
documentation, I guess) covers the following:
- Concept behind a tank drive
- Making a tank drive controllable via joysticks
- Making a tank drive controllable via `Translation`s
- Tracking the position by using odometry
- Complete example
- Tips for effective use of a tank drive

## What's a tank drive?
> Many mobile robots use a drive mechanism known as differential drive. It
> consists of 2 drive wheels mounted on a common axis, and each wheel can
> independently driven forward or backward... By varying the velocities of
> the two wheels, we can vary the trajectories that the robot takes.
> Because the rate of rotation (source:
> [Columbia](http://www.cs.columbia.edu/~allen/F17/NOTES/icckinematics.pdf))

![Tank](https://cdn.mos.cms.futurecdn.net/A8yMchdMoP3Qk87eEp4gMZ-1200-80.jpg)

Take a look at that beautiful wireframe image right above this caption. I
think we could both agree that the item depicted is, in fact, a tank. It's used
quite commonly in FRC and FTC environments for two reasons:
1. It's easy to set up and understand the math behind
2. It's incredibly rugged

## Making a tank drive controllable via joysticks
The examples you're about to read over assumes that you're using the FTC SDK.
If you're not... oh well!

### With two motors
```java
@TeleOp(name = "ExampleTankDrive", group = "default")
public class ExampleTankDrive extends LinearOpMode {
    @Override
    public void runOpMode() {
        // initialize the motors
        DcMotor rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        DcMotor leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");

        // wait for the op mode to start
        waitForStart();

        // run this code on loop until the program ends
        while (opModeIsActive()) {
            // get right and left power values from the joystick
            double rightPower = -gamepad1.right_stick_y;
            double leftPower = gamepad1.left_stick_y;

            // set power to the motors
            rightMotor.setPower(rightPower);
            leftMotor.setPower(leftPower);
        }
    }
}
```

### With four motors
```java
@TeleOp(name = "ExampleTankDrive", group = "default")
public class ExampleTankDrive extends LinearOpMode {
    @Override
    public void runOpMode() {
        // initialize the motors
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        // wait for the op mode to start
        waitForStart();

        // run this code on loop until the program ends
        while (opModeIsActive()) {
            // get right and left power values from the joystick
            double rightPower = -gamepad1.right_stick_y;
            double leftPower = gamepad1.left_stick_y;

            // set power to the motors
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);
            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
        }
    }
}
```

## Making a tank drive controllable via `Translation`s
The easiest way to control a tank drive is by using independent right and left
values. However, in order to use your tank drive with Pathfinder, you'll also
need to make your tank drive capable of accepting `Translation` values. In
order to do this, you need to know the track width of your robot. This is
how far (in your unit of choice) the wheels that move the robot are from
each other (side-to-side distance, that is).

### Wrapper `Motor`
You'll need to make a wrapper class that implements the `Motor` interface.
```java
public class MotorWrapper extends BaseMotor {
    private final DcMotor motor;

    public MotorWrapper(DcMotor motor) {
        this.motor = motor;
    }

    @Override
    public void abstractSetPower(double power) {
        motor.setPower(power);
    }
}
```

### Setting up `TankDriveKinematics`
You'll also need an instance of `TankDriveKinematics` in order to calculate
power values based on `Translation`s.
```java
// -0.05 is a decent starting place, but you'll need to fine-tune this value
// values with a higher absolute value will make the robot turn more
// dramatically, while values with a lower absolute value will make the robot
// turn at a slower pace
double TURN_COEFFICIENT = -0.05;
double ROBOT_WIDTH = 12;

TankDriveKinematics kinematics = new TankDriveKinematics(
    TURN_COEFFICIENT,
    ROBOT_WIDTH
);
```

#### Using `TankDriveKinematics`
Assuming you have an instance of `TankDriveKinematics` set up, you can now
calculate `TankState`s from `Translation`s.
```java
double TURN_COEFFICIENT = -0.05;
double ROBOT_WIDTH = 12;

TankDriveKinematics kinematics = new TankDriveKinematics(
    TURN_COEFFICIENT,
    ROBOT_WIDTH
);

Translation translation = new Translation(0, 1, 0);
TankState state = kinematics.calculate(translation);

// would get set to (the) right motor(s)
double right = state.right();

// would get set to (the) left motor(s)
double left = state.left();
```

### Using `TankDrive`
Pathfinder provides a `TankDrive` class to simplify the coding for robots that
use a tank/differential drive.
```java
TankDriveKinematics kinematics = new TankDriveKinematics(-0.05, 12);

DcMotor dcFrontRight = hardwareMap.get(DcMotor.class, "frontRight");
DcMotor dcFrontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
DcMotor dcBackRight = hardwareMap.get(DcMotor.class, "backRight");
DcMotor dcBackLeft = hardwareMap.get(DcMotor.class, "backLeft");

Motor right = new MultiMotor(
    new MotorWrapper(dcFrontRight),
    new MotorWrapper(dcBackRight),
);
Motor left = new MultiMotor(
    new MotorWrapper(dcFrontLeft),
    new MotorWrapper(dcBackLeft),
);

TankDrive drive = new TankDrive(right, left, kinematics);

// make the robot move straight forwards
drive.setTranslation(new Translation(0, 1, 0));
```

## Tracking the position by using odometry
Tracking the position of a tank drive robot is relatively simple. You just
two encoders and a gyroscope.

### Calculating distance of encoders
There's a nice little class named `EncoderConverter` that's designed to
help with exactly that. Its constructor has two parameters:
- The circumference of the drive wheel
- The encoder's "counts per revolution" value

### Using `TankDriveOdometry`
Alright. Now that we know how to track encoders, it's time to get set up with
an instance of `TankDriveOdometry`. Assume we have a gyroscope and the
`getGyroAngle` supplier returns the angle of that gyroscope.
```java
Supplier<Angle> getGyroAngle = () -> Angle.fromDeg(0);
```

```java
DcMotor right = hardwareMap.get(DcMotor.class, "right");
DcMotor left = hardwareMap.get(DcMotor.class, "left");

double COUNTS_PER_REVOLUTION = 1_024;
double WHEEL_CIRCUMFERENCE = 12;

EncoderConverter converter = new EncoderConverter(
    COUNTS_PER_REVOLUTION,
    WHEEL_CIRCUMFERENCE
);

Supplier<Double> getRightDistance = () -> {
    return converter.distanceFromTicks(right.getCurrentPosition());
};
Supplier<Double> getLeftDistance = () -> {
    return converter.distanceFromTicks(left.getCurrentPosition());
};

TankDriveOdometry odometry = new TankDriveOdometry(
    getRightDistance,
    getLeftDistance,
    getGyroAngle
);
```

`TankDriveOdometry` is an implementation of the `Odometry` interface, so there
you go: you're ready to start using Pathfinder!

## Complete example
Warning: it's a lot of boilerplate code. Sorry!
```java
@TeleOp(name = "ExampleTankDrive", group = "default")
public class ExampleTankDrive extends LinearOpMode {
    private final double COUNTS_PER_REVOLUTION = 1_024;
    private final double WHEEL_CIRCUMFERENCE = 12;

    @Override
    public void runOpMode() {
        // initialize the motors
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        Motor right = new MultiMotor(
            new MotorWrapper(frontRight),
            new MotorWrapper(backRight),
        );
        Motor left = new MultiMotor(
            new MotorWrapper(frontLeft),
            new MotorWrapper(backLeft),
        );

        EncoderConverter converter = new EncoderConverter(
            COUNTS_PER_REVOLUTION,
            WHEEL_CIRCUMFERENCE
        );

        Supplier<Double> getRightDistance = () -> {
            return converter.distanceFromTicks(right.getCurrentPosition());
        };
        Supplier<Double> getLeftDistance = () -> {
            return converter.distanceFromTicks(left.getCurrentPosition());
        };

        Supplier<Angle> getGyroAngle = () -> Angle.fromDeg(0);

        Kinematics<TankState> kinematics = new TankDriveKinematics(
            TURN_COEFFICIENT,
            ROBOT_WIDTH
        );
        Angle gyroAngle = getGyroAngle.get();
        PointXYZ initialPosition = PointXYZ.ZERO;

        TankDrive drive = new TankDrive(right, left, kinematics);
        TankDriveOdometry odometry = new TankDriveOdometry(
            getRightDistance,
            getLeftDistance,
            getGyroAngle
        );

        Robot robot = new Robot(drive, odometry);
        Pathfinder pathfinder = new Pathfinder(robot, -0.05);

        pathfinder.onTick((pf) -> {
            pathfinder.setTranslation(new Translation(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x
            ));
        });

        // wait for the op mode to start
        waitForStart();

        // run this code on loop until the program ends
        while (opModeIsActive()) {
            pathfinder.tick();
        }
    }
}
```

## Tips for effective use of a tank drive
Now that you've built a tank drive and what not, you might want to know a few
things about how to operate your robot so that you can effectively utilize
your robot.
- Avoid using `Translation`s when you're manually controlling the robot! A
  tank drive can be controlled better by 2 joystick inputs than via
  `Translation`s (at least when a human is driving the robot).
- Limit movement along the X axis. Moving along the X axis requires the robot
  to turn, which can slow the robot down significantly. When possible, only
  move along the robot's Y axis (or turn).
- Face the direction you're moving, all the time. Make sure your adjacent
  trajectories allow for this by stopping the robot facing the
  correct orientation.
- Avoid small X adjustments. These are particularly challenging for Pathfinder
  to deal with: in order to move along the X axis, the robot must first turn.
  After turning, the robot must then move a certain amount of distance. If
  there's any slip, or any inaccuracies, the robot's position will be messed
  up, and it'll fall into an infinite loop of trying to correct for an
  increasingly inaccurate position.
