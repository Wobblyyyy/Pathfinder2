# Making a tank drive
A tank drive (also known as a "differential drive") is a type of drivetrain
that has "right" and "left" speeds, much like an actual tank. The right and
left sides of the drivetrain are controlled separately. This guide (or bit of
documentation, I guess) covers the following:
- Concept behind a tank drive
- Making a tank drive controllable via joysticks
- Making a tank drive controllable via `Translation`s
- Tracking the position by using odometry

## What's a tank drive?
> Many mobile robots use a drive mechanism known as differential drive. It
> consists of 2 drive wheels mounted on a common axis, and each wheel can
> independently driven forward or backward... By varying the velocities of
> the two wheels, we can vary the trajectories that the robot takes.
> Because the rate of rotation (source:
> [Columbia](http://www.cs.columbia.edu/~allen/F17/NOTES/icckinematics.pdf))

![Tank](https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.army-technology.com%2Fprojects%2Foplot-m-main-battle-tank-ukraine%2F&psig=AOvVaw1zb_woxS3uDGClYprgmfE0&ust=1650725861488000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCJCYzpb3p_cCFQAAAAAdAAAAABBY)
