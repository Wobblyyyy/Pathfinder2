# Commands
The `pathfinder2-commands` module, introduced following the v2.0.0, provides
a set of utilities accommodating simple scripting functionality.

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

## Available commands
This list is incomplete, mostly because I'm really lazy. Oops.
- `goTo`: go to a certain position (ex `goTo 15, 10, 5 deg`)
- `def`: define a value (ex `def abc 123`)
- `wait`: wait for a certain amount of time (in ms) (ex `wait 1000`)
- `setSpeed`
- `setTolerance`
- `setAngleTolerance`
- `setTranslation`
- `linearTrajectory`
- `fastTrajectory`
- `tick`
- `tickUntil`: tick until a trajectory is completed.
  - with no parameters: `tickUntil`
  - with a maximum time parameter: `tickUntil 1000`

## Creating your own commands
I'm going to add a guide on this later, when I'm not as tired.
