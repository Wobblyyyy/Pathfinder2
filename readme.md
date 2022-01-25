<p align="center">
<img src="media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<b>Autonomous motion planning and control library for wheeled mobile robots.</b>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

<div align="center">
<img alt="GNU GPL V3" src="https://img.shields.io/github/license/Wobblyyyy/Pathfinder2">
<img alt="Top Language" src="https://img.shields.io/github/languages/top/wobblyyyy/Pathfinder2">
<img alt="Issues" src="https://img.shields.io/github/issues/Wobblyyyy/Pathfinder2">
<img alt="Forks" src="https://img.shields.io/github/forks/Wobblyyyy/Pathfinder2">
<img alt="Stars" src="https://img.shields.io/github/stars/Wobblyyyy/Pathfinder2">
<img alt="Latest Release (Bleeding)" src="https://img.shields.io/github/v/release/wobblyyyy/Pathfinder2?include_prereleases">
<img alt="Latest Release" src="https://img.shields.io/github/v/release/wobblyyyy/Pathfinder2">
</div>

<h1 align="center"></h1>

Pathfinder2 is a continuation of the
original [Pathfinder](https://github.com/Wobblyyyy/Pathfinder)
library - an autonomous motion planning and control system designed for FIRST
Robotics environments, such as
the [FIRST Robotics Competition](https://www.firstinspires.org/robotics/frc) and
the
[FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc). This library
will be actively developed until Q3 2022, and (hopefully) maintained long
afterwards. Pathfinder2 is focused on providing a streamlined and user-friendly
interface for interacting with topics such as odometry and motion planning.

For a fun little video tangentially related to the rest of the readme,
see [this](https://www.reddit.com/r/FTC/comments/rdqitc/guys_we_got_auton_working/).
No, our robot does not do that anymore, and no, yours won't do that either -
but you gotta admit, our robot has got some moves.

<h2 align="center">Example Code</h2>

Here's a demonstration of Pathfinder's utilization of lambda expressions to
write simple and readable code.
```java
// make the robot follow a spline. this could be used for autonomous movement
pathfinder
    .setSpeed(0.5) // a value, 0-1, how fast the robot is
    .setTolerance(2) // how much tolerance pathfinder will use when
                     // evaluating if the robot is at a certain position
    .setAngleTolerance(Angle.fixedDeg(5)) // same thing as tolerance, but
                                          // it's an angle
    .splineTo(                                     // create a spline with 3
        new PointXYZ(10, 10, Angle.fixedDeg(90)),  // very lovely points.
        new PointXYZ(20, 30, Angle.fixedDeg(45)),  // splines can have a lot
        new PointXYZ(30, 50, Angle.fixedDeg(180))  // more cool features that
                                                   // are explained later on
    )
    .tickUntil(); // "tick" pathfinder until it's done. "ticking" is basically
                  // updating the robot - it'll evaluate it's current position,
                  // and it'll use that to determine where it should move.
                  // pathfinder DOES NOT WORK unless you use the tick method!

// add listeners for the A and B buttons, as well as either of the triggers.
// this is very useful for making tele-op code
pathfinder.getListenerManager()
    .bindButton(gampad::a, (isPressed) -> {
        // code to be executed whenever the A button is pressed
    })
    .bindButton(gamepad::b, (isPressed) -> {
        // code to be executed whenever the B button is pressed
    })
    .bind(new ListenerBuilder()
        .setMode(ListenerMode.CONDITION_IS_MET) // the listener will be active
                                                // whenever the condition is
                                                // met

        .addInput(SupplierFilter.anyTrue(       // if either of the two
                                                // conditions are true, the
                                                // supplier will return true,
                                                // meaning the listener
                                                // will be activated
            () -> gamepad.rightTrigger() > 0,
            () -> gamepad.leftTrigger() > 0
        ))
        .setWhenTriggered(() -> {
            // code to be executed whenever either trigger is pressed
        })
        .setPriority(10) // how important the listener is - higher priorities
                         // are executed first
        .setExpiration(Double.MAX_VALUE), // the timestamp (ms since epoch)
                                          // that will cause the listener to
                                          // "expire," basically making it no
                                          // longer listen
        .setMaximumExecutions(Double.MAX_VALUE) // the max amount of times
                                                // the listener can be called
    );

// whenever pathfinder is "ticked" (updated, basically), set the robot's
// translation to whatever the driver's controller outputs
pathfinder.onTick(() -> {
    pathfinder.setTranslation(new Translation(
        gamepad.leftStickX(),
        gamepad.leftStickY(),
        gamepad.rightStickX()
    ));
});

while (true) {
    // tick pathfinder forever
    pathfinder.tick();
}
```

<h2 align="center">Installation</h2>

Installation instructions have been moved [here](project_releases.md).

<h2 align="center">Documentation</h2>

Here's all of the sources of documentation I can think of right now.

- [Documentation portal](https://wobblyyyy.github.io/docs/pathfinder2/documentation.html) - 
  the hub for documentation for this library.
- [Project tutorial](.github/project_tutorial.md) - this is a basic tutorial
  that walks through the fundamentals of the library. This is probably the best
  place to start getting acquainted with the library.
- [Examples](pathfinder2-examples) - several example usages of the project. Each
  of the classes contained in this module will contain more specific
  documentation explaining what the example is demonstrating.
- [JavaDoc](https://wobblyyyy.github.io/JavaDocs/Pathfinder2/0.7.0) - JavaDoc 
  for the Pathfinder2 library. This is the most fine-grained documentation, and I'd
  encourage you to check this out once you're comfortable with the library.
- [Video guides](https://google.com) - (not yet available) video guides on using
  the library. Who doesn't love videos? Right? Right.

<h2 align="center">Licensing</h2>

This project uses the [GNU GPL V3](license.md). You're allowed to freely
distribute and modify the code contained in this project, so long as a copy of
the original license is provided, and all of your code is made open-source.
