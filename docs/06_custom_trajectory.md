# Custom trajectories
Trajectories are the very core of Pathfinder's movement. If, for whatever
reason, the default trajectories don't suit you, then you can create your
own pretty easily by implementing the `Trajectory` interface.

## `Trajectory` interface
The `Trajectory` interface has three methods:
- `PointXYZ nextMarker(PointXYZ)` get the next "marker point." A marker point
  is a point that Pathfinder uses to calculate the next `Translation`. Based
  on the robot's current position and the marker position, your robot will
  generate a `Translation` that will move the robot closer to the marker
  point. This should always return a point that is NOT the robot's position,
  unless the trajectory is finished.
- `boolean isDone(PointXYZ)` is the trajectory done? If the trajectory has
  finished its execution and is ready to be dequeued, this should return true.
  If the trajectory has not finished its execution, this should return false.
- `double speed(PointXYZ)` get the robot's speed. This is a value, 0.0 to 1.0,
  that specifies how fast the robot should move. If you want your robot to move
  at a glacial pace, try using a lower speed value. If you want your robot to
  move at a pace that's not quite glacial, try a higher speed value. And if
  neither of those suit you, try something right in the middle.

## Example implementation
Here's a really quick `Trajectory` implementation. This is similiar to the
`LinearTrajectory` provided within Pathfinder, except without a few of the
bells and whistles. I would encourage you to use `LinearTrajectory` over
this implementation: this is just for the purpose of demonstrating a correct
implementation of `Trajectory`.
```java
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * primitive implementation of the concept of LinearTrajectory. this is
 * a demonstration on implementing the methods required by the Trajectory
 * interface.
 */
public class ExampleTrajectory implements Trajectory {
    private final PointXYZ target;
    private final double speed;

    public ExampleTrajectory(PointXYZ target,
                             double speed) {
        this.target = target;
        this.speed = speed;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        // always just return the same marker position, because... well,
        // there's only one marker position. the robot's current position
        // is passed to this method in case a more advanced trajectory
        // may change marker positions based on the robot's current position:
        // an AdvancedSplineTrajectory, for example, does exactly that
        return target;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        // has the robot reached the correct X and Y values? this has a
        // tolerance of two, so if the robot is within 2 units of the target,
        // it'll evaluate to be true
        boolean xy = current.absDistance(target) < 2;

        // has the robot reached the correct orientation? this has a tolerance
        // 5 degrees
        boolean z = current.z().isCloseDeg(target.z().fix(), Angle.fromDeg(5));

        // the robot must have reached the correct X, Y, and Z values in order
        // have actually completed the trajectory
        return xy && z;
    }

    @Override
    public double speed(PointXYZ current) {
        // just return the same constant speed value regardless of where
        // the robot is - the robot's current position is only provided as
        // an optional value that can be used in calculating a speed value
        // so that the robot moves at a different speed at different points
        // throughout the trajectory, but that's for later
        return speed;
    }
}
```

## Going further
Of course, you can do much more with the `Trajectory` interface, and the
above example was exactly that - a simple example. If you'd like to learn
more about what you can do with trajectories, check out some of the
existing implementations:
- [LinearTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java)
  is the default implementation of a trajectory in Pathfinder. It moves to
  a single target point at a single speed and has tweakable tolerance values.
- [AdvancedSplineTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/spline/AdvancedSplineTrajectory.java)
  is, as the name would suggest, the most advanced type of trajectory available
  in Pathfinder. You can use independent spline functions to determine the
  robot's speed and target position (including angle!), allowing you to
  customize your robot's movement with curves.
- [FastTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/FastTrajectory.java)
  is the simplest implementation of a trajectory in Pathfinder. It's like the
  linear trajectory, but it has no tolerance values, and it doesn't require
  the robot to be aligned at the correct heading.
- [ControlledTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/ControlledTrajectory.java)
  is a trajectory that makes use of a `Controller` to determine the robot's
  speed, allowing you to make a trajectory that accelerates or decelerates.
- [MultiTargetTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/multi/target/MultiTargetTrajectory.java)
  is basically a `LinearTrajectory` that has multiple sequential target points.
- [MultiSegmentTrajectory](../pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/multi/segment/MultiSegmentTrajectory.java)
  isn't really a trajectory - it's a wrapper for other trajectories, allowing
  you to chain multiple trajectories together into one `MultiSegmentTrajectory`.
