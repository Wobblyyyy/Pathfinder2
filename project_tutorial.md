# Pathfinder Tutorial

Alright. Here's a pretty quick and pretty cool tutorial on using Pathfinder. Note that this is only a really basic
implementation of Pathfinder, and there's way cooler things you can do.

## Quick Overview

Okay. Here's a really quick overview of a couple of the most important concepts. Note that this does not fully encompass
everything you can do.

- Your robot has a drivetrain. This is done with the Drive interface. There are several ways you can implement a Drive,
  and there are a couple of cool implementations, such as `MeccanumDrive`.
- Your robot has an odometry system. This thing just has to tell us where the robot is. It doesn't really matter how you
  implement odometry - I've used three-wheel odometry before, and it's worked perfectly.

## Creating a drive and odometry setup

Code time!

```java
public class PathfinderGuide {
    // You shouldn't actually use "new Motor()" for each of these - you
    // should make/use your own motors.
    private Drive drive = new MeccanumDrive(
            new Motor(),
            new Motor(),
            new Motor(),
            new Motor()
    );

    // Once again, you shouldn't use "simulated odometry" - you should
    // implement your own, maybe check out three wheel odometry?
    private Odometry odometry = new SimulatedOdometry();
}
```

Cool. We've got those working now, right?

Next up, we have to construct a robot.

```java
public class PathfinderGuide {
    // ...

    private Robot robot = new Robot(drive, odometry);
}
```

Alright, there's our robot. Only a couple more things we need to do before we can get Pathfinder actually working.

```java
public class PathfinderGuide {
    // you'll have to figure out a turn controller that works for you
    private Controller turnController = new GenericTurnController(0.5);

    // you can read some documentation on this if you're curious
    private FollowerGenerator followerGenerator = new GenericFollowerGenerator(turnController);
}
```

Okay, Pathfinder time!

```java
public class PathfinderGuide {
    // ...
    private Pathfinder pathfinder = new Pathfinder(robot, followerGenerator);
}
```

There we go! In total, we have...

```java
public class PathfinderGuide {
    private Drive drive = new MeccanumDrive(
            new Motor(),
            new Motor(),
            new Motor(),
            new Motor()
    );

    // Once again, you shouldn't use "simulated odometry" - you should
    // implement your own, maybe check out three wheel odometry?
    private Odometry odometry = new SimulatedOdometry();

    private Robot robot = new Robot(drive, odometry);

    private Controller turnController = new GenericTurnController(0.05);

    private FollowerGenerator followerGenerator = new GenericFollowerGenerator(turnController);

    private Pathfinder pathfinder = new Pathfinder(robot, followerGenerator);
}
```

## Actually using Pathfinder

There's a ton of functionality in this library, but for the sake of keeping things simple, we'll only go over the most
basic concepts.

### Operating Pathfinder

The most important thing you can do is tick Pathfinder - if you don't tick it, nothing happens. For example:

```java
public class PathfinderGuide {
    private Pathfinder pathfinder;

    public void loop() {
        pathfinder.tick();
    }
}
```

The concept here is that Pathfinder is running in a loop - it constantly checks its state (where it is, what the chassis
is doing, where it's supposed to be going) and then decides what to do from there.

When you instruct Pathfinder to do something, you need to wait for it to finish doing what you asked it to do or cancel
it.

```java
public class PathfinderGuide {
    private Pathfinder pathfinder;

    public void moveAround() {
        pathfinder.goTo(new PointXYZ(10, 10, 45));

        while (pathfinder.isActive()) {
            // This line isn't necessary, but it's a good programming practice
            // to use something like this in your loops.
            Thread.onSpinWait();

            pathfinder.tick();
        }
    }
}
```

But what if we want to cancel it? Let's say we want to check for a gamepad button being pressed - what do we do then?

```java
public class PathfinderGuide {
    private Pathfinder pathfinder;

    public void maybeMoveAround() {
        pathfinder.goTo(new PointXYZ(10, 10, 45));

        while (pathfinder.isActive()) {
            pathfinder.tick();

            // If the A button on the gamepad is being held down, cancel
            // following the path.
            if (gamepad.a) {
                pathfinder.cancel();
            }
        }
    }
}
```

Now let's follow some more paths, shall we? We're going to learn about more concepts - exciting! Right! Yeah...

### Trajectories and Followers

Trajectories and followers are at the heart of Pathfinder's operation. I'd encourage you to go read the Javadocs for
those (maybe just check out these pages)

- [Follower](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/follower/Follower.java)
- [Trajectory](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/Trajectory.java)
- [LinearTrajectory](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/trajectory/LinearTrajectory.java)
- [GenericFollower](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/follower/GenericFollower.java)
- [GenericFollowerGenerator](https://github.com/Wobblyyyy/Pathfinder2/blob/master/pathfinder2-core/src/main/java/me/wobblyyyy/pathfinder2/follower/generators/GenericFollowerGenerator.java)

Trajectories are paths your robot can follow. Followers are the things that make your robot follow the paths. Let's take
a look at a simple example of a trajectory - a linear trajectory.

```java
public class PathfinderGuide {
    public void learningAboutTrajectories() {
        // Create a new angle at (10, 10), with the robot facing
        // 45 degrees.
        PointXYZ target = new PointXYZ(10, 10, Angle.deg(45));

        // Speed can be any value between 0 and 1 - 0 isn't moving, 1 is
        // as fast as possible.
        double speed = 0.5;
        
        // The "tolerance." Basically, Pathfinder has a tolerance for when
        // you're at a certain position. If you're at (9.5, 9.5) and your
        // target is (10, 10), you're technically 0.707 units away from the
        // target (we use the distance formula here). Because 0.707 is less
        // than 1.0, Pathfinder goes "hey, we've reached our target." If
        // you're at (9.0, 9.0), your distance is 1.414 units away from your
        // target (sqrt2). 1.414 is greater than 1.0, so Pathfinder knows it
        // hasn't reached it target yet.
        double tolerance = 1.0;
        
        // The angle tolerance - basically the same thing with the regular
        // tolerance, but for the angle the robot is/should be facing.
        Angle angleTolerance = Angle.deg(15);

        // Let's create a linear trajectory. It requires four parameters:
        // 1. a target/destination point
        // 2. a speed to move at (0.0 - 1.0)
        // 3. a tolerance value
        // 4. an angle tolerance value
        Trajectory trajectory = new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
        );
        
        // Tell Pathfinder to follow the trajectory.
        pathfinder.followTrajectory(trajectory);
        
        while (pathfinder.isActive()) {
            Thread.onSpinWait();
            
            pathfinder.tick();
        }
    }
}
```