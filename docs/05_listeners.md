# Listeners
Listeners allow you to "listen" for certain conditions, making it easy to
write event-driven code. Listeners need to be ticked, which happens when
Pathfinder's `tick()` method calls the `tick()` method of Pathfinder's
`ListenerManager`, which in turn calls the `tick()` method of all of the
associated listeners.

Listeners function by repeatedly checking to see if a certain condition has
been met. In a robotics environment, your robot's physical actions are
separated from your code, so listeners work magically - you simply plop one
down and you're good to go. In an environment where fields/variables must be
changed manually via code, listeners shouldn't be used, as they can
overcomplicate code.

## Ticking listeners
Listeners must be ticked in order to function properly. It's strongly suggested
that you make use of the `ListenerManager` class, as it makes managing
listeners significantly easier. `Pathfinder` has a method called
`getListenerManager()` which returns the `ListenerManager` that that instance
of Pathfinder is using.

### If you used a listener manager
If you register a listener by using the `pathfinder#getListenerManager()`'s
`bind`, your listener will automataically be updated whenever Pathfinder's
`tick()` method is called.

### If you did not use a listener manager
If you did not use a listener manager, you'll have to figure out how to
tick your listeners on your own. I promise, it's not too hard - you just
have to use the `tick` method, and you'll be all good!

## Using bindings
The `listening` package of Pathfinder provides many utilities designed to
simplify writing code for a robot. These utilities are customized to my
preferences and using them may not be appropriate if a different solution
is preferable.

### Listener manager
First, it's important to understand HOW the listener manager works. It's
not all that difficult, to be honest. This example is going to assume
a robotics context:
- Bind the listener (say we want to make dpad up do something)
- Tick Pathfinder as normal

See? Not too bad. Each of the listeners in the listener manager is added
to a collection. That collection is polled/ticked/updated every time Pathfinder
is ticked.

**IF YOU DON'T TICK PATHFINDER, LISTENERS WILL NOT WORK.**

### Binding buttons
Buttons are a critical part of user input.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // bind imaginary controls to an imaginary A button
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                aButton::isPressed,
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // code to be run whenever the A button has been
                    // pressed
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                aButton::isPressed,
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // code to be run whenever the A button has been
                    // released
                }
            );

        // tick pathfinder forever and ever...
        //                     ... and ever...
        //                     ... and ever...
        //                     ... and ever...
        //                     ... and ever.
        while (true)
            pathfinder.tick();
    }
}
```

### Binding buttons (but easier)
This is only sightly easier than the previous approach, but who doesn't
love writing clean code? Exactly.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                aButton::isPressed,
                () -> {}
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                aButton::isPressed,
                () -> {}
            );

        while (true)
            pathfinder.tick();
    }
}
```

### Binding arbitrary objects
You can also bind arbitrary objects, allowing Pathfinder to handle just
about any event-based functionality you want. The "bind" method of the
`ListenerManager` class (accessible via `Pathfinder#getListenerManager()`)
is a generic method with type parameter T, representing the type of object
that's being listened to. Conveniently enough, Java's lambda syntax makes
it very easy to create these bindings.

#### Supplier
This `Supplier` of type T should accept input for the binding. This input
can be anything at all. This is frequently a `Supplier<Boolean>`, as it
allows you to bind something to a button. For example, here's a basic
binding attached to a button.
```java
public class Example {
    /**
     * this method is meant to emulate a method that gets the state of
     * a button. for the purpose of demonstration, assume this method
     * returns whether or not a physical button (the A button in this case)
     * is pressed.
     *
     * @return the button's current state.
     */
    public boolean aButton() {
        return true;
    }

    public void bindButton() {
        // print a message whenever a button is pressed
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                this::aButton,
                (isPressed) -> isPressed,
                (isPressed) -> System.out.println("A button has been pressed!");
            );
    }
}
```

#### Predicate
How is it determined if the condition is met or not? A `Predicate` is used.
This is the same type as the `Supplier`. Every time the listener is ticked,
this predicate will be tested (using the `Supplier`) for input. If the
predicate returns true, the condition is considered to have been met. If
the predicate returns false, the condition is considered to have not been met.

#### Predicate with a boolean
Because a boolean is already a predicate in itself, you simply have to return
the value of the boolean. All this predicate does is return the input value:
because the input value is a boolean, and `Predicate`s must return booleans,
we're all good!
```java
public class Example {
    private final Predicate<Boolean> predicate = (bool) -> bool;
}
```

#### Predicate with an arbitrary object
With an arbitrary object, you can have any condition you want.
```java
public class Example {
    private final Predicate<PointXYZ> predicate = (point) -> point.x() > 10;
}
```

#### Consumer
When the condition is met and the listener mode is active (for the "NEWLY
MET", this happens the first time the `Predicate` returns true and will not
happen again until the `Predicate` returns false then true once again), this
`Consumer` accepts an input of type T as a parameter. This is the input value
that caused the condition to be true.

#### Code example
Here's a complete code example.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    // whenever x or y individually exceeds 500, print out
    // "x value has exceeded 500!" or "y value has exceeded 500!"
    // if both the x and y values exceed 500, print out
    // "x AND y values have exceeded 500!"
    // if only x or only y exceeds 500, print out
    // "only x exceeds 500!" or "only y exceeds 500!"
    public void bindControlsAndRun() {
        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),      // Supplier<PointXYZ>
                (position) -> position.x() > 500,    // Predicate<PointXYZ>
                (position) -> System.out.println("x value has exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),      // Supplier<PointXYZ>
                (position) -> position.y() > 500,    // Predicate<PointXYZ>
                (position) -> System.out.println("y value has exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> pathfinder.getPosition(),
                (position) -> position.x() > 500 && position.y() > 500,
                (position) -> System.out.println("x AND y values have exceeded 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // note that this is some
                    () -> position.x() > 500,          // pretty terrible
                    () -> position.y() > 500           // code - suppliers are
                ),                                     // not appropriate here,
                (bool) -> bool,                        // but this is only a demonstration, so who cares?
                (bool) -> System.out.println("only x exceeds 500!")
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse(
                    () -> position.y() > 500, // must be true
                    () -> position.x() > 500  // must be false
                ),
                (bool) -> bool,
                (bool) -> System.out.println("only y exceeds 500!")
            );

        while (true)
            pathfinder.tick();
    }
}
```

### Binding joysticks
How else can you drive the robot? Exactly. You'll most likely need to bind
joysticks to drive your robot during tele-op.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // example joystick values for demonstration purposes
        double forwardsPower = 0.0;
        double sidewaysPower = 0.0;
        double turnPower = 0.0;

        pathfinder
            .bind(
                ListenerMode.CONDITION_IS_MET,
                () -> true,                    // true, so it's always executed
                (isPressed) -> true,           // true, so it's always executed
                (isPressed) -> {
                    pathfinder.setTranslation(new Translation(
                        forwardsPower,
                        sidewaysPower,
                        turnPower
                    ));
                }
            );

        while (true)
            pathfinder.tick();
    }
}
```

### Binding a speed modifier
When one gear isn't cool enough... This sample builds upon the previous
sample on binding joysticks. Pressing the right trigger sets the "speed
multiplier" to 1.0, making the robot move as fast as it can. Pressing the
left trigger sets the multiplier to 0.25, making the robot significantly
slower. If neither trigger is pressed, the multiplier will be set to 0.5,
making the robot move at its normal speed.
```java
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.geometry.Translation;

import java.util.atomic.AtomicReference;

public class BindingUserControls {
    Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

    public void bindControlsAndRun() {
        // example joystick values for demonstration purposes
        // range: -1.0 to 1.0
        double forwardsPower = 0.0;
        double sidewaysPower = 0.0;
        double turnPower = 0.0;

        // example trigger values for demonstration purposes
        // range: 0.0 to 1.0
        double rightTrigger = 0.0;
        double leftTrigger = 0.0;

        // needs to be an atomic reference because non-effectively final
        // values cannot be used from inside lambdas
        AtomicReference<Double> mult = new AtomicReference<>(0d);

        pathfinder.getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // make sure ONLY
                    () -> rightTrigger > 0,            // the right trigger
                    () -> leftTrigger > 0              // is pressed
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // if it's pressed, set the multiplier to 1.0: full
                    // speed!
                    mult.set(1.0);
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.trueThenAllFalse( // make sure ONLY
                    () -> leftTrigger > 0,             // the left trigger
                    () -> rightTrigger > 0             // is pressed
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // if it's pressed, set the multiplier to 0.25: very
                    // slow, for precise movement.
                    mult.set(0.25);
                }
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> SupplierFilter.allFalse( // make sure BOTH of the
                    () -> rightTrigger > 0,    // triggers are NOT pressed
                    () -> leftTrigger > 0
                ),
                (isPressed) -> isPressed,
                (isPressed) -> {
                    // the default multiplier is 0.5
                    mult.set(0.5);
                }
            );

        while (true)
            pathfinder.tick();
    }
}
```

Please note that this is NOT the best implementation of a multiplier-like
concept. It's rather verbose and can be simplified greatly.

## More advanced bindings
You can do some pretty cool and pretty advanced bindings using the
following class: `me.wobblyyyy.pathfinder2.utils.SupplierFilter`.

### Requiring multiple buttons to be pressed
This binding will only be activated if the A and B buttons are pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.allTrue(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton  // assume bButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

### Requiring one button to be pressed and other buttons not pressed
This binding will only be activated if the A button is pressed, and the
B, X, and Y buttons are not pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.trueThenAllFalse(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton, // assume bButton is a Supplier<Boolean>
            this::xButton, // assume xButton is a Supplier<Boolean>
            this::yButton  // assume yButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

### Requiring any condition to be true
This binding will be activated whenever either the A, B, X, or Y button
is pressed.
```java
pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.anyTrue(
            this::aButton, // assume aButton is a Supplier<Boolean>
            this::bButton, // assume bButton is a Supplier<Boolean>
            this::xButton, // assume xButton is a Supplier<Boolean>
            this::yButton  // assume yButton is a Supplier<Boolean>
        ),
        (isPressed) -> isPressed,
        (isPressed) -> {
            // both the a and b buttons must be pressed
        }
    );
```

### Binding a speed multiplier
```java
// assume these are declared elsewhere for the sake of demonstration
Supplier<Boolean> rightTrigger;
Supplier<Boolean> leftTrigger;

// must be effectively final to use from within lambdas
AtomicReference<Double> multiplier = new AtomicReference(0d);

pathfinder.getListenerManager()
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        () -> SupplierFilter.allFalse(
            rightTrigger,
            leftTrigger
        )
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(0.5);
    )
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        rightTrigger,
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(1.0);
    )
    .bind(
        ListenerMode.CONDITION_NEWLY_MET,
        leftTrigger,
        (isPressed) -> isPressed,
        (isPressed) -> multiplier.set(0.25);
    );
```

### A simple shifter
A shifter is a pretty simple concept - you can either shift up or down.
Pressing the right trigger will shift upwards, and pressing the left trigger
will shift downwards. This shifter doesn't actually do very much - it allows
you to shift up and down, but it doesn't do anything with the gear. Whenever
you press the A button, this will log the current gear to the standard output.
```java
Supplier<Boolean> rightTrigger;
Supplier<Boolean> leftTrigger;
Supplier<Boolean> aButton;

Shifter shifter = new Shifter(1, 1, 5, false, (pf) -> {});

pathfinder.getListenerManager()
    .bindNewlyMet(
        () -> SupplierFilter.trueThenAllFalse(rightTrigger, leftTrigger),
        (isPressed) -> isPressed,
        (isPressed) -> shifter.shift(ShifterDirection.UP)
    )
    .bindNewlyMet(
        () -> SupplierFilter.trueThenAllFalse(leftTrigger, rightTrigger),
        (isPressed) -> isPressed,
        (isPressed) -> shifter.shift(ShifterDirection.DOWN)
    )
    .bindButton(
        aButton,
        (isPressed) -> System.out.printf("current gear: %s%n", shifter.getCurrentGear())
    );
```
