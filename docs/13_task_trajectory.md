# Task trajectory
`TaskTrajectory` is a trajectory implementation that has to do with the state
of the robot and NOT (exclusively, at least) the robot's position.

## Task trajectory options
- `initial` is a `Runnable` that will be run the very first time the
  trajectory's `isDone(PointXYZ)` method is called. This will only be run
  once.
- `during` is a `Runnable` that will be run every time the `isDone(PointXYZ)`
  method is called, including the first and last times it's executed.
- `onFinish` is a `Runnable` that will be executed only once, the very first
  time the trajectory is considered "finished."
- `isFinished` is a `Supplier<Boolean>` that tells the trajectory if it has
  finished its execution. If this returns true, the trajectory's
  `isDone(PointXYZ)` method will also return true.
- `minTimeMs` is the minimum time, in milliseconds, that the task may take.
  This overrides `isFinished`.
- `maxTimeMs` is the maximum time, in milliseconds, that the task may take.
  This overrides `isFinished`.
- `speed` is a `double` value that the `isDone(PointXYZ)` will return.

## Creating a `TaskTrajectory`
There are three ways you can create a `TaskTrajectory`.

### Using `TaskTrajectoryBuilder`
This one should be pretty self-explanatory. Here's an excerpt from
`Pathfinder`, demonstrating how to use a `TaskTrajectoryBuilder`.

```java
Trajectory trajectory = new TaskTrajectoryBuilder()
        .setInitial(initial)
        .setDuring(during)
        .setOnFinish(onFinish)
        .setIsFinished(isFinished)
        .setMinTimeMs(minTimeMs)
        .setMaxTimeMs(maxTimeMs)
        .build();
```

### Using Pathfinder's "task" method
The "task" method (and its variants) are instance methods of the `Pathfinder`
class that create a `TaskTrajectory` for you, without needing a constructor.
This has no functional benefit aside from requiring less of Java's infamous
boilerplate code.

### Using the constructor (not suggested)
`TaskTrajectory`'s constructor accepts the following arguments:
- `Runnable initial`
- `Runnable during`
- `Runnable onFinish`
- `Supplier<Boolean> isFinished`
- `double minTimeMs`
- `double maxTimeMs`

## Use cases
A `TaskTrajectory` can be used whenever you'd like your robot's movement to
pause while a task is executed, and then resume afterwards.
