# Making a motor
Motors are a pretty important part of a robot, especially if you want that
robot to be able to move. There are already a couple of implementations of
the `Motor` interface available:
- `AbstractMotor` is a good starting point for any non-specific motor
- `SparkMaxMotor` supports the CAN Spark Max motor controller for FRC
- `TalonFXMotor` supports the Talon FX motor controller for FRC
- `TalonSRXMotor` supports the Talon SRX motor controller for FRC

If you can't make use of one of those, or you simply don't want to, here's
a quick guide on creating your own implementation of the `Motor` interface.

## Methods of the `Motor` interface
There's only two methods in the `Motor` interface, and those are as follows.
- `double getPower()`:
  Get the motor's current power value. If the motor does not have a method to
  access its current power value, simply return the last power value that
  was set to the motor.
- `void setPower(double)`:
  As the name would suggest, set power to the motor. This should (usually)
  change the physical state of the motor.

Given there's only two methods you need to worry about, it's pretty easy to
create a `Motor` implementation from scratch.

## Skeleton implementation
This is among the simplest possible implementations of `Motor`.
```java
public class ExampleMotor implements Motor {
    private double power = 0;

    public ExampleMotor() {

    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
    }
}
```

Please note that the above implementation won't actually do anything: it's
not communicating with the robot's hardware, so it can't do much.

## `AbstractMotor`
The `AbstractMotor` class has a couple of advantages over the regular
`Motor` interface.
- Supports minimum and maximum power values
- Supports dead-band (if you try to set power to the motor, but the power
  value you try to set is lesser in magnitude than this dead-band, the motor's
  power will be set to 0 instead)
- "Lazy mode" (read more about this in the `AbstractMotor` class: basically,
  it tries to save CPU cycles by only adjusting the motor's power when it
  would actually impact the motor)

### Using the constructor(s)
Let's say you have an object named `VeryCoolMotor`. Here's a stub for that
type:
```java
public interface VeryCoolMotor {
    double getPower();
    void setPower(double power);
}
```

As you can see, it's exactly the same as the `Motor` interface: this is just
for the purpose of demonstration.

Here's how you'd create an abstract motor with that.
```java
VeryCoolMotor vcm = null;

Motor abstractMotor = new AbstractMotor(vcm::setPower, vcm::getPower);
```

`vcm` should (obviously) not be null.

### Overriding methods
Alternatively, as of Pathfinder2 v1.4.1, you can override methods in the
`AbstractMotor` class to implement the same functionality.
```java
public class ExampleMotor extends AbstractMotor {
    private final VeryCoolMotor vcm;

    public ExampleMotor(VeryCoolMotor vcm) {
        this.vcm = vcm;
    }

    @Override
    public double rawGetPower() {
        return vcm.getPower();
    }

    @Override
    public void rawSetPower(double power) {
        vcm.setPower(power);
    }
}
```
