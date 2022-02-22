# Modifying trajectories
Implementations of the `Trajectory` interface are intended to be immutable,
but the interface provides a variety of default methods intended to return
a new `Trajectory` based on the original, but modified in some way.

## Mirroring a trajectory
Let's say you have a trajectory like this:
```java
Trajectory trajectory = new LinearTrajectory(new PointXYZ(10, 10, 0), 0.5,
        2, Angle.fromDeg(5));
```

As you can (hopefully) tell, that trajectory is going to end up at (10, 10).
Although this is a very simple example, this demonstrates the general concept:
```java
Trajectory original = new LinearTrajectory(new PointXYZ(10, 10, 0), 0.5,
        2, Angle.fromDeg(5));

Trajectory a = original.reflectX(0); // would end at (-10, 0)
Trajectory b = original.reflectY(0); // would end at (0, -10)
Trajectory c = original.reflectX(0).reflectY(0); // would end at (-10, -10)
```

## Scaling a trajectory
Now say you have a really small trajectory, but you don't want it to be
really small.
```java
Trajectory trajectory = new LinearTrajectory(new PointXYZ(10, 10, 0), 0.5,
        2, Angle.fromDeg(5));

Trajectory a = original.multiply(0.5); // would end at (5, 5)
Trajectory b = original.multiplyX(0.5); // would end at (5, 10)
Trajectory c = original.multiplyY(0.5); // would end at (10, 5)
Trajectory d = original.multiply(-1); // would end at (-10, -10)
Trajectory e = original.multiply(-0.1); // would end at (-1, -1)
```

## Shifting a trajectory
What if you have a trajectory that you want to move? Say you have a
trajectory that you'd like the robot to follow as if the trajectory was
relative to the robot's initial position - how would you go about doing
that?
```java
Trajectory trajectory = new LinearTrajectory(new PointXYZ(10, 10, 0), 0.5,
        2, Angle.fromDeg(5));

Trajectory a = original.add(2, 2, 0); // would end at (12, 12)
Trajectory b = original.add(0, 1, 0); // would end at (10, 11)
Trajectory c = original.add(10, 10, 0); // would end at (20, 20)
Trajectory d = original.shiftToRobot(
        new PointXYZ(0, 0, 0),
        new PointXYZ(10, 10, 0)
); // would end at (20, 20)
Trajectory e = original.add(0, 2, 0).shiftToRobot(
        new PointXYZ(0, 0, 0),
        new PointXYZ(0, 0, 90)
); // would end at (-12, 10)
```

## Further reading
- [Linear trajectories](03_linear_trajectory.md)
- [Spline trajectories](04_advanced_spline_trajectory.md)
- [Fast trajectories](09_fast_trajectories.md)
