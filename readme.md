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

<h2 align="center">Installation</h2>

Installing Pathfinder shouldn't be TOO difficult. If you're using Gradle, it's
pretty simple. First, add the `jitpack` repository by adding the following to
your `build.gradle`.
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
Once you're done with that, add Pathfinder2 as a dependency:
```groovy
dependencies {
    implementation 'com.github.Wobblyyyy:Pathfinder2:v0.7.0'
}
```
For a complete example, see the `pathfinder2-examples` module's
[build.gradle](pathfinder2-examples/build.gradle) file.
If you're using maven, sbt, or leiningen, you can see installation instructions
[here](https://jitpack.io/#Wobblyyyy/Pathfinder2/v0.7.0).

<h2 align="center">Important Notice</h2>

This project is still in active development. You will almost certainly encounter
bugs, broken code, missing/poorly-written documentation, or any number of a host
of other issues. You're welcome (and encouraged) to use this project and help
find any issues with it, but I need to disclose that there's a decent chance
something will, at some point, go very wrong.

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
- [JavaDoc](https://google.com) - (not yet available) JavaDoc for the
  Pathfinder2 library. This is the most fine-grained documentation, and I'd
  encourage you to check this out once you're comfortable with the library.
- [Video guides](https://google.com) - (not yet available) video guides on using
  the library. Who doesn't love videos? Right? Right.

<h2 align="center">Licensing</h2>

This project uses the [GNU GPL V3](license.md). You're allowed to freely
distribute and modify the code contained in this project, so long as a copy of
the original license is provided, and all of your code is made open-source.
