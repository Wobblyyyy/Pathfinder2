# Controllers
Controllers are a pillar of autonomous navigation. Basically, the idea is
that you have a controller (a device which receives an input, and based on
that input, determines an output). The controller's input is some arbitrary
value (this is usually from a sensor on the robot or a joystick) and it outputs
a value that will hopefully minimize the controller's "error" (how far from
its target the controller currently is).

## Available controllers
- [`ProportionalController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/ProportionalController.java)
- [`PIDController`](../pathfinder2-kinematics/src/main/java/me/wobblyyyy/pathfinder2/control/PIDController.java)
- [`WPIPIDController`](../pathfinder2-frc/src/main/java/me/wobblyyyy/pathfinder2/wpilib/WPIPIDController.java)
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

### Example: how a proportional controller works
We're going to use a `ProportionalController` here, because it's the simplest
type of controller, meaning it'll be the most easy to explain. Before we
actually start tuning anything, we'll need to learn how a proportional
controller works.

Let's start by taking a look at the `calculate` method in the
`ProportionalController` class:
```java
@Override
public double calculate(double value) {
    // find the difference between the current and target values
    double delta = getTarget() - value;

    // multiply delta by the controller's coefficient and ensure it fits
    // in between the minimum and maximum values
    return MinMax.clip(delta * coefficient, getMin(), getMax());
}
```

As an example: say the controller's target is 10, and `value` is 2. `delta`
would then be `8` (10 - 2 = 8). If the controller's coefficient is 0.1, the
controller would then return 0.1 * 8, which is 0.8. If the controller's
coefficient were negative (say, -0.01), the `calculate` method would then
return -0.08 instead.

### Example: how Pathfinder utilizes a turn controller
As you've probably figured out by now, Pathfinder uses a controller to control
your robot's rotation. This controller will ALWAYS have a target value of 0,
and will ALWAYS be provided with the minimum delta to the target angle, in
degrees. _Say you're facing 45 degrees and you want to be facing 90 degrees -
the controller's target would be 0, but the controller's input would be 45
(target - current, or, in this case, 90 - 45)._ 

Here are a couple more examples:

| # | Current angle (degrees) | Target angle (degrees) | Controller input |
|---|-------------------------|------------------------|------------------|
| 1 | 45                      | 90                     | 45               |
| 2 | 45                      | 0                      | -45              |
| 3 | 45                      | 135                    | 90               |
| 4 | 0                       | 270                    | -90              |
| 5 | 0                       | 90                     | 90               |
| 6 | 90                      | 45                     | -45              |

#### Important notice about turn controllers
- __The maximum value a controller will ever receive as input is 180.__
- __Turn controller coefficients should almost always be negative.__

### Determining a good coefficient
The only way to determine what coefficient will work best for your specific use
case is to test it... over... and over... and over... and over again. However,
you can try to figure out a good starting point, to reduce the amount of time
it takes to tune the controller.

Say you're using a `ProportionalController`, right? How would you figure out
what a decent coefficient might be? Recall the maximum value this controller
will ever receive as input is 180, and the minimum is -180. The controller will
calculate `vz` values for your robot, which, in most cases, will only ever be
between -1 and 1, inclusive (note it CAN be outside of this range, but it's
somewhat uncommon). Given that we know our input will be between -180 and 180,
and our output will be between -1 and 1, the "ideal" coefficient would
logically have to be 0.005.

I'll tell you that this is NOT actually the ideal coefficient: your robot will
turn to slow. Where would I suggest you start? Try -0.05.

### Setting up a `Controller` as a turn controller
This is completely optional, but it might be helpful. This sets the minimum and
maximum values a controller can output.
```java
Controller controller =
    new ProportionalController(-0.05)
        .setMin(-1.0)
        .setMax(1.0);
```

## Further reading
- [Open-loop controller](https://en.wikipedia.org/wiki/Open-loop_controller)
- [PID controller](https://en.wikipedia.org/wiki/PID_controller)
- [PID control in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html)
