<p align="center">
<img src="media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<i>Autonomous motion planning and control library for wheeled mobile robots.</i>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

<h1>Pathfinder2 Releases</h1>

<h2>What should I download?</h2>

<h3>After October 27, 2021</h3>

Pathfinder2 releases are released in a single form - one JAR file that contains
all the libraries related to Pathfinder2. This was done to simplify
installation, and also partially because I'm really lazy.

Please see the [readme](readme.md) to learn more about how to install
Pathfinder2 without downloading a JAR.

<h3>Prior to October 27, 2021</h3>

Each of Pathfinder's releases will be released with several assets. As of
September 9th, 2021, those assets will be:

- Pathfinder2 Geometry
- Pathfinder2 Kinematics
- Pathfinder2 Core
- Pathfinder2 OdometryCore
- Pathfinder2 ALL

Generally, I'd advise you to download Pathfinder2's all-inclusive JAR. If you
really care about cutting down on size, you can download individual components.
At the very least, you'll need

- Pathfinder2 Geometry
- Pathfinder2 Kinematics
- Pathfinder2 Core

Core depends on Geometry and Kinematics.

<h2>Semantic versioning</h2>

The Pathfinder2 project uses semantic versioning - in short, each version has
three numbers.

- A MAJOR number
- A MINOR number
- A PATCH number

For example, take hypothetical version 6.7.0.

- The major version is 6.
- The minor version is 7.
- The patch version is 0.

Every time a small change is made - say, for example, a bug is corrected or an
algorithm is optimized - the patch version is incremented.

Every time an additive change - a change that adds functionality or features
without breaking any of the existing code - is made, the minor version is
incremented.

Every time a breaking change - a change that requires end users to have to
re-write some of their code for the library to still work - is made, the major
version is incremented.

<h2>Policy on releases</h2>

If a release is a "pre-release," it's not 100% confirmed to work. You can use
it, and it'll probably work, but it's suggested you stick to regular releases
unless you have a reason not to do so.

It's generally safe to update the version of Pathfinder2 you're using whenever a
new patch is released. In fact, I'd encourage it, quite strongly - getting the
latest bug fixes and optimizations is never a bad thing. It's also generally
safe to update Pathfinder2 whenever a new minor version is released - this isn't
as strongly encouraged as updating patch versions, because minor changes,
although beneficial, aren't required for the library to work as intended.

It is NOT safe to update the version of Pathfinder2 whenever a major release
comes out. Each major release will have a detailed set of notes explaining all
of the breaking changes that were made for the release. If any of the changes
affect your code, you will need to update your code in order to use the latest
and greatest version of Pathfinder2.

If you notice any issues with a release - issues, NOT changes - please report
them immediately. Bugs will be fixed as soon as possible and released in a new
patch. If you report an issue
(or submit a patch) you'll ensure everyone gets to experience a fully-functional
Pathfinder.
