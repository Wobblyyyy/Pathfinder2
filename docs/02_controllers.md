# Controllers
Controllers are a pillar of autonomous navigation. Basically, the idea is
that you have a controller (a device which receives an input, and based on
that input, determines an output). The controller's input is some arbitrary
value (this is usually from a sensor on the robot or a joystick) and it outputs
a value that will hopefully minimize the controller's "error" (how far from
its target the contoller currently is).

## Available controllers
- [`ProportionalController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/ProportionalController.java)
- [`PIDController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/PIDController.java)
- [`AbstractController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/AbstractController.java)
- [`SplineController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/SplineController.java)
- [`Controller` interface](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/Controller.java)
- [`control` package](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/)

## Choosing the right controller
The most common type of controller in FTC and FRC environments is the PID
controller (available [here](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/PIDController.java)).
The [`ProportionalController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/ProportionalController.java)
may be preferred over the `PIDController` due to its simplicity: when I first
started writing Pathfinder, I didn't write a PID controller because I was unsure
of how best to test it, so the `ProportionalController` became the de-facto
controller. With that being said, however, the `PIDController` should work
perfectly fine. _As a brief aside, if you're using wpilib, you can use use
`WPIPIDController` if you'd like._

## Tuning your controller
Unfortunately, there's no magic way to immediately tune a controller.
Depending on what the controller is being used for, it'll have to be tuned
differently. The most common controller application in Pathfinder (and the only
application of the `Controller` interface that's required in order to have a
robot capable of turning) is a "turn controller," which is, in essence,
responsible for... well, making your robot turn. Crazy, I know.

### Example: tuning a proportional turn controller
We're going to use a `ProportionalController` here, because it's the simplest
type of controller, meaning it'll be the most easy to explain. Before we
actually start tuning anything, we'll need to learn how a proportional
controller works.

## Further reading
- [Open-loop controller](https://en.wikipedia.org/wiki/Open-loop_controller)
- [PID controller](https://en.wikipedia.org/wiki/PID_controller)
- [PID control in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html)
