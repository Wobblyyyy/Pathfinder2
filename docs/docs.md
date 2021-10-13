# Pathfinder Documentation
Because who doesn't love some documentation? I know I certainly do.

# Table of Contents
This will be updated later.

## Geometry
Geometry is one of the important concepts in Pathfinder, given the entire
library is literally based around geometry. 

### Angles
The `Angle` class is a representation of... an angle. It's shocking, I know.
There's two important methods in the `Angle` class:
- `deg()` returns the angle's value, in degrees.
- `rad()` returns the angle's value, in radians.

Angles are incredibly useful (and used frequently) for geometry-related
operations. Your robot's heading/angle/direction/facing is an angle.
Your robot's heading/angle/direction/facing is dictated by a target angle
and a current angle.

### Points (PointXY and PointXYZ)
There's two types of points - `PointXY` and `PointXYZ`. `PointXYZ` is an
extension of `PointXY`.

#### PointXY
The simplest kind of point - also the basis for all of Pathfinder's geometry
system. `PointXY` has two main values:
- X
- Y

That's just about the simplest way to explain it - it's literally just a point.
There's a bunch of methods in the `PointXY` class - see the JavaDoc for
information on all of them, but the most important methods are listed below.

##### Method: distance
Get the distance between two points. This method uses the distance formula
to determine the distance between the two points.
```java
PointXY a = new PointXY(0, 0);
PointXY b = new PointXY(5, 0);

double distance = PointXY.distance(a, b); // distance = 5
```

```java
PointXY a = new PointXY(0, 0);
PointXY b = new PointXY(5, 5);

double distance = PointXY.distance(a, b); // 5 * sqrt(2) (roughly 7.07)
```

```java
PointXY a = new PointXY(-5, -5);
PointXY b = new PointXY(5, 5);

double distance = PointXY.distance(a, b); // 10 * sqrt(2) (roughly 14.14)
```

##### Method: inDirection
Create a new point a given distance away from a base point. There's two ways
to use this method:
```java
PointXY base = new PointXY(0, 0);
double distance = 7.07;
Angle direction = Angle.fromDeg(45);

// this method accepts three parameters:
// base:      the base point
// distance:  how far away the point should be
// direction: the direction the point should be created in        
PointXY newPoint = PointXY.inDirection(base, distance, direction); 

// the above point is (5, 5)
```

```java
PointXY base = new PointXY(-5, -5);
double distance = 14.14;
Angle direction = Angle.fromDeg(45);

PointXY newPoint = base.inDirection(distance, direction);

// the above point is (5, 5)
```

```java
PointXY base = new PointXY(0, 0);
double distance = 10;
Angle direction = Angle.fromDeg(90); // vertical/straight up

PointXY newPoint = base.inDirection(distance, direction);

// the above point is (0, 10)
```

## Operating Pathfinder
Pathfinder's operation is designed to be relatively simple.

### Robot
A `Robot` is composed of two elements - a `Drive` and an `Odometry`. 

#### Robot: drive
The `Drive` interface is responsible for physically driving a robot around
on a field. You need to have an implementation of the drive class to actually
operate Pathfinder. There are a couple of prebuilt drive implementations you
can use, if you so desire:
- `me.wobblyyyy.pathfinder2.drive.MeccanumDrive`
- `me.wobblyyyy.pathfinder2.drive.SwerveDrive`

If you'd prefer to use your own implementation of the `Drive` interface, you
simply have to implement a couple of methods - go see the JavaDoc for the
drive interface for more information. Actually, you can just look right here.
There's only a total of four methods you need to implement.

##### Methods from `me.wobblyyyy.pathfinder2.robot.Drive`
- `getTranslation()` - return the last drivetrain that was set to the robot.
- `setTranslation(Translation)` - set a translation to the robot. 

##### Methods from `me.wobblyyyy.pathfinder2.robot.modifiers.Modifiable`
- `getModifier()` - return the modifier.
- `setModifier(Function<E, E>)` - set the object's modifier.

##### The `AbstractDrive` class
If you're going to implement your own `Drive`, I'd encourage you to use the
`AbstractDrive` class (`me.wobblyyyy.pathfinder2.robot.AbstractDrive`). It 
doesn't do much, but it removes the need to implement methods from the
`Modifiable` interface.

##### Using the `Drive` class
It's pretty simple, to be honest. There are two main ways to use translations.
It's worth noting that the `Drive` interface assumes that if your robot is
given a translation, it'll move according to that translation, relative to
the robot's current position. In other words - the `Drive` interface accepts
RELATIVE translations - NOT ABSOLUTE translations.

Let's say your robot is at (0, 0) and is facing straight forwards. If you
tell your robot to move right (a translation of (1, 0, 0)), it should move
right - the robot's X coordinate should increase. Now let's say your robot
is facing NOT forwards - maybe a 90-degree rotation, for example. If you
give your robot the same translation ((1, 0, 0)) the robot's X coordinate
will DECREASE and become negative.

The `Translation` class (`me.wobblyyyy.pathfinder2.geometry.Translation`)
has a method `toRelative(Angle)` that accepts an `Angle` parameter representing
the robot's current heading. This converts an absolute translation to a
relative translation.

```java
// let's say you want to move the robot forwards, relative to the robot
Drive drive = ...; // assume this is actually implemented

Translation translation = new Translation(0, 1, 0);

drive.setTranslation(translation);
```

```java
// let's say you want to move the robot forwards, relative to the field
Drive drive = ...; // assume this is actually implemented
        
// assume 'robot' is declared
// assume 'robot' has a method 'getPos' that returns a PointXYZ - the robot's position
Translation translation = new Translation(0, 1, 0).toRelative(robot.getPos().z());
```

#### Robot: odometry
The `Odometry` interface is responsible for reporting information on the
robot's position on the field. Like the drive interface, you're required
to have an implementation of this. Also like the drive interface, there
are some prebuilt implementations you can feel free to use.

There are A LOT of methods you need to implement for the odometry interface.
I would STRONGLY encourage you to use the `AbstractOdometry` class instead
of the `Odometry` interface: `me.wobblyyyy.pathfinder2.robot.AbstractOdometry`.

The `Odometry` interface is incredibly simple - it should report the robot's
position on the field. That's it. This position should be absolute.
