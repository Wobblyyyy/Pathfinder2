# Preface
Alright. I'll try and keep this relatively short. There are a couple important
things to know about Pathfinder, and those are as follows.

## Important information
- All angles used within Pathfinder make use of the `Angle` class and have
  a value within the bounds 0 to 360 degrees. You can use the `fix()` method
  of an angle to "fix" the angle, ensuring it's within those bounds. Negative
  angles are possible, but discouraged.
- Everything is on a cartesian coordinate plane. This plane is absolute,
  meaning its orientation does not change regardless of a robot turning.
- If you put your robot down, facing forwards, and fire up Pathfinder,
  Pathfinder's default axes will map to the robot's initial orientation
  like so:
  - As the robot moves forwards, its Y position increases.
  - As the robot moves backwards, its Y position decreases.
  - As the robot moves rightwards, its X position increases.
  - As the robot moves leftwards, its X position decreases.

## Pathfinder's axes
I was planning on creating a nice little diagram in GIMP, but my laptop is
fairly low on battery right now and I'm not going to waste it on making an
image when I can do (just about) the same thing using some good old fashioned
ASCII art. Pretend your robot is at the very center (the little `+`) and is
facing forwards (the front of the robot is facing towards the `y+`). It's
pretty much layed out just like a cartesian coordinate plane - crazy!

```
              y+

              ^
              |
              |
              |
              |
x-  <---------+--------->  x+
              |
              |
              |
              |
              v

              y-
```

# Help! My robot isn't moving!
There are quite a few potential causes for an immobile robot, which can make
it frustrating to debug.
- If you're trying to move your robot to a position that's within the default
  tolerance (as defined by `Geometry.tolerancePointXYZ`), the robot will NOT
  move, and it will only turn. There are two ways to fix this:
  - Change the target point to be farther away.
  - Change `tolerancePointXYZ` (make it higher!)
