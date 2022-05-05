# Documentation
Welcome to the documentation included within Pathfinder! There's quite a few
documents in this directory, all of which are markdown so that they can easily
be browsed from GitHub.

- [Preface](./00_preface.md):
  A couple of quick pointers you might want to make note of.
- [Setting up your robot](./01_setting_up_robot.md):
  Before you start using the library, you'll have to get your robot set up.
  This is a guide on doing exactly that.
- [Calibration](./14_calibration.md):
  Calibration is one of the most important things you can do to so that
  Pathfinder is more accurate.
- [Example implementation](./10_example_implementation.md):
  Here's an example of Pathfinder in action.
- [The `Trajectory` system](./17_the_trajectory_system.md):
  The `Trajectory` system is the core of Pathfinder's movement. Such, it's a
  fairly important concept to understand in order to maximize the benefit you
  get from utilizing the library.
- [Ticking Pathfinder](./19_ticking_pathfinder.md):
  A complete (or at least hopefully complete) guide to ticking Pathfinder.
  Ticking is key to operating Pathfinder.
- [Controllers](./02_controllers.md):
  Open-loop controllers are a concept used widely in robotics. This is a brief
  overview on what they are and how to use them.
- [Linear trajectories](./03_linear_trajectory.md):
  Linear trajectories are the simplest form of trajectories and the logical
  starting point if you've never used Pathfinder before.
- [Spline trajectories](./04_advanced_spline_trajectory.md):
  Spline trajectories provide significantly more flexibility over your robot's
  motion, but are slightly harder to use.
- [Fast trajectories](./09_fast_trajectory.md):
  Fast trajectories are a simple type of trajectory that does not account
  for heading and does not correct if it overshoots the target point. These
  are useful when you need your robot to move... somewhere over there.
- [Listeners](./05_listeners.md):
  Listeners are baked into Pathfinder by default and allow you to listen for
  certain events by making use of functional interfaces and Pathfinder's
  `tick()` method. These can help to greatly simplify making user controls.
- [Making a tank drive](./20_making_a_tank_drive.md):
  A differential drive/tank drive is one of the most common types of
  drivetrain. This is a guide that explains how to make a differential drive.
- [Commands](./18_commands.md):
  The `pathfinder2-commands` module implements basic scripting functionality
  into Pathfinder.
- [Recording](./22_recording.md):
  State recording allows you to record your robot's actions and play them
  back later on. Example use case: you could record 30 seconds of Tele-Op
  gameplay and play it back during autonomous.
- [Custom trajectory implementation](./06_custom_trajectory.md):
  If you'd like to REALLY customize Pathfinder and none of the default
  `Trajectory` implementations satisfy your needs, this is a guide on creating
  your own `Trajectory` implementation.
- [Modifying global values](./07_global_values.md):
  If Pathfinder's default constants don't satisfy you, you can modify many
  (all, actually, if I'm not mistaken) of them.
- [Modifying trajectories](./08_modifying_trajectories.md):
  The `Trajectory` interface allows you to apply modifiers to trajectories,
  allowing you to scale, invert, reflect, offset, or shift a trajectory
  to suit your needs.
- [Zones](./11_zones.md):
  Zones are one of the more advanced concepts in Pathfinder. They allow you to
  make your robot perform certain actions based on its location on the field.
- [Global trajectory map](./12_global_trajectory_map.md):
  The global trajectory map can be used for smaller projects to make code
  easier to write. I would NOT encourage you to use the global trajectory
  map for larger projects, as it can be confusing.
- [Task trajectories](./13_task_trajectory.md):
  Task trajectories are the only type of trajectory bundled with Pathfinder
  that are not movement-oriented - instead, they perform a task.
- [General documentation](./15_general_documentation.md):
  This is directly stolen from the documentation portal, and I'm only
  including it here to make it easier to find.
- [Making a motor](./16_making_a_motor.md):
  A guide on creating a motor.
