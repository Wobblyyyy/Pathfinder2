<h1>Project Structure</h1>
<ul>
  <li>
    <code>pathfinder2-core</code><br>
    <i>Core code behind Pathfinder. This module depends on both the geometry and
    kinematics portion of Pathfinder. This module is needed to actually use Pathfinder,
    outside of the geometry and kinematics portions of the library.</i>
  </li>
  <li>
    <code>pathfinder2-geometry</code><br>
    <i>Everything geometry-related in Pathfinder. The library contains quite a few
    geometry utilities that may be useful in other projects, so this has been split
    into a separate module.</i>
  </li>
  <li>
    <code>pathfinder2-kinematics</code><br>
    <i>Forwards and inverse kinematics. Tank, swerve, meccanum - etc. This module
    contains kinematics and odometry for most major types of drivetrains.</i>
  </li>
</ul>
