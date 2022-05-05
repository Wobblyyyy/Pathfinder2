# Recording
Recording is possible using two methods:
- `StateRecorder` (suggested)
- `MovementRecorder` (not suggested)

As of now, there is only documentation on using `StateRecorder`, as
`MovementRecorder` is relatively ineffective outside of very specific use
cases.

## Using `StateRecorder`
Your `Pathfinder` instance has a `StateRecorder` you can use like so:
```java
Pathfinder pathfinder; // assume this is initialized elsewhere

StateRecorder recorder = pathfinder.getRecorder();
```

If you don't want to go through `Pathfinder`, you can instantiate your own
`StateRecorder` like so. Just remember: if you're not using `Pathfinder`'s
`tick()` method, you'll need to update the `StateRecorder` yourself.
```java
Pathfinder pathfinder; // assume this is initialized elsewhere

StateRecorder recorder = new StateRecorder();
```

### How does state recording work?
State recording, as the name would imply, records the state of the robot. It
does this by making use of the `Recordable` interface. The idea here is that
anything implementing `Recordable` should be able to be recorded and fed
a recording in order to recreate that recording. Some parts of Pathfinder
already implement the `Recordable` interface: for example, take...
- `Motor`
- `Servo`

Each recording is simply a sequence of records. A record contains all of the
information about the current state of the system.

As an example: say you have a tank drive robot with 2 motors. If you were
to record the "state" of the robot (remember, it has 2 motors), you'd simply
record the power values of each of those motors. In order to play that
recording back, you'd just set those power values to the motors.

### Adding `Recordable`s
In order to record anything, you'll first need to add some `Recordable`s to
the `StateRecorder`'s internal map.
```java
Pathfinder pathfinder; // assume this is initialized elsewhere

StateRecorder recorder = pathfinder.getRecorder();

Motor rightMotor; // assume this is initialized elsewhere
Motor leftMotor; // assume this is initialized elsewhere

// each "node" has two components:
// - a String name
// - a Recordable object that can be recorded
recorder.putNode("rightMotor", rightMotor);
recorder.putNode("leftMotor", leftMotor);
```

Notice how the `putNode` method requires a `String` parameter - the name of
the node. When storing a recording and playing it back later, the recording
will need to be encoded into some sort of file - in the case of Pathfinder,
JSON is preferred. In order to decode this file, and make the robot recreate
the recording, the file will need to be decoded. In order to replay a
recording, these names will be used to access the objects.

#### IMPORTANT NOTE ABOUT ADDING RECORDABLES
You will need to add these `Recordable`s to the `StateRecorder` in order to
record OR play back. Thus, it's suggested you add these `Recordable`s to your
`StateRecorder` during Pathfinder's initialization.

### Creating a recording
Creating a recording is relatively straightforward. You just need two
parameters:
```java
StateRecorder recorder; // assume this is initialized elsewhere

int RECORDING_INTERVAL_MS = 50;
int ESTIMATED_RECORDING_LENGTH_MS = 10_000;

recorder.startRecording(
    RECORDING_INTERVAL_MS,
    ESTIMATED_RECORDING_LENGTH_MS
);
```

#### Recording interval
The interval, in milliseconds, between each capture of the robot's state. A
higher recording interval will reduce the accuracy of the recording while also
reducing the size of the outputted recording. A larger recording interval will
increase the accuracy of the recording and increase the size of the outputted
recording. If you find that your recordings are too large, try either
increasing the recording interval, or shortening your recordings.

A good starting place for a recording interval is 100 milliseconds. That means
you'll get 10 captures per second. If you find this is too inaccurate, try
a recording interval of 50, or even 25 - whatever floats your boat.

#### Estimated recording length
This parameter is used to allocate an array for storing records. The
performance penalty you'd get from making this value too small is negligible.

### Saving your recording
Say you've done all the stuff you want to do in your recording and now it's
time to do some other stuff in your code. To stop a recording, use the aptly
named `stopRecording` method. This will return a `StateRecording`, which you
can then save to play back later.
```java
StateRecorder recorder; // assume this is initialized elsewhere

int RECORDING_INTERVAL_MS = 50;
int ESTIMATED_RECORDING_LENGTH_MS = 10_000;

recorder.startRecording(
    RECORDING_INTERVAL_MS,
    ESTIMATED_RECORDING_LENGTH_MS
);

// this will call recorder.update() in a loop for 10 seconds
// you MUST call update, otherwise nothing will happen!
// note that if you're using a StateRecorder that came from a Pathfinder
// instance, Pathfinder's tick() will take care of this for you
Time.runFor(10_000, () -> recorder.update());

StateRecording recording = recorder.stopRecording();
```

### Playing back a recording
Alright, time to play back that lovely recording. Hint: it's pretty easy.
```java
StateRecorder recorder; // assume this is initialized elsewhere

recorder.startRecording(50, 10_000);

Time.runFor(10_000, () -> recorder.update());

StateRecording recording = recorder.stopRecording();

recorder.startPlayback(recording);

Time.runFor(10_000, () -> recorder.update());
```

### Prematurely stopping playback
If, for whatever reason, you'd like to prematurely stop playback, simply
use the `stopPlayback` method, like so:
```java
StateRecorder recorder; // assume this is initialized elsewhere
StateRecording recording; // assume this is initialized elsewhere

recorder.startPlayback(recording);
recorder.stopPlayback();
```

This will stop playing the recording back. There is no way to simply pause
the recording - you'll have to start from the beginning of the recording.
