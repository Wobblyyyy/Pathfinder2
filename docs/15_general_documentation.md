IF YOU WANT TO BROWSE THIS FILE MORE CONVENIENTLY, CHECK OUT THE
DOCUMENTATION PORTAL [HERE](https://wobblyyyy.github.io/docs/pathfinder2/documentation.md).
I would encourage you to do that, so you can take advantage of the table
of contents and search functionality, but maybe that's just me.

## A very quick tip
Sorry, I hate to interrupt. Anyways. If you're browsing this page in a web
browser, you're probably using the table of contents to navigate. If that's
the case, I'd like to let you know that after you click on a link and read
whatever it is you need to read, you can jump immediately back to the table
of contents if you hit the backspace key on your keyboard.

## Geometry
Geometry is one of the important concepts in Pathfinder, given the entire
library is literally based around geometry.

### Angles
The `Angle` class is a representation of... an angle. It's shocking, I know.
There's two important methods in the `Angle` class:
- `deg()` returns the angle's value, in degrees.
- `rad()` returns the angle's value, in radians.

Angles are incredibly useful (and used frequently) for geometry-related
operations. Your robot's heading/angle/direction/facing is an angle.
Your robot's heading/angle/direction/facing is dictated by a target angle
and a current angle.

### Points (PointXY and PointXYZ)
There's two types of points - `PointXY` and `PointXYZ`. `PointXYZ` is an
extension of `PointXY`.

#### PointXY
The simplest kind of point - also the basis for all of Pathfinder's geometry
system. `PointXY` has two main values:
- X
- Y

That's just about the simplest way to explain it - it's literally just a point.
There's a bunch of methods in the `PointXY` class - see the JavaDoc for
information on all of them, but the most important methods are listed below.

##### Method: distance
Get the distance between two points. This method uses the distance formula
to determine the distance between the two points.
```java
PointXY a = new PointXY(0, 0);
PointXY b = new PointXY(5, 0);
double distance = PointXY.distance(a, b); // distance = 5
```

```java
PointXY a = new PointXY(0, 0);
PointXY b = new PointXY(5, 5);

double distance = PointXY.distance(a, b); // 5 times sqrt(2) (roughly 7.07)
```

```java
PointXY a = new PointXY(-5, -5);
PointXY b = new PointXY(5, 5);

double distance = PointXY.distance(a, b); // 10 times sqrt(2) (roughly 14.14)
```

##### Method: inDirection
Create a new point a given distance away from a base point. There's two ways
to use this method:
```java
PointXY base = new PointXY(0, 0);
double distance = 7.07;
Angle direction = Angle.fromDeg(45);

// this method accepts three parameters:
// base:      the base point
// distance:  how far away the point should be
// direction: the direction the point should be created in
PointXY newPoint = PointXY.inDirection(base, distance, direction);

// the above point is (5, 5)
```

```java
PointXY base = new PointXY(-5, -5);
double distance = 14.14;
Angle direction = Angle.fromDeg(45);

PointXY newPoint = base.inDirection(distance, direction);

// the above point is (5, 5)
```

```java
PointXY base = new PointXY(0, 0);
double distance = 10;
Angle direction = Angle.fromDeg(90); // vertical/straight up

PointXY newPoint = base.inDirection(distance, direction);

// the above point is (0, 10)
```

### Translation
Translations are at the heart of Pathfinder's movement. The general idea is
that any chassis should be able to receive a translation and move accordingly.
Translations received by the robot will always be relative - a translation that
means "go forwards" will make the robot "go forwards," relative to the robot
itself.

#### Values for a translation
There are three values for a translation:
- vx (x displacement)
- vy (y displacement)
- vz (z displacement)

`vx` and `vy` affect how the robot moves along the X and Y axes. `vz` controls
how the robot rotates - a positive `vz` value should make the robot rotate
around it's center, and a negative `vz` value should make the robot rotate
around it's center in the other direction.

In most (almost all) cases, translations will have `vx` an `vy` values less
than 1.0. Typically, the value `sqrt((vx^2)+(vy^2))` will be less than or
equal to 1.0. Likewise, `vz` will almost always be less than or equal to 1.0.

#### Absolute translations vs relative translations
I don't know how to put this in a way that makes a lot of sense. Basically,
an absolute translation always has the same `vx` and `vy` values, regardless
of which direction the robot is facing. A relative translation, on the other
hand, can have different meanings for `vx` and `vy` values based on the
direction the robot is facing.

Let's say your robot is facing 0 degrees. If you give it a translation of
`(vx: 1, vy: 0, vz: 0)`, the robot will move towards positive X. 0 is the
default facing of the robot, so any absolute translation will be exactly
the same as a relative translation while the robot is facing 0. If the robot
was to be rotated by 90 degrees (meaning it's facing either 90 or -90 degrees),
the translation would be more like `(vx: 0, vy: 1, vz: 0)`. This is because
the robot is moving in the same ABSOLUTE direction. The robot is NOT moving
in the same RELATIVE direction, because it's facing a different direction.
It's still moving the same way it would if it was facing 0 degrees, but the
translation the robot receives is different.

#### Converting an absolute translation to a relative translation
The `Translation` class provides an `absoluteToRelative(Translation, Angle)`
method that converts absolute translations to relative translations.
```java
public static Translation absoluteToRelative(Translation translation,
                                             Angle heading) {
}
```

## Calibration
Calibration is a topic so important it deserves its very own page! Check
it out [right here](calibration.md).

## Operating Pathfinder
Pathfinder's operation is designed to be as simple as possible, while still
allowing advanced users to have the highest degree of control over their
robot's movement. There are some key concepts you'll need to get the hang of
in order to operate the library, but after you do, it should be easy going.

### Ticking Pathfinder
This is absolutely crucial to operating the library - you need to "tick" it.
Ticking is the process of updating the robot's translation based on its
current position and target position (more specifically, the trajectory the
robot is currently following). The `Pathfinder` class provides a `tick()`
method that does exactly this. If you're using the library in a loop, you
should run this `tick()` method once per loop cycle.

#### Ticking Pathfinder in a loop
Say you're using the library during tele-op or something similar. You want to
call the `tick()` method once per loop update, as follows.
```java
while (opModeIsActive()) {
    pathfinder.tick();
}
```

#### Ticking Pathfinder outside a loop
Say you're using the library during autonomous. You could simply do
something like:
```java
while (pathfinder.isActive()) {
    pathfinder.tick();
}
```

You could also do something like this:
```java
pathfinder.tickUntil();
```

This makes it so that you don't have to implement your own loop, which
generally makes code a bit cleaner. There are A LOT of overloads for the
tickUntil method. Likewise, there's a method called "andThen", which is
the same as tickUntil, except it has a `Consumer` that will be executed
once the tickUntil method has finished.

#### An example of method chaining
Method chaining is beautiful - who doesn't love method chaining? Method
chaining is mostly personal preference - there's no real advantage or
disadvantage to using it or not using it. Most of Pathfinder's API-like
classes have chainable methods by default.
```java
public class ExampleMethodChaining() {
    private static final PointXYZ TARGET_A = ...;
    private static final PointXYZ TARGET_B = ...;
    private static final PointXYZ TARGET_C = ...;
    private static final PointXYZ TARGET_D = ...;

    private void doSomething() {

    }

    private boolean shouldRun() {
    return true;
    }

    @SuppressWarnings("CodeBlock2Expr")
    public void example() {
    pathfinder.goTo(TARGET_A)
            .tickUntil() // will tick Pathfinder until the path finishes
            // executing, regardless of how long it takes
            .goTo(TARGET_B)
            .tickUntil(4_000) // will tick Pathfinder until either (a) the
            // path finishes, or (b) the elapsed time is
            // greater than or equal to 4 seconds
            .goTo(TARGET_C)
            .andThen((pathfinder -> doSomething()))
            .goTo(TARGET_D)
            .tickUntil(4_000, this::shouldRun, (pathfinder, elapsedMs) -> {
              // this has a timeout of 4 seconds
              // if the shouldRun supplier returns false, this method
              // will finish executing immediately
              // this consumer will be called once per tick and will be
              // provided the current instance of Pathfinder, as well
              // as the elapsed time (in milliseconds)
              PointXYZ currentPosition = pathfinder.getPosition();

              // print the current position and the elapsed time
              System.out.printf(
                      "Current position: %s%n" +
                              "Elapsed time: %sms%n",
                      currentPosition,
                      elapsedMs
              );
            });
    }
}
```

### Manually controlling Pathfinder's movement
The `Pathfinder` class has several methods for manually controlling the motion
of the robot.

#### Setting the robot's translation
`setTranslation(Translation)` will set the robot's translation.
```java
public class ExampleSetTranslation {
    public void example() {
        Pathfinder pathfinder = new Pathfinder(...);
        Translation translation = new Translation(0.5, 0.5, 0);
        pathfinder.setTranslation(translation);
    }
}
```

### Controlling the robot in...
Here are some quick tips on controlling the robot in different modes.

#### Autonomous
If the robot is in an autonomous period, it's strongly encouraged you make
use of trajectories and followers. You can absolutely control your robot
however you'd like, but I would strongly encourage you to make use of
trajectories, as they greatly simplify your autonomous code and allow you to
do a lot more with your autonomous.

#### Tele-op
Whenever the robot is operating in tele-op mode, you'll (probably) want the
robot to respond to driver input. This can be accomplished with the previously
mentioned `setTranslation(Translation)` method.
```java
public void runTeleOp() {
    Pathfinder pathfinder = new Pathfinder(...);

    while (true) {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double z = gamepad1.right_stick_x;

        Translation translation = new Translation(x, y, z);

        pathfinder.setTranslation(translation);
    }
}
```

If you're using the `tick()` method during tele-op, the translation will be
automatically changed whenever the `tick()` method is called. I'd suggest you
either use one or the other at a time - if you're using `setTranslation`, you
shouldn't be using `tick`, and vice versa.

### Stopping and pausing
I'm sure at some point, you'll need to stop your robot. I'm going to quickly
define some terms, just so there's no confusion later on.
- Pathfinder's MOVEMENT is your robot's physical movement. If Pathfinder is
  still moving... well, your robot is still moving.
- Pathfinder's EXECUTION is managed with the `tick()` method. Execution
  controls the robot, but it does not directly impact movement - there's only
  a (very strong) correlation.

#### Stopping Pathfinder's execution
Pathfinder's execution and movement are NOT linked, so it's possible to cancel
ONLY Pathfinder's execution or ONLY Pathfinder's movement. If your robot is
moving when you use the `clear()` method, it'll continue moving after the
robot's translation has been manually set.

`Pathfinder` provides a method, `clear()`, that can be used to stop the
execution of the library. This will clear the queue of `Follower` instances,
which will transitively clear any queued `Trajectory` instances.
```java
Pathfinder pathfinder = new Pathfinder(...);
pathfinder.clear();
```

Note that stopping execution will NOT stop the movement of the robot.

#### Stopping the robot (stopping movement)
Physically stopping the robot is an incredibly common task that I'm sure you
will, at some point, need to do. To physically stop the robot, set the
translation to a translation with X, Y, and Z values of 0.
```java
Pathfinder pathfinder = new Pathfinder(...);
pathfinder.setTranslation(new Translation(0, 0, 0));
```

If you stop the robot's movement WITHOUT also stopping Pathfinder's execution,
your robot will begin moving again as soon as the `tick()` method is called.
In order to completely stop the robot, you need to clear BOTH the executors
and the translation.

##### Stopping execution and movement
Surprisingly enough, it's exactly what you'd expect.
```java
Pathfinder pathfinder = new Pathfinder(...);

// stop the execution
pathfinder.clear();

// stop the movement
pathfinder.setTranslation(new Translation(0, 0, 0));
```

#### Pausing
There's no officially supported way to pause Pathfinder temporarily. For now,
you can just stop calling the `tick()` method for as long as you'd like to
pause Pathfinder. This will work perfectly fine for anything that does not
have elapsed time as a parameter.
```java
public void run() {
    Pathfinder pathfinder = new Pathfinder(...);
    boolean isPaused = false;
    while (true) {
        if (isPaused) continue;

        pathfinder.tick();

        // other code...
    }
}
```

### Robot
A `Robot` is composed of two elements - a `Drive` and an `Odometry`.

#### Robot: drive
The `Drive` interface is responsible for physically driving a robot around
on a field. You need to have an implementation of the drive class to actually
operate Pathfinder. There are a couple of prebuilt drive implementations you
can use, if you so desire:
- `me.wobblyyyy.pathfinder2.drive.MeccanumDrive`
- `me.wobblyyyy.pathfinder2.drive.SwerveDrive`

If you'd prefer to use your own implementation of the `Drive` interface, you
simply have to implement a couple of methods - go see the JavaDoc for the
drive interface for more information. Actually, you can just look right here.
There's only a total of four methods you need to implement.

##### Methods from `me.wobblyyyy.pathfinder2.robot.Drive`
- `getTranslation()` - return the last drivetrain that was set to the robot.
- `setTranslation(Translation)` - set a translation to the robot.

##### Methods from `me.wobblyyyy.pathfinder2.robot.modifiers.Modifiable`
- `getModifier()` - return the modifier.
- `setModifier(Function<E, E>)` - set the object's modifier.

##### The `AbstractDrive` class
If you're going to implement your own `Drive`, I'd encourage you to use the
`AbstractDrive` class (`me.wobblyyyy.pathfinder2.robot.AbstractDrive`). It
doesn't do much, but it removes the need to implement methods from the
`Modifiable` interface.

##### Using the `Drive` interface
It's pretty simple, to be honest. There are two main ways to use translations.
It's worth noting that the `Drive` interface assumes that if your robot is
given a translation, it'll move according to that translation, relative to
the robot's current position. In other words - the `Drive` interface accepts
RELATIVE translations - NOT ABSOLUTE translations.

Let's say your robot is at (0, 0) and is facing straight forwards. If you
tell your robot to move right (a translation of (1, 0, 0)), it should move
right - the robot's X coordinate should increase. Now let's say your robot
is facing NOT forwards - maybe a 90-degree rotation, for example. If you
give your robot the same translation ((1, 0, 0)) the robot's X coordinate
will DECREASE and become negative.

The `Translation` class (`me.wobblyyyy.pathfinder2.geometry.Translation`)
has a method `toRelative(Angle)` that accepts an `Angle` parameter representing
the robot's current heading. This converts an absolute translation to a
relative translation.

```java
// let's say you want to move the robot forwards, relative to the robot
Drive drive = ...; // assume this is actually implemented

Translation translation = new Translation(0, 1, 0);

drive.setTranslation(translation);
```

```java
// let's say you want to move the robot forwards, relative to the field
Drive drive = ...; // assume this is actually implemented

// assume 'robot' is declared
// assume 'robot' has a method 'getPos' that returns a PointXYZ - the robot's position
Translation translation = new Translation(0, 1, 0).toRelative(robot.getPos().z());
```

#### Robot: odometry
The `Odometry` interface is responsible for reporting information on the
robot's position on the field. Like the drive interface, you're required
to have an implementation of this. Also like the drive interface, there
are some prebuilt implementations you can feel free to use.

There are A LOT of methods you need to implement for the odometry interface.
I would STRONGLY encourage you to use the `AbstractOdometry` class instead
of the `Odometry` interface: `me.wobblyyyy.pathfinder2.robot.AbstractOdometry`.

The `Odometry` interface is incredibly simple - it should report the robot's
position on the field. That's it. This position should be absolute.

##### Methods from `me.wobblyyyy.pathfinder2.robot.Odometry`
There's a lot of methods in the Odometry interface, to be honest. I'm not
going to list them all here, because that would take way too much time, but
the main ones you need to know are:
- `getPosition()` - get the robot's current position

Yep. That's it. There's not much to it, really.

##### The `AbstractOdometry` class
Please, for your own good, make use of the `AbstractOdometry` abstract class
instead of implementing the entire `Odometry` interface yourself. You only
need to write one method - `PointXYZ getRawPosition()`, which should return...
well, it should return the robot's raw position.

##### Using the `Odometry` interface
There's not really all that much you can do with it.

###### Why should offsets be managed with odometry?
If offsets are managed exclusively by odometry, it's significantly less likely
you'll encounter a hard-to-find bug. Because Pathfinder is designed to be a
suite of movement-related tools, you can handle all of your odometry offsetting
needs with built-in Pathfinder utilities.

###### Modify the robot's position
Refer to the following methods to modify the robot's position. It's suggested
that you only modify the robot's position with the `Odometry` interface's
methods, so you can eliminate as many potential sources of issues as possible.
```java
public interface Odometry {
    // ...

    void setOffset(PointXYZ offset);
    void offsetBy(PointXYZ offset);
    void removeOffset();
    void offsetSoPositionIs(PointXYZ targetPosition);
    void zeroOdometry();

    // ...
}
```

### Trajectories
Trajectories are the basis for Pathfinder's movement. Well, technically
speaking, `Follower`s actually control your robot's movement, but instances
of the `Trajectory` interface dictate how your robot moves.

A trajectory instructs your robot on how to move around the field. They
can be customized to modify how the robot moves. There are a variety of types
of trajectories, but they all do the same thing - tell your robot where to go.

#### What's a `Follower`?
You might see the term `Follower` mentioned in Pathfinder's documentation (or
source code) at some point. A `Follower` is an internal class used to actually
follow trajectories. You may have also seen `GenericFollowerGenerator`, the
de facto `FollowerGenerator`, responsible for creating `Follower` instances
that follow `Trajectory` instances. Putting that in writing makes it sound
way more complicated than it actually is, but just know that `Follower` is used
exclusively internally by Pathfinder. You can create your own implementations
of `Follower` and `FollowerGenerator` because this library is fairly modular,
but there's not much of a reason to.

##### Does my follower matter?
Not really, no. The `GenericFollower` should work for almost all use cases.
I can't think of a situation where a `GenericFollower` would not suffice.

#### Linear trajectory
The most simple kind of trajectory is the [linear trajectory](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java).
It can be (and is) described as follows:

> The most simple type of trajectory. A linear trajectory does nothing other
> than go to a point at a linear speed. Such, there's not much you can
> customize here. But it's simple, and it works. Hopefully, that is.

Linear trajectories are the logical starting point if you've never used
a `Trajectory` before. Although simple, linear trajectories aren't
particularly fast: a well-optimized spline trajectory will almost always
be more effective, albeit more complex.

##### Creating a linear trajectory
The `LinearTrajectory` class has a single constructor, which accepts the
following parameters:

- Target point - the target point (a `PointXYZ`) is the trajectory's target.
  In other words, it's where you want the robot to go.
- Speed - the speed at which the robot should move. This value must be
  greater than 0 and less than or equal to 1. A speed value of 1 will make
  the robot move as fast as it can, and a speed value of 0.1 will be... pretty
  slow.
- Tolerance - the tolerance Pathfinder uses in determining if it's finished
  following the trajectory. This value should be determined experimentally.
  Higher tolerance values make your robot's movement less accurate, while
  lower tolerance values increase accuracy, but can sometimes cause issues
  with your robot circling around a point.
- Angle tolerance - just like tolerance, but for the robot's heading. This
  should be an `Angle`.

#### Fast trajectory
A fast trajectory is a linear trajectory, but it's less precise. The purpose
of a fast trajectory is documented in the file - check it out right
[here](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/FastTrajectory.java).

Fast trajectories save speed by not requiring your robot to meet certain
tolerance values when it determines if it has or has not completed a follower.
This makes the trajectory less accurate, but can save a good amount of time,
as your robot won't be required to adjust itself, which can take quite a
while to do, and sometimes cause your robot to circle around a point forever.

#### Timed trajectory
A timed trajectory is unlike any of the other forms of trajectory - it operates
based exclusively on elapsed time.
```java
/**
 * Create a new {@code TimedTrajectory}.
 *
 * @param translation    the translation the robot should follow. This
 *                       translation will have vx and vy values of how far
 *                       the robot should move in those respective directions
 *                       (remember, always relative to the robot). The vz
 *                       value of this translation will be how fast the
 *                       robot will turn, in radians.
 * @param timeoutMs      how long the trajectory should last. This time
 *                       is measured in milliseconds. The trajectory is
 *                       considered finished after the elapsed time (ms) is
 *                       greater than this value.
 * @param speed          how fast the robot should move (should usually be
 *                       a value within 0.0 to 1.0)
 * @param turnMultiplier the value that all vz values will be multiplied
 *                       by. Having a higher turn multiplier means your
 *                       robot will attempt to turn more quickly, while
 *                       having a lower turn multiplier means your robot
 *                       will attempt to turn more slowly.
 */
public TimedTrajectory(Translation translation,
                       double timeoutMs,
                       double speed,
                       double turnMultiplier) {
        this.translation = translation;
        this.timeoutMs = timeoutMs;
        this.speed = speed;
        this.turnMultiplier = turnMultiplier;
}
```

#### Spline trajectories
Splines are among the coolest things to ever grace this beautiful planet.
In short, a spline is basically a curvy line. Splines are generally created
with a series of control points (points that the line MUST pass through),
and interpolation handles everything in between.

Splines are popular for trajectories because they allow you to move your robot
quickly, utilizing the curve to cut time. You can also make a trajectory
speed up or slow down or just about anything else, except not actually anything
else.

##### Rules for control points
Splines must adhere to a certain set of control points, or the spline won't
be able to be interpolated. Those conditions are as follows:
- The X values must move in the same direction. If the X values are decreasing,
  all of them must decrease. If the X values are increasing, all of the values
  must then increase.
- Y values must be monotonic. This is similiar to the restrictions placed on
  X values - they must all move in the same direction. Y can either increase
  or decrease throughout the spline, but it PROBABLY can't do both.
- Each X value should be unique.

If your spline doesn't follow these rules, there may be some issues with your
spline, some of which can be challenging to debug. I tried to make the spline
constructor provide useful exceptions, but no promises.

##### How splines work
Here's the answer: spline interpolation. Basically, you input an X value and
get out a Y value. It's similiar to a linear equation, or any equation, for
that matter.

##### When to use a spline
Splines have quite a few use cases.
- Making a robot move in a curvy pattern. Splines allow you to make your robot
  move along a non-linear path. Multiple splines can be combined to create an
  even more complex path.
- Dynamically varying the speed or tolerance of a follower. Say you want a
  follower that accelerates for the first half of the path and then remains
  at a constant velocity for the rest. You could use a spline to dynamically
  interpolate speed values, allowing for precise control over acceleration.
- Making a non-linear controller.

##### When to NOT use a spline
Splines can be overused quite easily, so I'd suggest that you avoid using
splines whenever possible. By "possible" I mean whenever it doesn't harm
you in any way - if using a spline would be more effective, but would require
more work, I'd encourage you to put in the extra work.

##### What's a step value?
You'll see the term `step` used quite often when dealing with splines. In
order to properly explain what it is, you'll need a bit of background info
on how Pathfinder processes splines.

A `Spline` is basically just an equation. You can input an X value and get
a Y value as a result. This does two things - firstly, it means X values
can't go positive AND negative - they can only go positive OR negative.
Secondly, it means that you'll always need to supply an X value in order
to calculate a Y value.

If you used the robot's current position as that X value, then Pathfinder's
target position would be exactly the same as its current position, so it
would not move at all.

In order to circumvent this problem, there's a `step` values. This value is
added to your robot's current X position in order to calculate a new target.

###### Positive or negative?
It's pretty simple, actually. If your robot is moving in a positive X direction
(meaning X values are increasing), then you'll want a positive step value. If
your robot is moving in a negative X direction (meaning X values are
decreasing), then you'll want to use a negative step value.

###### Determining a good step value
Like everything else, it's just trial and error. A value somewhere around 0.5
seems fairly good, right? Yeah. Looks fine to me. If you're reading this and
you have a better idea for what a default step value is, please let me know
(or just update this yourself).

###### Issues with step values
The most common issue you will encounter with splines is using an invalid
step value. Well, that might not actually be the most common, but it sounds
cooler if I put it like that. If you have a negative value when it should
be positive (or a positive value when it should be negative), your robot will
never move along the spline. Pathfinder won't throw any exceptions if this is
the case, so it can be challenging to debug. Make sure your step value is
approaching the same infinity as the rest of your points.

##### Creating splines with a factory (suggested)
This is the easiest (and suggested) method of creating spline trajectories.
```java
SplineBuilderFactory factory = new SplineBuilderFactory()
    .setSpeed(0.5)
    .setStep(0.1)
    .setTolerance(2)
    .setAngleTolerance(Angle.fromDeg(5));

Trajectory trajectory3 = factory.builder()
    .add(0, 60, Angle.fromDeg(0))
    .add(new PointXYZ(20, 60, 0))
    .add(new PointXYZ(30, 60, 0))
    .add(new PointXYZ(40, 70, 0))
    .build();
Trajectory trajectory4 = factory.builder()
    .add(new PointXYZ(40, 70, 0))
    .add(new PointXYZ(30, 60, 0))
    .add(new PointXYZ(20, 60, 0))
    .add(0, 60, Angle.fromDeg(0))
    .build();
```

##### Creating a spline trajectory
It's encouraged that you use a different method of creating splines, because
this can make your code somewhat confusing. The JavaDoc for the constructor
of the `AdvancedSplineTrajectory` class is included below.
```java
/**
 * Create a new {@code AdvancedSplineTrajectory}.
 *
 * @param spline         a spline responsible for controlling the target point
 *                       of the trajectory. This target point should be updated
 *                       dynamically so that the robot is constantly given
 *                       a new marker/target point.
 * @param angleSpline    a spline responsible for controlling the angle
 *                       target of the trajectory. Because splines only work
 *                       with X and Y values, this has to be separate from
 *                       the original spline.
 * @param speedSpline    a spline responsible for controlling the speed of
 *                       the robot. This allows your robot to accelerate
 *                       and decelerate with relative ease. If you'd
 *                       like to have your robot move at a consistent
 *                       speed, you can use a {@code ZeroSlopeSpline},
 *                       which makes the spline return the same value,
 *                       no matter what input is provided.
 * @param step           how large each "step" value should be. A larger
 *                       step value makes the trajectory slightly less
 *                       accurate, but makes it have coarser movement. A
 *                       smaller step makes the trajectory more accurate, but
 *                       might be hard to work with at high velocities.
 *                       If your spline is moving in a positive X
 *                       direction, this value should also be positive.
 *                       Likewise, if your spline is moving in a negative
 *                       X direction, this value should also be negative.
 *                       Having a positive step with a negative spline
 *                       (or vice versa) will cause your robot to never
 *                       complete the trajectory, because it'll try to go
 *                       to the wrong target point.
 * @param tolerance      the tolerance used in determining if the robot is
 *                       actually at the target point. This tolerance
 *                       only affects the LAST of the points in the
 *                       trajectory - all the other points ignore
 *                       whatever this value is.
 * @param angleTolerance the tolerance used for determining if the robot
 *                       is facing the correct direction. Like the
 *                       {@code tolerance} parameter, this only affects
 *                       the LAST of the points in the trajectory.
 */
```

###### Creating a speed spline with a constant value
What if you want to have a spline that doesn't change speed at all?
Well. I would say "that sucks," but luckily for you, there's this!
*It's encouraged that you use a `ZeroSlopeSpline` instead of a more complex
type of spline, such as a `MonotoneCubicSpline`.*
```java
// introducing the ZeroSlopeSpline!
// me.wobblyyyy.pathfinder2.math.ZeroSlopeSpline
Spline spline = new ZeroSlopeSpline(0.5);
```

No matter where you are on the spline, it'll always return 0.5.

##### Creating a speed spline with a linear equation
You'll need to make use of the `LinearEquation` class, but it shouldn't be
all that difficult. Hopefully.
```java
// me.wobblyyyy.pathfinder2.math.LinearSpline
LinearEquation equation = new PointSlope(new PointXY(0, 0), 0.5);
Spline linearSpline = new LinearSpline(equation);
```

You'll always get a value dictated by a `LinearEquation` - quite lovely.

##### Creating splines with a builder
This is preferable to using the constructor to create splines, but it's
still not as good as using a factory. Anyways.
```java
Trajectory trajectory1 = new AdvancedSplineTrajectoryBuilder()
        .setSpeed(0.5)
        .setStep(0.1)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5))
        .add(new PointXYZ(0, 0, 0))
        .add(new PointXYZ(4, 6, 0))
        .add(new PointXYZ(6, 12, 0))
        .add(new PointXYZ(8, 24, 0)).build();
```

##### Types of splines
What if I told you there's more than one type of spline? Well, there is.
Shocking, I know. Anyways. A spline, as a mathematical concept, is just
an equation that interpolates a path between control points. Thus, Pathfinder
can use splines for just about everything that can vary throughout execution.

### Listeners
Listeners allow you to "listen" for certain conditions, making it easy to
write event-driven code. Listeners need to be ticked, which happens when
Pathfinder's `tick()` method calls the `tick()` method of Pathfinder's
`ListenerManager`, which in turn calls the `tick()` method of all of the
associated listeners.

Listeners function by repeatedly checking to see if a certain condition has
been met. In a robotics environment, your robot's physical actions are
separated from your code, so listeners work magically - you simply plop one
down and you're good to go. In an environment where fields/variables must be
changed manually via code, listeners shouldn't be used, as they can
overcomplicate code.

#### Ticking listeners
Listeners must be ticked in order to function properly. It's strongly suggested
that you make use of the `ListenerManager` class, as it makes managing
listeners significantly easier. `Pathfinder` has a method called
`getListenerManager()` which returns the `ListenerManager` that that instance
of Pathfinder is using.

##### If you used a listener manager
If you register a listener by using the `pathfinder#getListenerManager()`'s
`bind`, your listener will automataically be updated whenever Pathfinder's
`tick()` method is called.

##### If you did not use a listener manager
If you did not use a listener manager, you'll have to figure out how to
tick your listeners on your own. I promise, it's not too hard - you just
have to use the `tick` method, and you'll be all good!

#### Using bindings
The `listening` package of Pathfinder provides many utilities designed to
simplify writing code for a robot. These utilities are customized to my
preferences and using them may not be appropriate if a different solution
is preferable.

##### Listener manager
First, it's important to understand HOW the listener manager works. It's
not all that difficult, to be honest. This example is going to assume
a robotics context:
- Bind the listener (say we want to make dpad up do something)
- Tick Pathfinder as normal

See? Not too bad. Each of the listeners in the listener manager is added
to a collection. That collection is polled/ticked/updated every time Pathfinder
is ticked.

**IF YOU DON'T TICK PATHFINDER, LISTENERS WILL NOT WORK.**

##### Binding buttons
Buttons are a critical part of user input.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // bind imaginary controls to an imaginary A button
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                aButton::isPressed,
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // code to be run whenever the A button has been
                    // pressed
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                aButton::isPressed,
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // code to be run whenever the A button has been
                    // released
                }
            );

        // tick pathfinder forever and ever...
        //                     ... and ever...
        //                     ... and ever...
        //                     ... and ever...
        //                     ... and ever.
        while (true)
            pathfinder.tick();
    }
}
```

##### Binding buttons (but easier)
This is only sightly easier than the previous approach, but who doesn't
love writing clean code? Exactly.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                aButton::isPressed,
                () -> {}
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                aButton::isPressed,
                () -> {}
            );

        while (true)
            pathfinder.tick();
    }
}
```

##### Binding arbitrary objects
You can also bind arbitrary objects, allowing Pathfinder to handle just
about any event-based functionality you want. The "bind" method of the
`ListenerManager` class (accessible via `Pathfinder#getListenerManager()`)
is a generic method with type parameter T, representing the type of object
that's being listened to. Conveniently enough, Java's lambda syntax makes
it very easy to create these bindings.

###### Supplier
This `Supplier` of type T should accept input for the binding. This input
can be anything at all. This is frequently a `Supplier<Boolean>`, as it
allows you to bind something to a button. For example, here's a basic
binding attached to a button.
```java
public class Example {
    /**
     * this method is meant to emulate a method that gets the state of
     * a button. for the purpose of demonstration, assume this method
     * returns whether or not a physical button (the A button in this case)
     * is pressed.
     *
     * @return the button's current state.
     */
    public boolean aButton() {
        return true;
    }

    public void bindButton() {
        // print a message whenever a button is pressed
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                this::aButton,
                (isPressed) -> isPressed,
                (isPressed) -> System.out.println("A button has been pressed!");
            );
    }
}
```

###### Predicate
How is it determined if the condition is met or not? A `Predicate` is used.
This is the same type as the `Supplier`. Every time the listener is ticked,
this predicate will be tested (using the `Supplier`) for input. If the
predicate returns true, the condition is considered to have been met. If
the predicate returns false, the condition is considered to have not been met.

###### Predicate with a boolean
Because a boolean is already a predicate in itself, you simply have to return
the value of the boolean. All this predicate does is return the input value:
because the input value is a boolean, and `Predicate`s must return booleans,
we're all good!
```java
public class Example {
    private final Predicate<Boolean> predicate = (bool) -> bool;
}
```

###### Predicate with an arbitrary object
With an arbitrary object, you can have any condition you want.
```java
public class Example {
    private final Predicate<PointXYZ> predicate = (point) -> point.x() > 10;
}
```

###### Consumer
When the condition is met and the listener mode is active (for the "NEWLY
MET", this happens the first time the `Predicate` returns true and will not
happen again until the `Predicate` returns false then true once again), this
`Consumer` accepts an input of type T as a parameter. This is the input value
that caused the condition to be true.

###### Code example
Here's a complete code example.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    // whenever x or y individually exceeds 500, print out
    // "x value has exceeded 500!" or "y value has exceeded 500!"
    // if both the x and y values exceed 500, print out
    // "x AND y values have exceeded 500!"
    // if only x or only y exceeds 500, print out
    // "only x exceeds 500!" or "only y exceeds 500!"
    public void bindControlsAndRun() {
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),      // Supplier<PointXYZ>
                (position) -> position.x() > 500,    // Predicate<PointXYZ>
                (position) -> System.out.println("x value has exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),      // Supplier<PointXYZ>
                (position) -> position.y() > 500,    // Predicate<PointXYZ>
                (position) -> System.out.println("y value has exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),
                (position) -> position.x() > 500 && position.y() > 500,
                (position) -> System.out.println("x AND y values have exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // note that this is some
                    () -> position.x() > 500,          // pretty terrible
                    () -> position.y() > 500           // code - suppliers are
                ),                                     // not appropriate here,
                (bool) -> bool,                        // but this is only a demonstration, so who cares?
                (bool) -> System.out.println("only x exceeds 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse(
                    () -> position.y() > 500, // must be true
                    () -> position.x() > 500  // must be false
                ),
                (bool) -> bool,
                (bool) -> System.out.println("only y exceeds 500!")
            );

        while (true)
            pathfinder.tick();
    }
}
```

##### Binding joysticks
How else can you drive the robot? Exactly. You'll most likely need to bind
joysticks to drive your robot during tele-op.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // example joystick values for demonstration purposes
        double forwardsPower = 0.0;
        double sidewaysPower = 0.0;
        double turnPower = 0.0;

        pathfinder
            .bind(
                ListenerMode.CONDITION_IS_MET,
                () -> true,                    // true, so it's always executed
                (isPressed) -> true,           // true, so it's always executed
                (isPressed) -> {
                    pathfinder.setTranslation(new Translation(
                        forwardsPower,
                        sidewaysPower,
                        turnPower
                    ));
                }
            );

        while (true)
            pathfinder.tick();
    }
}
```

##### Binding a speed modifier
When one gear isn't cool enough... This sample builds upon the previous
sample on binding joysticks. Pressing the right trigger sets the "speed
multiplier" to 1.0, making the robot move as fast as it can. Pressing the
left trigger sets the multiplier to 0.25, making the robot significantly
slower. If neither trigger is pressed, the multiplier will be set to 0.5,
making the robot move at its normal speed.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.geometry.Translation;

import java.util.atomic.AtomicReference;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // example joystick values for demonstration purposes
        // range: -1.0 to 1.0
        double forwardsPower = 0.0;
        double sidewaysPower = 0.0;
        double turnPower = 0.0;

        // example trigger values for demonstration purposes
        // range: 0.0 to 1.0
        double rightTrigger = 0.0;
        double leftTrigger = 0.0;

        // needs to be an atomic reference because non-effectively final
        // values cannot be used from inside lambdas
        AtomicReference<Double> mult = new AtomicReference<>(0d);

        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // make sure ONLY
                    () -> rightTrigger > 0,            // the right trigger
                    () -> leftTrigger > 0              // is pressed
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // if it's pressed, set the multiplier to 1.0: full
                    // speed!
                    mult.set(1.0);
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // make sure ONLY
                    () -> leftTrigger > 0,             // the left trigger
                    () -> rightTrigger > 0             // is pressed
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // if it's pressed, set the multiplier to 0.25: very
                    // slow, for precise movement.
                    mult.set(0.25);
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.allFalse( // make sure BOTH of the
                    () -> rightTrigger > 0,    // triggers are NOT pressed
                    () -> leftTrigger > 0
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // the default multiplier is 0.5
                    mult.set(0.5);
                }
            );

        while (true)
            pathfinder.tick();
    }
}
```

Please note that this is NOT the best implementation of a multiplier-like
concept. It's rather verbose and can be simplified greatly.

### More advanced bindings
You can do some pretty cool and pretty advanced bindings using the
following class: `me.wobblyyyy.pathfinder2.utils.SupplierFilter`.

#### Requiring multiple buttons to be pressed
This binding will only be activated if the A and B buttons are pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.allTrue(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton  // assume bButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

#### Requiring one button to be pressed and other buttons not pressed
This binding will only be activated if the A button is pressed, and the
B, X, and Y buttons are not pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.trueThenAllFalse(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton, // assume bButton is a Supplier<Boolean>
            this::xButton, // assume xButton is a Supplier<Boolean>
            this::yButton  // assume yButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

#### Requiring any condition to be true
This binding will be activated whenever either the A, B, X, or Y button
is pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.anyTrue(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton, // assume bButton is a Supplier<Boolean>
            this::xButton, // assume xButton is a Supplier<Boolean>
            this::yButton  // assume yButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

#### Binding a speed multiplier
```java
// assume these are declared elsewhere for the sake of demonstration
Supplier<Boolean> rightTrigger;
Supplier<Boolean> leftTrigger;

// must be effectively final to use from within lambdas
AtomicReference<Double> multiplier = new AtomicReference(0d);

pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.allFalse(
            rightTrigger,
            leftTrigger
        )
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(0.5);
    )
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        rightTrigger,
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(1.0);
    )
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        leftTrigger,
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(0.25);
    );
```

#### A simple shifter
A shifter is a pretty simple concept - you can either shift up or down.
Pressing the right trigger will shift upwards, and pressing the left trigger
will shift downwards. This shifter doesn't actually do very much - it allows
you to shift up and down, but it doesn't do anything with the gear. Whenever
you press the A button, this will log the current gear to the standard output.
```java
Supplier<Boolean> rightTrigger;
Supplier<Boolean> leftTrigger;
Supplier<Boolean> aButton;

Shifter shifter = new Shifter(1, 1, 5, false, (pf) -> {});

pathfinder.getListenerManager()
    .bindNewlyMet(
        () -> SupplierFilter.trueThenAllFalse(rightTrigger, leftTrigger),
        (isPressed) -> isPressed,
        (isPressed) -> shifter.shift(ShifterDirection.UP)
    )
    .bindNewlyMet(
        () -> SupplierFilter.trueThenAllFalse(leftTrigger, rightTrigger),
        (isPressed) -> isPressed,
        (isPressed) -> shifter.shift(ShifterDirection.DOWN)
    )
    .bindButton(
        aButton,
        (isPressed) -> System.out.printf("current gear: %s%n", shifter.getCurrentGear())
    );
```

## Using prebuilt utilities
Prebuilt utilities are provided because they're common enough that I found
it's worth including some abstraction.
### Automatically rotate around a point
### Control an elevator
### Lock the robot's heading

## Plugins
A plugin is a piece of code that runs on top of Pathfinder and processes
data in a way not normally possible with Pathfinder.

### Loading plugins
The `Pathfinder` class provides a method for loading `PathfinderPlugin`
instances, utilizing a `PathfinderPluginManager`. There's not much you need
to know about HOW plugins are loaded, but all plugins are loaded when the
`Pathfinder` constructor is called.

#### Automatically loading plugins
Some plugins can load automatically by using the "automatic loading" feature
of Pathfinder. This allows plugins to be loaded whenever an instance of
Pathfinder is created, which can simplify the use of very frequently-used
plugins. I'd generally discourage using automatically loading plugins, only
because static code may cause issues with other codebases.

#### Disabling automatically loading plugins
The `Pathfinder` constructor that takes four arguments has a varargs `String`
argument that allows you to specify the name(s) of plugin(s) that you don't
want to automatically load. This overrides the automatically loading plugins.

### Creating a plugin
Creating a plugin is relatively simple - just extend `PathfinderPlugin`, which
is an abstract class, and override the methods you'd like to override. The
meaning of each of these methods should be fairly self-evident. If you'd like
to distribute your plugin, you can share the class that extends
`PathfinderPlugin` and have users instantiate it and then load it.

## Movement profile
Pathfinder, a library based entirely around movement, can track your robot's
movement. While that may be just short of astounding, I promise, it's true.

### Velocity
### Acceleration

## Path generation
Path generation is quite literally what the name suggests: generating a path.
Pathfinder uses an "A star" pathfinding algorithm.

### A quick suggestion
Don't use path generation if you don't have to. It's computationally expensive,
which, during loop-based operation, can cause some performance issues.

### Pathfinding algorithm
### Practical applications
### Implementation

## Recording and playback
### Recording movement
### Playing movement back

## Zones
### Zone-based events
#### Using a zone processor
##### On enter
##### On exit
##### While inside
#### Using a listener
##### On enter
##### On exit
##### While inside

## Random
### Generating a random string
### `SupplierFilter`
### Modifiers

## Debugging and simulation tools
### Simulated robot hardware
#### Simulated classes
#### Empty classes

## Sensors and non-movement related classes

