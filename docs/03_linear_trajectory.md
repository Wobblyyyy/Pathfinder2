# Linear trajectory
The [`LinearTrajectory`](./pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java)
is the most simple form of trajectory. It has a target position, and it will
move to that target position at a linear speed. Linear trajectories also
have adjustable tolerance(s) so you can fine-tune trajectories for your robot.

## Creating a linear trajectory
The most intuitive way to create a linear trajectory is by using the
linear trajectory's constructor like so.
```java
Trajectory trajectory = new LinearTrajectory(
        new PointXYZ(10, 10, 0),    // target point
        0.5,                        // speed to move at (0-1)
        2,                          // tolerance
        Angle.fromDeg(5)            // angle tolerance of 5 degrees
);
```

### Following your new trajectory
Linear trajectories can be followed just like any other trajectory.
```java
Pathfinder pathfinder = null;            // pretend this isn't null
pathfinder.followTrajectory(trajectory); // queue the trajectory
pathfinder.tickUntil();                  // and follow it!
```

## Using `goTo` instead
Pathfinder's main class offers a `goTo(PointXYZ)` method that allows you to
create a whole linear trajectory with only a single parameter. In order to
use `goTo`, you'll first have to set some default values, which can be done
just like this:
```java
Robot robot = null;           // these are just null for demonstration purposes
Controller controller = null; // you'll need to implement these yourself

Pathfinder pathfinder = new Pathfinder(robot, controller)
        .setSpeed(0.5)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5));
```

From there, it's pretty smooth sailing - check it out.
```java
Pathfinder pathfinder = new Pathfinder(robot, controller)
        .setSpeed(0.5)
        .setTolerance(2)
        .setAngleTolerance(Angle.fromDeg(5));

pathfinder.goTo(new PointXYZ(10, 10, Angle.fromDeg(45)));
```
