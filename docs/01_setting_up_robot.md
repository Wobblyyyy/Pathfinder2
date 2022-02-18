# Setting up the robot
This tutorial covers how to set up a basic Pathfinder-enabled robot.

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
