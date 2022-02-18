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
