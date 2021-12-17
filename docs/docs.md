# Pathfinder Documentation
Because who doesn't love some documentation? I know I certainly do.

# Table of Contents
This will be updated later.

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

double distance = PointXY.distance(a, b); // 5 * sqrt(2) (roughly 7.07)
```

```java
PointXY a = new PointXY(-5, -5);
PointXY b = new PointXY(5, 5);

double distance = PointXY.distance(a, b); // 10 * sqrt(2) (roughly 14.14)
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
/**
 * Convert an absolute translation into a relative translation.
 *
 * <p>
 * You may be more familiar with this concept if we use the term
 * "field-relative" and "robot-relative". This method converts a field
 * relative translation into a robot relative one.
 * </p>
 *
 * @param translation the original (absolute) {@code Translation}.
 * @param heading     the heading the robot is currently facing. This value
 *                    should almost always come directly from the robot's
 *                    odometry system.
 * @return a relative translation.
 */
public static Translation absoluteToRelative(Translation translation,
                                             Angle heading) {
}
```

## Operating Pathfinder
Pathfinder's operation is designed to be relatively simple.

### Ticking Pathfinder
This is absolutely crucial to operating the library - you need to "tick" it.
Ticking is the process of updating the robot's translation based on it's
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

#### Ticking Pathfinder outside of a loop
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
Method chaining is beautiful - who doesn't love method chaining?

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
            .andThen((pathfinder -> {
              doSomething();
            }))
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

##### Using the `Drive` class
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

### Trajectories
Trajectories are the basis for Pathfinder's movement. Well, technically
speaking, `Follower`s actually control your robot's movement, but instances
of the `Trajectory` interface dictate how your robot moves.

#### Linear trajectory
The most simple kind of trajectory is the [linear trajectory](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java).
It can be (and is) described as follows:

> The most simple type of trajectory. A linear trajectory does nothing other
> than go to a point at a linear speed. Such, there's not much you can
> customize here. But it's simple, and it works. Hopefully, that is.

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
 *                       trajectory - all of the other points ignore
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