# Ticking Pathfinder
Ticking is key to Pathfinder's operation. My Adderall just wore off, but I
need to finish typing at least something here or I know I'll forget to later,
so I do apologize for the very low-quality documentation.

## What does ticking do?
The `Pathfinder` class (the primary interface for interacting with the whole of
Pathfinder contains a method with the name `tick()`. It returns the instance of
`Pathfinder` that it belongs to so that you can chain method calls.

Another word for "tick" would be "update": calling `tick()` will update all of
the subcomponents of Pathfinder. These components include, but are not limited
to:
- Executor manager (`Trajectory` system (follow trajectories, etc))
- Movement profiler
- Movement recording/playback manager
- Listeners
- Plugins
- Scheduler
- Zone processor
- `Runnable`s bound to `tick()`

You do not need to call `tick()` in order to use portions of the library, but
if you'd like to effectively make use of `Pathfinder`, you'll need to tick it.
In driver-controlled game modes, it's common to tick Pathfinder at either the
start or end of the loop. In autonomous modes that make use of a command system
similar in nature to that of _wpilib_, Pathfinder should have its own
subsystem with a `periodic()` method that ticks Pathfinder. In linear autonomous
modes, variants of `tickUntil()` are used frequently. `tickFor(double)` allows
you to tick Pathfinder for a certain amount of time (specified in milliseconds),
allowing you to create sequential autonomous programs.

It's strongly encouraged that you only use `tick()` from the robot's main
thread. Pathfinder is, in general, built for a single-threaded robot program.
There aren't any known issues with using multiple threads to control Pathfinder,
but the library was designed to be operated from a single thread. You're welcome
to use multiple threads if you'd like, but it has not been tested, and it's
quite likely you'll encounter some issues somewhere along the way.

## The normal `tick()` method
Internally, `tick()` uses `me.wobblyyyy.pathfinder2.TickProcessor` to
manipulate the subsystems of Pathfinder. Specifically, there are four
components to a single "tick."
- `runPreTick()`
- `runExecutorTick()`
- `runOnTick()`
- `runPostTick()`

The "tick" method is the base method for ticking Pathfinder, and every other
variant of it is based upon it. `tickUntil` and its derivatives, for example,
all call `tick()` in a loop.

## `tickUntil` and its derivatives
`tickUntil` ticks Pathfinder until a certain condition is met (or a certain
condition is not met). Some methods include parameters for:
- A timeout, in milliseconds
- A `Supplier<Boolean>` that indicates if Pathfinder should continue ticking.
  If the supplier returns `true`, the `tickUntil` method will continue
  execution. If it returns false, it will stop.
- A `Runnble` (or `Consumer<Pathfinder>`) that will be executed after every
  tick. The `Consumer<Pathfinder>` accepts the instance of Pathfinder that
  the method is being called from.

## Ticking Pathfinder for a specific amount of time
Even more placeholder text.

Actually, just kidding!
```java
Pathfinder pathfinder = null;

// tick Pathfinder for 1,000 milliseconds (1 second)
pathfinder.tickFor(1_000);
```
