# The `Trajectory` system
The `Trajectory` system lies at the very heart of Pathfinder's movement. The
concept of a `Trajectory` is relatively simple - it's a movement-oriented and
stateful object that guides your robot along a certain path at a certain speed.

## What does a trajectory do?
Well, it guides your robot along a specified path at a specified speed. The
`Trajectory` interface is extremely minimal and it only has three methods:
- `PointXYZ nextMarker(PointXYZ current)`
- `boolean isDone(PointXYZ current)`
- `double speed(PointXYZ current)`

Each of those methods accepts a single `PointXYZ` parameter, representing the
robot's current position. Based on the robot's position, the `Trajectory`
should be capable of instructing the robot on what to do next.

## How are trajectories processed?
Through `Follower`s. Beyond the `Follower` interface, you won't need to know
much about how Pathfinder actually follows trajectories - if you're curious,
check out `FollowerExecutor`.

### The `Follower` interface
In order for Pathfinder to be able to follow a `Trajectory`, you need to use a
`Follower`. As the name would suggest, a `Follower`... well, it follows a
trajectory, of course!

I can't think of any reasons you'd want to use a `Follower` implementation
other than the default `GenericFollower`, but you have the option to do so if
you so desire.

### `FollowerExecutor`
Followers are executed with follower executors. I genuinely cannot conceive a
single reason why you'd ever need to know how a `FollowerExecutor` works, but
you can feel free to check out the `FollowerExecutor` class.

## Trajectory implementations
Several implementations of the `Trajectory` interface are provided within
Pathfinder.
- [`LinearTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java):
  The simplest trajectory implementation. Moves to a single point at a linear
  speed. This will dynamically correct for any movement error.
- [`MutableLinearTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/MutableLinearTrajectory.java):
  Just like `LinearTrajectory`, but it's mutable - that's really all I can say.
- [`FastTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/FastTrajectory.java):
  Similiar to `LinearTrajectory` in the sense that it moves to a single target
  point at a linear speed, but `FastTrajectory` will correct if it overshoots
  the target. Additionally, `FastTrajectory` does not require the robot's
  heading to be correct in order for the trajectory to be finished.
- [`TimedTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/TimedTrajectory.java):
  A trajectory with time constraints.
- [`TaskTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/TaskTrajectory.java):
  A trajectory that executes an action not related to movement.
- [`SplineTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/spline/SplineTrajectory.java):
  A trajectory that utilizes a `Spline` to guide the robot.
- [`AdvancedSplineTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/spline/AdvancedSplineTrajectory.java):
  The most advanced form of `Trajectory` provided in Pathfinder,
  `AdvancedSplineTrajectory` has separate spline interpolation functions
  responsible for controlling the trajectory's X/Y target, desired heading,
  and even speed.
- [`ArcTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/ArcTrajectory.java):
  A trajectory based on `Circle` that follows an arc.
- [`EmptyTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/EmptyTrajectory.java):
  A trajectory that does absolutely nothing.
- [`ControlledTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/ControlledTrajectory.java):
  A trajectory that utilizes a `Controller` to determine speed.
- [`MultiSegmentTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/multi/segment/MultiSegmentTrajectory.java):
  A `Trajectory` composed of several other trajectories. Each of these
  trajectories will be executed sequentially.
- [`MultiTargetTrajectory`](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/multi/target/MultiTargetTrajectory.java):
  Sort of like a `MultiSegmentTrajectory` of many linear trajectories: a
  `MultiTargetTrajectory` will guide the robot to a series of points.

## Further reading
- [Linear trajectories](./03_linear_trajectory.md)
- [Spline trajectories](./04_advanced_spline_trajectory.md)
- [Fast trajectories](./09_fast_trajectories.md)
- [Task trajectories](./13_task_trajectory.md)
- [Custom trajectories](./06_custom_trajectory.md)
