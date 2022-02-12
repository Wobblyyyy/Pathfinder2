<p align="center">
<img src="media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<i>Autonomous motion planning and control library for wheeled mobile robots.</i>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

<h1>Pathfinder2 Releases</h1>

<h3>Table of Contents</h3>

- What should I download?
- Semantic versioning
- Policy on releases

<h2>What should I download?</h2>

Pathfinder has had several different guidelines on installation. Outdated
guidelines are preserved for posterity. These guidelines are sorted
chronologically, so that the most recent guideline (the one you should be
following if you're using the latest release of the library) is at the top,
and the oldest guideline (the one you definitely shouldn't be following) is all
the way at the bottom.

<h3>After February 11th, 2022 (v0.15.0 and onwards)</h3>
As of February 11th, 2022, Pathfinder will provide individual module artifacts
on `jitpack`.

Make sure to add `jitpack` as a dependency as so:
```
// this should go in the top level of your build.gradle file
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Then select the components you'd like to install. All artifacts have the
same group (`com.github.Wobblyyyy.Pathfinder2`).
```groovy
dependencies {
    implementation 'com.github.Wobblyyyy.Pathfinder2:geometry:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:kinematics:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:core:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:frc:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:OdometryCore:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:io:v0.15.0'
}
```
You should only select the modules you need. At the very least, you'll
need `geometry`, `kinematics`, and `core`, which would be implemented as
follows:
```groovy
dependencies {
    implementation 'com.github.Wobblyyyy.Pathfinder2:geometry:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:kinematics:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:core:v0.15.0'
}
```
If there's a newer version (newer than v0.15.0 that is) you should use the
latest version instead.

Here's an example of a full `build.gradle`.
```groovy
plugin {
    id 'java'
}

// you might have some other stuff here

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// or maybe even here

dependencies {
    implementation 'com.github.Wobblyyyy.Pathfinder2:geometry:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:kinematics:v0.15.0'
    implementation 'com.github.Wobblyyyy.Pathfinder2:core:v0.15.0'
}
```

<h3>After January 3rd, 2022 (v0.7.1 and onwards)</h3>
As of January 3rd, 2022, Pathfinder will no longer provide JAR binaries in
the Releases section. You can install Pathfinder via `jitpack`. Instructions
on doing that are as follows:

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
    implementation 'com.github.Wobblyyyy:Pathfinder2:v0.10.3'
}
```
For a complete example, see the `pathfinder2-examples` module's
[build.gradle](pathfinder2-examples/build.gradle) file.
If you're using maven, sbt, or leiningen, you can see installation instructions
[here](https://jitpack.io/#Wobblyyyy/Pathfinder2/v0.7.0).

<h3>After October 27, 2021, but prior to January 3rd, 2022 (v0.2.0-v0.7.0)</h3>

Pathfinder2 releases are released in a single form - one JAR file that contains
all the libraries related to Pathfinder2. This was done to simplify
installation, and also partially because I'm really lazy.

Please see the [readme](readme.md) to learn more about how to install
Pathfinder2 without downloading a JAR.

<h3>Prior to October 27, 2021 (v0.1.0)</h3>

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
