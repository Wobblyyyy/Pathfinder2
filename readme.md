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

<h1 align="center">Description</h1>

Pathfinder2 is a continuation of the
original [Pathfinder](https://github.com/Wobblyyyy/Pathfinder)
library - an autonomous motion planning and control system designed for FIRST
Robotics environments, such as
the [FIRST Robotics Competition](https://www.firstinspires.org/robotics/frc) and
the
[FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc). This library
will be actively developed until Q3 2022, and (hopefully) maintained long
afterwards. 

<h2 align="center">Important Notice</h2>

This library is still in active development. There's a lot of placeholders and a
lot of missing documentation. You will most certainly encounter one or two bugs.
Actually, maybe a little more than one or two.

As of now, Pathfinder aims to have a stable release as early into the FTC season
as possible. Pathfinder will have a release before the FRC season begins.

If you'd like to contribute, email
me ([wobblyyyy@gmail.com](mailto:wobblyyyy@gmail.com)) and I'll let you know
what you can help with. Some of the most important work you can do is testing
the software and reporting any issues or bugs you encounter - if you notice an
issue, if you notice some missing or confusing documentation, if you find some
code that's running really slowly - report the issue, and I'll try and fix it as
soon as I can.

<h2 align="center">Documentation</h2>

Please see the [Pathfinder2 Documentation Portal](https://google.com), or
continue scrolling to access the quickstart. The documentation portal should
contain just about everything you need to know, and it's probably the best place
to start if you're confused.

<h3 align="center">Generic Quickstart</h3>

Check out the file
named [`project_tutorial.md`](https://github.com/Wobblyyyy/Pathfinder2/blob/master/project_tutorial.md)
for a quick overview of Pathfinder's core principles.

<h3 align="center">FTC Quickstart and Example</h3>

We now have an example project! Check it
out [here](https://github.com/Wobblyyyy/Pathfinder2Ftc). You'll want to navigate
to the TeamCode folder (`org.firstinspires...`) and check out the classes there.
This may be useful if you're in FTC or FRC. There will be an FRC-specific
repository in the near future!

<h2 align="center">Quickstart</h2>

Tired of reading? Don't want to do much more if it? Well... I hate to be the one
to tell you this... but that sucks for you, now, doesn't it? Pathfinder is a
gigantic library, (not really gigantic, but cool words make the library sound
cooler - you know?) and, such, it has... quite the abundance of documentation.

<h3 align="center">From Scratch</h3>

Here's what you're *probably* looking for. Two complete guides on getting
started with Pathfinder. These guides are example implementations of the
contents of Pathfinder and are available in both text and video forms.

- [Pathfinder from Scratch - FTC Programming](https://google.com)
- [Pathfinder from Scratch - FRC Programming](https://google.com)

<h3 align="center">Text Guides</h3>

There's a ton of documentation available for Pathfinder in a text format. Videos
aren't exactly everyone's forte, you know what I'm saying?

- [Pathfinder - JavaDoc](https://google.com)
- [Pathfinder - Documentation Portal](https://google.com)

<h3 align="center">Video Guides</h3>

Alright. Videos. Everyone's favorite. Or not really. Anyways. These video guides
are complete tutorials that explain some of the more advanced concepts, as well
as covering full-fledged implementations of the library.

- [Pathfinder Video Guides](https://google.com)

<h2 align="center">Design Goals</h2>

Pathfinder's design is extensively documented [here](https://google.com). For
the purpose of accessibility, many of the design intentions will be reiterated
here.

- Create an expansive library capable of handling all motion-related tasks for
  FTC and FRC environments. 
- Simplify the use of advanced features such
  as [odometry](https://en.wikipedia.org/wiki/Odometry),
  [pathfinding](https://en.wikipedia.org/wiki/Pathfinding),
  and [autonomous navigation](https://inertialsense.com/autonomous-navigation-autonomous-robotics-101/). 

<h2 align="center">Licensing</h2>

I know, I know... nobody likes licenses or whatever. I'll make it quick. Any
file contained in this repository is a part of the "Pathfinder2" project, which
is licensed under the GNU General Public License V3. Any contributors grant the
Pathfinder2 project the right to any contributed code. You're allowed to use
this project commercially, privately, publicly - really, however you want, SO
LONG AS you provide the source code for your program and include a copy of the
GNU General Public License V3 in your repository.

I don't actually care all that much if you "steal" my code - I love the open
source community. This project is licensed the way it is so that any FTC or FRC
teams making use of it share their contributions and ideas with the rest of the
FTC and FRC community - that's the whole point of the library, really.

GitHub provides
a [handy little guide](https://github.com/Wobblyyyy/Pathfinder2/blob/master/license.md)
to the GNU GPL V3, and I'd suggest you give it a quick read if you're unfamiliar
with the terms and conditions of the license.

<h3 align="center">More Licensing Stuff</h3>

Pathfinder2 depends on several other open source projects, most (all?) of which
are licensed under their own terms.

- [OdometryCore](https://github.com/tmthecoder/OdometryCore), licensed under
  the [MIT License](https://opensource.org/licenses/MIT)