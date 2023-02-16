<h2 align="center">Important Notice</h2>

Hi there! At the time of writing this, I'm now enrolled in university, studying
computer engineering and computer science. Such, I don't have very much time,
and especially not enough to maintain a project of this size. I hate to say it,
but this project will likely remain dormant until either (a) I have enough time
to maintain it or (b) someone else comes along and wants to continue the
project.

If anyone is interested in possibly maintaining or continuing this project,
please let me know! Additionally, if you end up using this project in your code
and need support, I'd be glad to answer any questions. This is the largest solo
project I've worked on, and I'm content with how it turned out, although I do
wish I had more time to improve documentation and flesh out some details.

You can reach me through Discord (`wobblyyyy#6733`) or e-mail
(`carobertson1000@gmail.com`).

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

Pathfinder2 is a continuation of the
original [Pathfinder](https://github.com/Wobblyyyy/Pathfinder) library: a
streamlined motion planning and control system designed for FIRST events, such
as the [FIRST Robotics Competition](https://www.firstinspires.org/robotics/frc)
and the [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).
- Track the position of a wheeled mobile robot using a variety of odometry
  implementations.
- Dynamically correct for movement error and drift.
- Plan complex routes using the
  [Trajectory](./docs/17_the_trajectory_system.md)
  system. Follow splines, arcs, lines, equations, the little green light at
  the end of the tunnel - whatever you want, it's really up to you.
- Full-featured geometry package with polygon collision detection, several
  shapes, lines, points, and angles.
- Map gamepad events (button press, trigger press, joystick move, etc) to
  `Runnable`s, making it easy to create listeners.
- Incredibly customizable: much of Pathfinder is based on interfaces and
  there's a plugin system for advance usages.
- Potentially the coolest piece of code ever written. I don't have any
  evidence to support that, but we're all about swag here.

NEW DOCUMENTATION:

> Pathfinder has improved documentation available in the [`docs`](/docs/)
> directory.

OLD DOCUMENTATION:

> Want to get started? Check out the [releases guide](project_releases.md) to
> get everything installed, and then head over to the
> [documentation portal](https://wobblyyyy.github.io/docs/pathfinder2/documentation.html).

<h2 align="center">Summary</h2>

Pathfinder handles everything related to your robot's movement. It abstracts
away much of the tedious movement-related code many robot codebases are bogged
down with and allows you to focus on what else your robot can do. In addition
to providing a robust interface for precisely controlling your robot's
movement, Pathfinder provides a wide array of robotics-related utilities and
tools.

Want your robot to move to a certain point on the field? Simple:
```java
pathfinder.goTo(new PointXY(10, 15));
```

`PointXY` is a simple Cartesian coordinate with X and Y values. Now let's say
you want to make your robot move to the same point, but turn to 45 degrees.
```java
// method 1
pathfinder.goTo(new PointXYZ(10, 15, Angle.fromDeg(45)));

// method 2
pathfinder.goToZ(Angle.fromDeg(45));
```

Pathfinder is capable of significantly more than simple movement - splines,
motion profiling, recording and playback - the list goes on.

<h2 align="center">Example Code</h2>

Check out an example FRC project
[here](https://github.com/Wobblyyyy/MecanumPathfinderFRC)!

Here's a demonstration of Pathfinder's utilization of lambda expressions to
write simple and readable code.
```java
pathfinder
    .setSpeed(0.5)
    .setTolerance(2)
    .setAngleTolerance(Angle.fixedDeg(5))
    .splineTo(
        new PointXYZ(10, 10, Angle.fixedDeg(90)),
        new PointXYZ(20, 30, Angle.fixedDeg(45)),
        new PointXYZ(30, 50, Angle.fixedDeg(180))
    )
    .tickUntil();

pathfinder.getListenerManager()
    .bindButton(gampad::a, (isPressed) -> {
        // code to be executed whenever the A button is pressed
    })
    .bindButton(gamepad::b, (isPressed) -> {
        // code to be executed whenever the B button is pressed
    })
    .bind(new ListenerBuilder()
        .setMode(ListenerMode.CONDITION_IS_MET)
        .addInput(() -> SupplierFilter.anyTrue(
            () -> gamepad.rightTrigger() > 0,
            () -> gamepad.leftTrigger() > 0
        ))
        .setWhenTriggered(() -> {
            // code to be executed whenever either trigger is pressed
        })
        .setPriority(10)
        .setExpiration(Double.MAX_VALUE),
        .setMaximumExecutions(Double.MAX_VALUE)
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
- [Project releases](project_releases.md) - a guide on all of the available
  releases of Pathfinder.

<h2 align="center">Licensing</h2>

This project uses the [GNU GPL V3](license.md). You're allowed to freely
distribute and modify the code contained in this project, so long as a copy of
the original license is provided, and all of your code is made open-source.
