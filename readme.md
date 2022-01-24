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
afterwards.

For a fun little video tangentially related to the rest of the readme,
see [this](https://www.reddit.com/r/FTC/comments/rdqitc/guys_we_got_auton_working/).
No, our robot does not do that anymore, and no, yours won't do that either -
but you gotta admit, our robot has got some moves.

<h2 align="center">Installation</h2>

Installation instructions have been moved [here](project_releases.md).

<h2 align="center">Example Code</h2>

Here's a small bit of code demonstrating how Pathfinder can be used to write
simple and easily understandable code. Pathfinder utilizes lambda expressions
to write expressive and intuitive code.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> gamepad.a(),
        (b) -> {
            pathfinder.clear();
            pathfinder.setTranslation(Translation.ZERO);
        }
    )
    .bindButton(
        gamepad::b,
        (b) -> pathfinder.goTo(new PointXYZ(10, 10, 10).tickUntil());
    );

pathfinder
    .setSpeed(0.5)
    .setTolerance(2)
    .setAngleTolerance(Angle.fixedDeg(5))
    .splineTo(
        new PointXYZ(10, 10, Angle.fixedDeg(90)),
        new PointXYZ(20, 30, Angle.fixedDeg(45)),
        new PointXYZ(30, 50, Angle.fixedDeg(180))
    )
    .andThen((pf) -> {
        // do some cool stuff after finishing the spline
    });
```

<h2 align="center">Documentation</h2>

Here's all of the sources of documentation I can think of right now.

- [Documentation portal](https://wobblyyyy.github.io/docs/pathfinder2/documentation.html) - 
  the hub for documentation for this library.
- [Project tutorial](.github/project_tutorial.md) - this is a basic tutorial
  that walks through the fundamentals of the library. This is probably the best
  place to start getting acquainted with the library.
- [General documentation](docs/docs.md) - this is a document containing a bunch
  of general documentation for the project. It's still being worked on and
  updated, but it contains a variety of information on different useful topics.
  This is probably a good place to start getting into the library.
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
