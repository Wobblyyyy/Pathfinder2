<p align="center">
<img src="../media/pathfinder2-logo.png" alt="Pathfinder2">
<br>
<i>Autonomous motion planning and control library for wheeled mobile robots.</i>
<br>
<i>Successor to <a href="https://github.com/Wobblyyyy/Pathfinder">Pathfinder</a>.</i>
</p>

# pathfinder2-commands
Pathfinder's built-in command/scripting support! Using the `commands` module,
you can write scripts (conventionally suffixed with `.pf` (for example, take
`coolAutonRoutine.pf`)) that can be executed by Pathfinder. The
`CommandRegistry` class parses `String`s into instructions interpreted by
Pathfinder, essentially creating a rudimentary scripting language. For examples
of the capabilities of this scripting language, check out the following:
- [`testGoTo.pf`](src/test/resources/me/wobblyyyy/pathfinder2/commands/testGoTo.pf)
- [`testSetTranslation.pf`](src/test/resources/me/wobblyyyy/pathfinder2/commands/testSetTranslation.pf)
- [`testSetValuesAndSplineTo.pf`](src/test/resources/me/wobblyyyy/pathfinder2/commands/testSetValuesAndSplineTo.pf)
- [`testSplineTo.pf`](src/test/resources/me/wobblyyyy/pathfinder2/commands/testSplineTo.pf)

## Dependencies

This module depends on...

- `pathfinder2-kinematics`
- `pathfinder2-geometry`
- `pathfinder2-core`
