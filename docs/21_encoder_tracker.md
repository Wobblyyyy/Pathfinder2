# `EncoderTracker`
The `EncoderTracker` class is responsible for - you guessed it - tracking an
encoder. Specifically, it tracks the velocity and speed of the encoder.

## `EncoderConverter`
In order to create an `EncoderTracker`, you'll first need an
`EncoderConverter`, responsible for converting encoder ticks into an actual
distance. The `EncoderConverter` constructor accepts two parameters:
- `double encoderCountsPerRevolution`: the encoder's "counts per revolution"
  value. If you don't know this value off the top of your head, do some
  research on your specific encoder. Google is a good starting place.
- `double wheelCircumference`: the circumference of the wheel. This should
  be whatever unit you're using for the rest of Pathfinder.

`EncoderConverter` doesn't actually do anything other than store two `double`
values. Look at that: a record!

## `EncoderTracker` constructor
There are two constructors available: one with two parameters and one with
three parameters. The former of the two will call the latter with a default
value for the `isInverted` parameter: false.

The parameters are:
- `EncoderConverter converter`: an `EncoderConverter` that stores the values
  required to convert encoder ticks into distance values.
- `Supplier<Integer> getTicks`: a `Supplier` that returns how many ticks
  the encoder is at.
- `boolean isInverted`: is the `EncoderTracker` inverted? Just in case,
  you never know...

## Creating an `EncoderTracker`
In order to create an `EncoderTracker`, you'll need an `EncoderConverter`
and a `Supplier`. The `EncoderConverter` should have values that will allow
the `EncoderTracker` to convert encoder counts to distance traveled. The
`Supplier` should return the encoder's "counts" value. This example uses
`DcMotor` from the FTC SDK. If you're unaware, `getCurrentPosition` is
a method of the `DcMotor` class that returns the counts of the encoder
that's attached to the motor.
```java
DcMotor right = hardwareMap.get(DcMotor.class, "right");
DcMotor left = hardwareMap.get(DcMotor.class, "left");

Supplier<Integer> rightTicks = right::getCurrentPosition;
Supplier<Integer> leftTicks = left::getCurrentPosition;

EncoderConverter converter = new EncoderConverter(1_024, 12.0);

EncoderTracker rightTracker = new EncoderTracker(converter, rightTicks);
EncoderTracker leftTracker = new EncoderTracker(converter, leftTicks);
```

## Use cases for `EncoderTracker`
The most prominent use case for `EncoderTracker` is to track the velocity
of an encoder. You need to track the velocity of encoders in order to
integrate the robot's velocity over time to calculate the position of
the robot.
