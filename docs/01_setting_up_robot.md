# Setting up the robot
This tutorial covers how to set up a basic Pathfinder-enabled robot.

The [calibration guide](./14_calibration.md) is a great starting point for
trying to make sure your robot is properly set up. You should (almost
certainly) give this a brief look-over before diving into the library.

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
