<p align="center">
<img src="media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<i>Autonomous motion planning and control library for wheeled mobile robots.</i>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

<h1>Contributing</h1>

This guide will be improved later, but for now, I just need to make a couple of
notes so that I don't forget to later.

<h2>Before pushing code</h2>

<h3>Formatting</h3>

Before pusing any code to GitHub, run the `spotlessApply` Gradle task. You can
do that like so:

If you're on Linux...
```
./gradlew spotlessApply
```

If you're on Windows...
```
./gradlew.bat spotlessApply
```

Running the `spotlessApply` task requires you to have Node installed on your
system. This task must be run before pushing any code to GitHub because failing
to have properly formatted code will cause the `build` task to fail, which will
cause issues with _jitpack_ being unable to build and publish a release.

<h3>Tests</h3>

Please run the `test` Gradle task. ALL TESTS MUST BE PASSING. Any code that has
been added must have associated tests that go along with it.
