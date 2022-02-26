# Global trajectory map
The `Pathfinder` class has a private static field named `TRAJECTORY_MAP`: it
maps `String` instances to `Trajectory` instances. There are several methods
that allow you to modify the contents of this map (variants of `addTrajectory`
specifically).

## Adding a trajectory
```java
public class TrajectoryMapDemo {
    static {
        Pathfinder.addTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_1",
                new EmptyTrajectory()
        );

        Pathfinder.addTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_2",
                new EmptyTrajectory()
        );
    }
}
```

## Removing a trajectory
```java
public class TrajectoryMapDemo {
    static {
        // first, add it
        Pathfinder.addTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_1",
                new EmptyTrajectory()
        );

        // then remove it
        Pathfinder.removeTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_1"
        );
    }
}
```

## Accessing (or following) a trajectory
There's two ways to access a trajectory.
```java
public class TrajectoryMapDemo {
    private final Pathfinder pf = Pathfinder.newSimulatedPathfinder(0.01);

    static {
        Pathfinder.addTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_1",
                new EmptyTrajectory()
        );
    }

    public void run() {
        // the first of the two ways
        pf.followTrajectory(TrajectoryMapDemo.class, "trajectory_1");

        // the second of the two ways
        Trajectory trajectory = Pathfinder.getTrajectory(
                TrajectoryMapDemo.class,
                "trajectory_1"
        );
        pf.followTrajectory(trajectory);
    }
}
```

## What's a "group?"
A "group" is a method of organization that allows trajectories to be
categorized by usage. This is the same concept as namespaces in a language
like C++: they essentially allow you to have duplicate functions in different
namespaces. There are two types of group supported: `String` and `Class<?>`.
The `Class<?>`-based option is suggested: every trajectory added to the map
should have its group set to the class that added the trajectory.

The `String` option works just as well, and is probably easier to use for
most use cases. You can feel free to use it, but the class method is suggested.
Why? Just because. There's not really any other reason to it. Sorry!

## Duplicate map keys
The trajectory map does NOT support duplicate keys - each key must be unique.
If you try to add a trajectory with the same name another trajectory already
has, an exception will be thrown.

To fix this, you can remove the previous trajectory.

## Stack tracing
Because the map is static, trajectories can be added from anywhere in the code.
To make it easier to debug, the stack trace of each trajectory that's added
to the map is stored in a separate map. In the event there's a naming conflict,
Pathfinder will show you where the naming conflict came from, so it can be
resolved.
