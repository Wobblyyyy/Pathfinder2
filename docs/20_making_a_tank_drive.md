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

### Understanding `EncoderTracker`
`EncoderTracker` can be used to track the velocity of an encoder. It allows you
to convert encoder counts to distance driven. Part of `EncoderTracker` is
`EncoderConverter`, which converts encoder counts into distance values by using
the counts per revolution of the encoder, as well as the circumference of the
wheel(s) that the encoder is tracking.

#### Creating an `EncoderConverter`
There are two parameters you need to supply: counts per revolution and wheel
circumference.
- To find the "counts per revolution" value, locate specifications about your
  encoder and determine the CPR. Google is a good place to start: type in your
  encoder's name and "CPR".
- Wheel circumference can be calculated by multiplying the radius of the wheel
  by 2 pi. You can find the radius of the wheel by (a) measuring the wheel or
  (b) Googling it.

```java
double COUNTS_PER_REVOLUTION = 1_024;
double WHEEL_CIRCUMFERENCE = 12.0;

EncoderConverter converter = new EncoderConverter(
    COUNTS_PER_REVOLUTION,
    WHEEL_CIRCUMFERENCE
);
```

#### Creating an `EncoderTracker`
Alright - now it's time to create our `EncoderTracker`. The `EncoderTracker`
constructor requires a `Supplier<Integer>` that will supply tick values from
the encoder.
```java
DcMotor right = hardwareMap.get(DcMotor.class, "right");
DcMotor left = hardwareMap.get(DcMotor.class, "left");

// in case you're not familiar with the FTC SDK:
// DcMotors are encoded, and you can access that encoder's position by
// calling DcMotor#getCurrentPosition() (it returns an int, representing
// the encoder's counts)
Supplier<Integer> rightTicks = right::getCurrentPosition;
Supplier<Integer> leftTicks = left::getCurrentPosition;

EncoderConverter converter = new EncoderConverter(1_024, 12.0);

EncoderTracker rightTracker = new EncoderTracker(converter, rightTicks);
EncoderTracker leftTracker = new EncoderTracker(converter, leftTicks);
```

`EncoderTracker` allows you to track the velocity (or speed) of the encoder.
This is important, as `TankDriveOdometry` requires that we keep track of the
velocity of the encoders.

### Using `TankDriveOdometry`
Alright. Now that we know how to track encoders, it's time to get set up with
an instance of `TankDriveOdometry`. Assume we have a gyroscope and the
`getGyroAngle` supplier returns the angle of that gyroscope.
```java
Supplier<Angle> getGyroAngle = () -> Angle.fromDeg(0);
```

#### `GenericOdometry<TankState>`
First, we need to have an instance of `GenericOdometry`. `GenericOdometry` is,
as the name would suggest, a generic odometry implementation that tracks the
position of the robot over time by integrating its velocity. The
`GenericOdometry` constructor accepts the following parameters:
- `Kinematics<T> kinematics`
- `Angle gyroAngle`
- `PointXYZ initialPosition`
- `double updateIntervalMs`

Recall the `TankDriveKinematics` declaration we've already performed.
```java
double TURN_COEFFICIENT = -0.05;
double ROBOT_WIDTH = 12;

Kinematics<TankState> kinematics = new TankDriveKinematics(
    TURN_COEFFICIENT,
    ROBOT_WIDTH
);
```

Let's create an instance of `GenericOdometry` then, shall we?
```java
Kinematics<TankState> kinematics = new TankDriveKinematics(
    TURN_COEFFICIENT,
    ROBOT_WIDTH
);
Angle gyroAngle = Angle.fromDeg(0);
PointXYZ initialPosition = PointXYZ.ZERO;
double updateIntervalMs = 5;
GenericOdometry<TankState> odometry = new GenericOdometry<>(
    kinematics,
    gyroAngle,
    initialPosition,
    updateIntervalMs
);
```

#### Instantiating `TankDriveOdometry`
```java
DcMotor right = hardwareMap.get(DcMotor.class, "right");
DcMotor left = hardwareMap.get(DcMotor.class, "left");

Supplier<Integer> rightTicks = right::getCurrentPosition;
Supplier<Integer> leftTicks = left::getCurrentPosition;

EncoderConverter converter = new EncoderConverter(1_024, 12.0);

EncoderTracker rightTracker = new EncoderTracker(converter, rightTicks);
EncoderTracker leftTracker = new EncoderTracker(converter, leftTicks);

Supplier<Angle> getGyroAngle = () -> Angle.fromDeg(0);

Kinematics<TankState> kinematics = new TankDriveKinematics(
    TURN_COEFFICIENT,
    ROBOT_WIDTH
);
Angle gyroAngle = getGyroAngle.get();
PointXYZ initialPosition = PointXYZ.ZERO;
double updateIntervalMs = 5;
GenericOdometry<TankState> odometry = new GenericOdometry<>(
    kinematics,
    gyroAngle,
    initialPosition,
    updateIntervalMs
);

TankDriveOdometry odometry = new TankDriveOdometry(
    rightTracker,
    leftTracker,
    getGyroAngle,
    kinematics
);
```

`TankDriveOdometry` is an implementation of the `Odometry` interface, so there
you go: you're ready to start using Pathfinder!

## Complete example
Warning: it's a lot of boilerplate code. Sorry!
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

        Motor right = new MultiMotor(
            new MotorWrapper(frontRight),
            new MotorWrapper(backRight),
        );
        Motor left = new MultiMotor(
            new MotorWrapper(frontLeft),
            new MotorWrapper(backLeft),
        );

        TankDrive drive = new TankDrive(right, left, kinematics);

        Supplier<Integer> rightTicks = frontRight::getCurrentPosition;
        Supplier<Integer> leftTicks = frontLeft::getCurrentPosition;

        EncoderConverter converter = new EncoderConverter(1_024, 12.0);

        EncoderTracker rightTracker = new EncoderTracker(converter, rightTicks);
        EncoderTracker leftTracker = new EncoderTracker(converter, leftTicks);

        Supplier<Angle> getGyroAngle = () -> Angle.fromDeg(0);

        Kinematics<TankState> kinematics = new TankDriveKinematics(
            TURN_COEFFICIENT,
            ROBOT_WIDTH
        );
        Angle gyroAngle = getGyroAngle.get();
        PointXYZ initialPosition = PointXYZ.ZERO;
        double updateIntervalMs = 5;
        GenericOdometry<TankState> odometry = new GenericOdometry<>(
            kinematics,
            gyroAngle,
            initialPosition,
            updateIntervalMs
        );

        TankDriveOdometry odometry = new TankDriveOdometry(
            rightTracker,
            leftTracker,
            getGyroAngle,
            kinematics
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
