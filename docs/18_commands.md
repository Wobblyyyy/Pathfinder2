# Commands
The `pathfinder2-commands` module, introduced following the v2.0.0, provides
a set of utilities accommodating simple scripting functionality.

## Purpose of `pathfinder2-commands`
This module provides limiting scripting capabilities, allowing you to create
autonomous routines without having to write "actual" code. These capabilities
are, as previously stated, rather limited: conditional logic, for example,
has not yet been implemented.

## Example script
Here's an example of the scripting capabilities of the command system. This
example does the following:
- Define a value "speed" with a value of 0.5
- Define a value "tolerance" with a value of 2
- Define a value "angleTolerance" with a value of 5 degrees
- Define several points (A, B, C, D, and E)
- Set Pathfinder's speed to "speed" (0.5)
- Set Pathfinder's tolerance to "tolerance" (2)
- Set Pathfinder's angle tolerance to "angleTolerance" (5 degrees)
- Create a spline trajectory following points A, B, C, D, and E
- Follow the spline trajectory until it's complete
- Create a spline trajectory following points E, D, C, B, and A
- Follow the spline trajectory until it's complete
```
def speed 0.5
def tolerance 2
def angleTolerance 5 deg

def pointA 0,0,0
def pointB 5,10,0
def pointC 10,20,0
def pointD 15,30,0
def pointE 20,45,0

setSpeed $speed
setTolerance $tolerance
setAngleTolerance $angleTolerance

splineTo $pointA $pointB \
    $pointC $pointD $pointE

tickUntil

splineTo $pointE $pointD \
    $pointC $pointB $pointA

tickUntil
```

### Values
That script could be simplified as follows:
```
def pointA 0,0,0
def pointB 5,10,0
def pointC 10,20,0
def pointD 15,30,0
def pointE 20,45,0

setSpeed 0.5
setTolerance 2
setAngleTolerance 5 deg

splineTo $pointA $pointB \
    $pointC $pointD $pointE

tickUntil

splineTo $pointE $pointD \
    $pointC $pointB $pointA

tickUntil
```

It's not much of a change, but it demonstrates an important point about how
values are processed by Pathfinder's `commands` module. Every variable must
be prefixed with a dollar sign ("`$`"). Each of these values will simply
be substituted in, verbatim. Meaning this:
```
def veryCoolValue 0.1, 0.2, 5 deg

goTo $veryCoolValue
tickUntil
```

... is exactly the same as this.
```
goTo 0.1, 0.2, 5 deg
tickUntil
```

At the time of writing this documentation (March 9th, 2022), support for values
and definitions is incredibly limited. In the future, values will be more
easily mutable. Additionally, conditional logic based on these values will
be supported. At the moment, however, that's not supported. Sorry!

## Executing commands using `CommandRegistry`
I'd like to preface this by saying I strongly encourage you to make use of the
`Script` class instead of directly working with `CommandRegistry`, but this
is included for the purpose of demonstration. This will do exactly the same
thing as the previous script example, but it will do so making direct use
of `CommandRegistry` rather than leveraging the abstractions provided by
`Script`.
```java
import me.wobblyyyy.pathfinder2.CommandRegistry;

public class ExampleCommandRegistry {
    public void run(CommandRegistry registry) {
        String[] lines = new String[] {
            "def pointA 0,0,0",
            "def pointB 5,10,0",
            "def pointC 10,20,0",
            "def pointD 15,30,0",
            "def pointE 20,45,0",
            "",
            "setSpeed 0.5",
            "setTolerance 2",
            "setAngleTolerance 5 deg",
            "","
            "splineTo $pointA $pointB \"",
                "$pointC $pointD $pointE",
            "",
            "tickUntil",
            "",
            "splineTo $pointE $pointD \"",
                "$pointC $pointB $pointA",
            "",
            "tickUntil",
        };

        registry.parse(lines);
    }
}
```

## Comments
Comments are supported by the commands system, but support for them is limited.
Internally, comments are handled like so:
```java
int indexOfComment = line.indexOf("//");
if (indexOfComment > -1) {
    line = line.substring(0, indexOfComment);
    Logger.debug(
        CommandRegistry.class,
        "trimming line <%s> at index <%s> to remove comment",
        line,
        indexOfComment
    );
}
```

Basically, anything that goes after `//` in a line is considered to be part
of a comment, and will be ignored. `//` is the only type of comment currently
supported. In the feature, C-style comments (`/*` and `*/`) will be
supported.

## Available commands
This list is incomplete, mostly because I'm really lazy. Oops.
- `goTo`: go to a certain position (ex `goTo 15, 10, 5 deg`)
- `def`: define a value (ex `def abc 123`)
- `wait`: wait for a certain amount of time (in ms) (ex `wait 1000`)
- `setSpeed`: set Pathfinder's speed
- `setTolerance`: set Pathfinder's tolerance
- `setAngleTolerance`: set Pathfinder's angle tolerance
- `setTranslation`: set Pathfinder's translation
- `linearTrajectory`: create a linear trajectory
- `fastTrajectory`: create a fast trajectory
- `tick`: tick Pathfinder once
- `tickUntil`: tick until a trajectory is completed.
  - with no parameters: `tickUntil`
  - with a maximum time parameter: `tickUntil 1000`

### `goTo`
The `goTo` command closely mirrors the `goTo` method(s) of the `Pathfinder`
class. It's usage is very simple, but requires you to have previously set
Pathfinder's `speed`, `tolerance,` and `angleTolerance` values, which can be
done like so:
```
// make sure speed, tolerance, and angleTolerance have been set
setSpeed 0.5
setTolerance 2
setAngleTolerance 5 deg
```

#### Moving to a `PointXY`
```
// defining a point, then moving to it
def target 10,10
goTo $target

// just moving to a point
goTo 10,10
```

#### Moving to a `PointXYZ`
```
// defining a point, then moving to it
def target 10,10,45deg
goTo $target

// just moving to a point
goTo 10,10,45deg
```

## Creating your own commands
Creating your own commands allows you to implement non-standard functionality
into a Pathfinder script. Below is a complete code example that demonstrates
how to (1) instantiate a command, (2) how to add it to a command registry and
(3) how to execute the command.
```java
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.commands.Command;
import me.wobblyyyy.pathfinder2.commands.CommandRegistry;

public class CustomCommandExample {
    private final Command command = new Command(
        "doSomethingCool",          // the command's name
        (pathfinder, args) -> {     // what the command DOES
            System.out.println("doing something cool!");

            String argA = args[0];
            System.out.println(argA);

            if (args.length > 1) {
                String argB = args[1];
                System.out.println(argB);
            }
        },
        1,                          // minimum # of arguments
        2                           // maximum # of arguments
    );

    public CustomCommandExample(CommandRegistry registry) {
        registry.add(command);
    }

    public void run() {
        // would print the following output
        // doing something cool!
        // abc
        // xyz
        registry.execute("doSomethingCool abc xyz");

        // would print the following output
        // doing something cool!
        // ARGUMENT_1
        // ARGUMENT_2
        registry.execute("doSomethingCool ARGUMENT_1 ARGUMENT_2");
    }
}
```
