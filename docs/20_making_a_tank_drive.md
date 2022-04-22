# Making a tank drive
A tank drive (also known as a "differential drive") is a type of drivetrain
that has "right" and "left" speeds, much like an actual tank. The right and
left sides of the drivetrain are controlled separately. This guide (or bit of
documentation, I guess) covers the following:
- Concept behind a tank drive
- Making a tank drive controllable via joysticks
- Making a tank drive controllable via `Translation`s
- Tracking the position by using odometry

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
Before anything else, you'll need to make a wrapper class that implements
the `Motor` interface.
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
