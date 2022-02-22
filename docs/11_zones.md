# Zones
Zones are a feature of Pathfinder that's generally not used outside of very
specific cases. They allow you to attach functionality to your robot's motion.
If your robot enters a certain zone, you can have it perform a certain action.
Likewise, if your robot exits a certain zone, or if it's inside the zone, or
even if it's not inside the zone, you can perform some certain action.

## Shapes
Zones are based on `Shape`s, which come from the more "advanced" portion of
features of the `pathfinder2-geometry` module. Here's an example of a simple
shape - a rectangle.
```java
Shape<?> rectangle = new Rectangle(0, 0, 10, 10);
```

There's much more you can do with shapes, and I'll provide a link to
`Shape` documentation here whenever I end up writing it.

## Creating a basic zone
Zones aren't too hard to create: take a look.
```java
Rectangle rectangle = new Rectangle(0, 0, 10, 10);
Zone zone = new Zone(rectangle);
```

There we go! We got a zone. What can we do with it, you may be asking?
By overriding some methods from the `Zone` class, we can add bindings for
certain conditions.
```java
Rectangle rectangle = new Rectangle(0, 0, 10, 10);
Zone zone = new Zone(rectangle) {
    @Override
    public void onEnter(Pathfinder pathfinder) {
        System.out.println("the robot just entered the zone!");
    }

    @Override
    public void onExit(Pathfinder pathfinder) {
        System.out.println("the robot just exited the zone!");
    }

    @Override
    public void whileInside(Pathfinder pathfinder) {
        System.out.println("the robot is currently inside the zone!");
    }
};
```

Of course, that's not all you can do by overriding these methods - you can
do just about anything! How inspiring. Bet you weren't expecting that, huh?
Reading code documentation and suddenly being given a motivational speech
about how you can do anything you put your mind to?

Now: how can we have these zones actually be processed? With the help of
a `ZoneProcessor`, of course. Conveniently enough, every instance of
`Pathfinder` comes with an instance of `ZoneProcessor`. You can add a zone
to the zone processor like so:
```java
// method 1
pathfinder.addZone("cool zone name", zone);

// method 2
pathfinder.getZoneProcessor().addZone("cool zone name", zone);
```

Everything should be good from here on out.
