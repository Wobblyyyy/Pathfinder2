# Advanced spline trajectory
[`AdvancedSplineTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/spline/AdvancedSplineTrajectory.java)
(excuse the long name) is the suggested spline trajectory implementation.

## The `AdvancedSplineTrajectory` class
`AdvancedSplineTrajectory` implements the `Trajectory` interface and uses
several splines to guide the robot along a path. The robot's speed, target
position, and target angle are all independent of each other and are controlled
by their own individual splines.

### Creating a spline trajectory
There are a couple of ways to create a spline trajectory.

#### `AdvancedSplineTrajectoryBuilder`
Although you could use the `AdvancedSplineTrajectory` constructor, I'd suggest
you don't, just because of the amount of boilerplate code you'd have to write.
Instead, you can make use of `AdvancedSplineTrajectoryBuilder`.
```java
Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
        .setSpeed(0.5)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5))
        .setStep(0.2)
        .add(new PointXYZ(5, 5, 0))
        .add(new PointXYZ(10, 15, 0))
        .add(new PointXYZ(12, 20, 0))
        .build();
```

#### `SplineBuilderFactory`
As you can see, that's a lot neater and easier to understand. To go even
further, you could use a `SplineBuilderFactory` to reduce boilerplate code
when using multiple trajectories.
```java
SplineBuilderFactory factory = new SplineBuilderFactory()
        .setStep(0.1)
        .setSpeed(0.5)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5));

Trajectory a = factory.builder().add(new PointXYZ(5, 5, 0))
        .add(new PointXYZ(10, 3, 0)).add(new PointXYZ(12, 0, 0)).build();
Trajectory b = factory.builder().add(new PointXYZ(0, 0, 0))
        .add(new PointXYZ(10, 3, 0)).add(new PointXYZ(20, 5, 0)).build();
Trajectory c = factory.builder().add(new PointXYZ(5, 5, 0))
        .add(new PointXYZ(10, 3, 0)).add(new PointXYZ(12, 0, 0)).build();
```

#### Pathfinder's "splineTo" methods
Once again, as you can see, that can greatly reduce the amount of boilerplate
code required. You can go a step further and use the convenience method
`splineTo` in `Pathfinder`. This means you don't have to create a factory
(or any builders), you just need to set some values.
```java
Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01)
        .setSpeed(0.5)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5))
        .splineTo(
                new PointXYZ(0, 0, 0),
                new PointXYZ(1, 2, 0),
                new PointXYZ(2, 4, 0),
                new PointXYZ(3, 8, 0)
        )
        .tickUntil();
```

Would you look at that? You can generate a spline trajectory and follow it
in only a couple lines of code.

## Types of spline interpolation
[`MonotoneCubicSpline`](../pathfinder2-geometry/src/main/java/me/wobblyyyy/pathfinder2/math/MonotoneCubicSpline.java)
is the default spline implementation in Pathfinder.
[`ApacheSpline`](../pathfinder2-geometry/src/main/java/me/wobblyyyy/pathfinder2/math/ApacheSpline.java)
is another implementation provided with Pathfinder that uses spline
interpolation from Apache Commons math library. `ApacheSpline` allows you
to use either cubic or Akima spline interpolation. Note that Akima spline
interpolation requires at least 4 control points, unlike cubic spline
interpolation, which only requires three.

Of course, using `ApacheSpline` will require you to include the Apache Commons
math library in your project, like so:
```groovy
implementation 'org.apache.commons:commons-math3:3.6.1'
```

### Rules for making splines
There are a couple guidelines to keep in mind while creating splines.
- Make sure there are no duplicate X/Y values. If there are any duplicate
  X or Y values, the spline's interpolation... won't work.
- Ensure X values are monotonic. It doesn't matter if they're increasing or
  decreasing, they just all need to be moving in the same direction.
- Y values should be monotonic... sometimes. If you attempt to create a spline
  trajectory with non-monotonic Y values, the `Trajectory` you'll get is
  actually a multi-segment trajectory made up of several splines. The path
  is grouped into trios - each of these trios must be individually X and Y
  monotonic.
