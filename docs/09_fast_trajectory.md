# Fast Trajectory
The [`FastTrajectory`](./pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/FastTrajectory.java)
is similiar to the `LinearTrajectory`, except that it doesn't require the
robot to compensate for potential error, and it doesn't require the robot
to meet the desired heading before completion.

## Creating a fast trajectory
The most intuitive way to create a linear trajectory is by using the
linear trajectory's constructor like so.
```java
Trajectory trajectory = new FastTrajectory(
        new PointXYZ(0, 0, 0),      // robot's initial position
        new PointXYZ(10, 10, 0),    // robot's target point
        0.5                         // the trajectory's speed
);
```

### Following your new trajectory
Fast trajectories can be followed just like any other trajectory.
```java
Pathfinder pathfinder = null;            // pretend this isn't null
pathfinder.followTrajectory(trajectory); // queue the trajectory
pathfinder.tickUntil();                  // and follow it!
```

## Use cases
- You're following a long series of points and don't have to be incredibly
  precise about where your robot ends up while in the middle of these
  points. You could use a fast trajectory to make sure no time is wasted
  by compensating for any movement error.
- You need the robot to move in a certain direction but don't care how far
  the robot moves in that direction (obviously with some restrictions). If
  you use a fast trajectory, your robot will move AT LEAST the distance you
  specified.

## Further reading
- [Linear trajectories](03_linear_trajectory.md)
- [Spline trajectories](04_advanced_spline_trajectory.md)

