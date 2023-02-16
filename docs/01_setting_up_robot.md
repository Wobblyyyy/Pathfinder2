# Setting up the robot
This tutorial covers how to set up a basic Pathfinder-enabled robot.

The [calibration guide](./14_calibration.md) is a great starting point for
trying to make sure your robot is properly set up. You should (almost
certainly) give this a brief look-over before diving into the library.

## Important notice
Choose a unit, and stick to it! This has been mentioned before, but I'd like to
very explicitly state this once again: _the measurement values you provide when
creating an odometry and drive instance will determine how your robot localizes
itself. If you use inches, use inches EVERYWHERE - likewise with every other
unit. Using different units will give you a headache!_

## Setting up the drive train
Before you begin using Pathfinder, you'll need to make sure your drivetrain
is both set up and properly calibrated. First, determine what kind of drive
system you have:
- Tank drive (each of the robot's two horizontal sides (right and left)) move
  independently of each other)
- Mecanum drive (four (or more) wheels that apply power on a 45 degree angle,
  allows omnidirectional movement)
- Swerve drive (three (or more, typically four) modules are controlled
  independently and able to face any direction, allowing for omnidirectional
  movement and turning while moving)

These are the most common types of drive (and the types of drive Pathfinder
supports out-of-box), but I'm sure there are other types of drive train
that you could use.

## Setting up the odometry system
There are a variety of odometry systems available, including:
- Three-wheel odometry (with dead wheels)
- Two-wheel odometry (with dead wheels)
- Encoder-based drive train odometry
- T265 webcam based odometry
- VSLAM (not supported out-of-box)

### Three-wheel odometry (with dead wheels)
The easiest way to implement three-wheel odometry is to make use of the class
`import me.wobblyyyy.pathfinder2.odometrycore.ThreeWheelOdometry`, an extension
of the [`OdometryCore`](https://github.com/tmthecoder/OdometryCore) library. To
view an abstract implementation of three-wheel odometry, check out the example,
[`ThreeWheelOdometry`](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-examples/src/main/java/me/wobblyyyy/pathfinder2/examples/ExampleThreeWheelOdometry.java).

Three-wheel odometry has a couple of requirements:
- You __MUST__ have three encoders with identical CPR values
- You __MUST__ have wheels with identical diameter (ideally, you should use the
  same type of wheel to avoid any error caused by differening amounts of
  slippage in between wheels)
- You __MUST__ know the position of each encoder, relative to the center of the
  robot.

To create an instance of `ThreeWheelOdometry`, you require two objects:
- A `ThreeWheelOdometry.ThreeWheelOdometryProfile`
- A `ThreeWheelOdometry.EncoderProfile`

#### `ThreeWheelOdometry.ThreeWheelOdometryProfile`
The constructor can be invoked just like this:
```
ThreeWheelOdometry.ThreeWheelOdometryProfile odometryProfile = new ThreeWheelOdometry.ThreeWheelOdometryProfile(
    CPR,
    WHEEL_DIAMETER,
    OFFSET_LEFT,
    OFFSET_RIGHT,
    OFFSET_CENTER
);
```

##### Counts per revolution
This is your encoder's CPR/counts per revolution value. If you don't know it,
you can find it out by looking up the specifications of the specific type of
encoder you're using. Safe bets are powers of 2: the most common CPR values
I've found are 1024 and 4096, but you should __not__ simply guess what this
value is, or you'll end up with a terribly inaccurate odometry implementation.

##### Wheel diameter
The diameter of each of the wheels on your robot, in the same unit you plan on
measuring position in. If you want your robot's position to be tracked in
_inches_, provide the diameter of each of your robot's wheels, in _inches_.
Likewise, if you want to track your position in, say, _centimeters_, provide the
measurement of each wheel's diameter, in _centimeters_.

##### Offset
There are three offsets:
- Left offset (how far the __LEFT__ encoder is from the exact center of your
  robot, in your unit of choice) (most likely X distance)
- Right offset (how far the __RIGHT__ encoder is from the exact center of your
  robot, in your unit of choice) (most likely X distance)
- Center offset (how far the __CENTER__ encoder is from the exact center of your
  robot, in your unit of choice) (most likely Y distance)

_Each of these offset values is dependent __ONLY__ on an X or Y position -
never both! Make sure that the __left/right__ and __center__ encoders use X
position and Y position (respectively!) or Y/X (respectively!)_

#### `ThreeWheelOdometry.EncoderProfile`
An `EncoderProfile` literally just contains references to methods that will
access each encoder's current "tick" value.
```
// create an EncoderProfile to store references to methods that
// provide the encoder's count. note that leftEncoder, rightEncoder,
// and centerEncoder must all be initialized, or a NullPointerException
// will show up whenever trying to determine the robot's position.
// 
// specifically in FTC, it is likely easier to not use the Encoder
// interface provided with Pathfinder, and rather get values directly
// from the motors, like so:
//
// DcMotor rightEncoder;
// DcMotor leftEncoder;
// DcMotor centerEncoder;
// ThreeWheelOdometry.EncoderProfile encoderProfile = new ThreeWheelOdometry.EncoderProfile(
//     () -> (double) leftEncoder.getCurrentPosition(),
//     () -> (double) rightEncoder.getCurrentPosition(),
//     () -> (double) centerEncoder.getCurrentPosition()
// );
//
// of course rightEncoder, leftEncoder, and centerEncoder will need
// to be initialized with the hardware map first.
ThreeWheelOdometry.EncoderProfile encoderProfile = new ThreeWheelOdometry.EncoderProfile(
    () -> leftEncoder.getTicks(),
    () -> rightEncoder.getTicks(),
    () -> centerEncoder.getTicks()
);
```

### Two-wheel odometry (with dead wheels)
This section needs to be expanded in the future!

### Encoder-based drive train odometry
This section needs to be expanded in the future!

### T265 webcam based odometry
I can't lie, since graduating out of both FTC and FRC, I no longer have access
to a T265 webcam that could be used to test an odometry implementation. Such,
I'm simply going to say that T265 webcam based odometry is not supported
out-of-box, but can absolutely be implemented if you're in the mood to do a good
bit of coding yourself.

### VSLAM (not supported out-of-box)
VSLAM is most definitely not supported out-of-box. If you can somehow find a way
to simultaneously localize and map your surroundings on one of the phones or
control hubs, good for you. You're a better man (or woman (or person, for all my
lovely, hot, and sexy non-binary folk)) than I am.

### Other types of odometry (custom)
If you implement the `AbstractOdometry` interface, setting up an odometry
system should be pretty simple.

## Setting up an instance of Pathfinder
To instantiate Pathfinder, at the very least, you should have a `Controller`
responsible for controlling the robot's angle and a `Robot` object, which
is simply a wrapper for `Drive` and `Odometry`.

`Controller` tutorial: [click here](../docs/02_controllers.md)

```java
Controller turnController = new ProportionalController(0.01);

// this assumes you have instances of Drive and Odometry
Drive drive = null;
Odometry odometry = null;
Robot robot = new Robot(drive, odometry);

Pathfinder pathfinder = new Pathfinder(robot, turnController);
```

## Further reading
- [Calibration guide](https://wobblyyyy.github.io/docs/pathfinder2/calibration.html)
- [Basic tutorial](https://wobblyyyy.github.io/docs/pathfinder2/tutorial.html)
