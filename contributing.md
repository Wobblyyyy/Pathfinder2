<p align="center">
<img src="media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<i>Autonomous motion planning and control library for wheeled mobile robots.</i>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

<h1>Contributing</h1>

<h2>Contributors</h2>

See [contributors.md](./contributors.md). Whenever you modify any code in the
Pathfinder project, be sure to properly credit yourself in the `contributors.md`
file. Or else.

<h2>Copyright</h2>

Any code you add or update in Pathfinder will be licensed to the Pathfinder
project. By contributing to Pathfinder, you agree that code you contributed to
the library is then the property of the Pathfinder project. If you modify any
Java source file, you can add yourself as an `author` by adding another author
tag, like so:
```java
/**
 * @author Colin Robertson
 * @author Example Author
 */
public class Example {

}
```

This is done to simplify any copyright-related issues and reduce the probability
of any copyright-related issues cropping up down the line.

<h2>What to work on</h2>

There are quite a few areas in Pathfinder that could benefit from getting a
bit of extra attention. Ordered by priority (high to low), those areas are
generally going to be:
- Bug fixes (fixing existing code)
- Documentation
  - Fixing (and/or improving) existing documentation
  - Adding new documentation
- Adding new features

<h3>Bug fixes</h3>

The best way to find bugs to fix is to either...
- Look at GitHub issues (if there are any)
- Message me on Discord: wobblyyyy#6733

If you find a bug and would like help, or if you'd like to contribute to the
project by fixing existing bugs, DM me on Discord and I'll be glad to work
something out with you!

<h3>Documentation</h3>

Documentation is one of the most important components of any large-scale
open-source project, and Pathfinder is no different. If you notice there's a
topic in the library that doesn't have very much documentation, please don't
hesitate to reach out!

<h4>Improving existing documentation</h4>

If you notice any issues with existing documentation, you're more than welcome
to go ahead and fix it! If you notice some existing documentation is missing
some information, you have two options:
- Add that information yourself
- If you don't know the information it's missing (because it's not documented)
  but feel it's important enough to be included in the documentation, message
  me, and I'll be sure to add it!

<h4>Adding new documentation</h4>

Check out the `docs` folder in the root directory of this repository to see
how documentation should generally be laid out. There's two things you need
to do when adding new documentation:
1. Create a new Markdown file with a name like `20_topic.md`, where `topic` is
   replaced with... well, the topic of the documentation...
2. Modify the `docs.md` file in the `docs` directory to include a link to the
   newly-created piece of documentation you added.

<h3>Adding new features</h3>

My rule for new features is this: if adding a new feature wouldn't harm any
other functionality, then go ahead. I have absolutely no objection to adding new
features to the library.

<h2>Before pushing code</h2>

<h3>Formatting</h3>

Before pushing any code to GitHub, run the `spotlessApply` Gradle task. You can
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
