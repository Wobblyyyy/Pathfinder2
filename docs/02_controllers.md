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

## Further reading
- [Open-loop controller](https://en.wikipedia.org/wiki/Open-loop_controller)
- [PID controller](https://en.wikipedia.org/wiki/PID_controller)
- [PID control in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html)
