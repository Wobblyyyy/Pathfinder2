# Example implementation
It can be challenging to actually comprehend documentation if you don't have
a complete example of the code. So, here we are: example implementations of
Pathfinder in both driver controlled and autonomous modes.

## Making an autonomous routine
Alright. Here we are. To make this more realistic, our robot will have several
subsystems that do things during the autonomous routine. Those subsystems
are as follows:
- `BlockGripper`
- `Elevator`
- `Arm`
```java
public interface BlockGripper {
    void setState(boolean isOpen);
    boolean isAtTarget();
}

public interface Elevator {
    void setTarget(int target);
    boolean isAtTarget();
}

public interface Arm {
    void setState(boolean isExtended);
    boolean isAtTarget();
}
```

Notice that those are interfaces, not classes - this is mostly because I
don't feel like implementing the subsystems right now, so method stubs will
have to suffice.
```java
public class Autonomous {
    BlockGripper blockGripper;
    Elevator elevator;
    Arm arm;
    Pathfinder pathfinder;

    public Autonomous(BlockGripper blockGripper,
                      Elevator elevator,
                      Arm arm,
                      Pathfinder pathfinder) {
        this.blockGripper = blockGripper;
        this.elevator = elevator;
        this.arm = arm;
        this.pathfinder = pathfinder;
    }

    public void execute() {

    }
}
```

Before we go any further, let's define how the autonomous routine is
going to function.
- Close the block gripper.
- Retract the arm.
- Set the elevator to the lowest level.
- Drive to a certain position.
- Extend the arm.
- Open the block gripper.
- Drive to a certain position.
- Close the block gripper.
- Retract the arm.
- Open the block gripper.
- Set the elevator level.
- Move to a target position.

Equally importantly, let's define what the robot does. It has three main
components: the block gripper, the elevator, and the arm. The block gripper
is connected to the arm so that it can grab blocks when the arm is extended.
The block gripper can be opened or closed - an opened gripper can pick up
blocks, but a closed gripper can can actually hold on to them. The elevator
uses encoders to determine its position, and you can set a target position,
making the elevator go to that position allows you to score.
```java
public class Autonomous {
    BlockGripper blockGripper;
    Elevator elevator;
    Arm arm;
    Pathfinder pathfinder;

    public Autonomous(BlockGripper blockGripper,
                      Elevator elevator,
                      Arm arm,
                      Pathfinder pathfinder) {
        this.blockGripper = blockGripper;
        this.elevator = elevator;
        this.arm = arm;
        this.pathfinder = pathfinder;
    }

    /*
     * notice how each of the three methods below use the tickUntil method
     * to automatically continue execution until a certain condition is met.
     * the 1_000 supplied as a parameter is the timeout of the tickUntil:
     * if it takes longer than 1_000 milliseconds (1 second), it will
     * stop the tickUntil execution.
     *
     * because each of the methods has a tickUntil method, they're all
     * blocking. after setGripperState is called, for example, the robot
     * will wait until either...
     *   > the gripper is opened (preferable)
     *   > more than 1 second has elapsed
     */

    private void setGripperState(boolean isOpen) {
        blockGripper.setState(isOpen);
        pathfinder.tickUntil(blockGripper::isAtTarget, 1_000);
    }

    private void setElevatorLevel(int elevatorLevel) {
        elevator.setTarget(elevatorLevel);
        pathfinder.tickUntil(elevator::isAtTarget, 1_000);
    }

    private void setArmState(boolean isExtended) {
        arm.setState(isExtended);
        pathfinder.tickUntil(arm::isAtTarget, 1_000);
    }

    public void execute() {
        // close the gripper, retract the arm, and reset the elevator
        setGripperState(false);
        setElevatorLevel(0);
        setArmState(false);

        // create a new trajectory to move to our first target point
        // use tickUntil to tick until the trajectory has finished, WITH
        // A TIME LIMIT - if the trajectory takes longer than 5,000
        // milliseconds, it'll automatically stop executing
        Trajectory a = new LinearTrajectory(
                new PointXYZ(16, 48, 45),
                0.5,
                2,
                Angle.fromDeg(5)
        );
        pathfinder.followTrajectory(a).tickUntil(5_000);

        // open the gripper so we can grab a block
        // extend the arm
        setGripperState(true);
        setArmState(true);

        // move to our next position - our autonomous is picking up a block,
        // so we need to position ourselves so that we're lined up with
        // the block. let's pretend (18, 48, 45 deg) is the position we need
        // to be in to grab the block
        Trajectory b = new LinearTrajectory(
                new PointXYZ(18, 48, 45),
                0.5,
                1,
                Angle.fromDeg(2)
        );
        pathfinder.followTrajectory(b).tickUntil(5_000);

        // close the gripper, then close the arm
        setGripperState(false);
        setArmState(false);

        // open the gripper so that we can drop the block into the elevator
        // and then set the elevator level to 1,000 (let's say that's the
        // position it needs to be at to score points)
        setGripperState(true);
        setElevatorLevel(1_000); // encoder counts

        // finally, follow one last trajectory - go to (32, 32, 0 deg)
        Trajectory c = new LinearTrajectory(
                new PointXYZ(32, 32, 0),
                0.5,
                2,
                Angle.fromDeg(5)
        );
        pathfinder.followTrajectory(c).tickUntil(5_000);
    }
}
```

## Enabling driver control
