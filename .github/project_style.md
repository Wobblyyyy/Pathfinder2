# Code Style
Ahh. Everyone's favorite topic. Not really, but yeah. Code style. All the code I write tends to adhere to the same
simple style guidelines, but the issue is, nobody knows what they are except for me, because I've never written them
down somewhere. Don't worry, I'll try to keep this short.

# General Code Style
There's not a lot of rules for how code here should look. Code is supposed to do one thing - function. Good code,
however, is supposed to function and be easy to maintain and understand.

## Comment Style
There are three types of comments. Styling them can be important.

### End-of-line Comments
```java
//bad example (no spacing)
// good example (nice spacing)
```

### JavaDoc Comments
```java
/**
 *bad example (no spacing)
 */

/**
 * good example (nice spacing)
 */
```

### C-style Comments
```java
/*bad example (no spacing)*/

/*
*bad example (not lined up, no spacing)        
 */

/* good example (clear spacing, this shouldn\'t really be used tho) */

/*
 * good example
 * (clear spacing)
 */
```

## Choosing Readable Identifiers
Avoid shortening words as much as possible. Unless there's a widely-used abbreviation (such as USB/Universal Serial
Bus) you should spell each word out to maximize the clarity of what things are supposed to represent.

Choose names that accurately describe the functionality of that identifier. Ex:

### Bad Identifiers
```java
public class Identifiers {
    /*
     * Doesn't describe the unit the time is measured in.
     * Doesn't describe what the time value actually means.
     * 
     * A better name would be elapsedTimeMilliseconds, as it clearly expresses the unit
     * and the purpose of the field.
     */
    public double time;

    /*
     * Isn't very descriptive. Booleans should usually be prefixed with "is".
     * 
     * A better name would be isEnabled.
     */
    public boolean enabled;

    /*
     * Confusing and impossible to understand without reading into the code.
     * Useless parameter name.
     * 
     * A better name is demonstrated below.
     */
    public void update(double ms) {
        this.time = ms;
    }
}
```

### Good Identifiers 
```java
public class Identifiers {
    /*
     * Describes the unit of time and the purpose of that measurement. 
     * Uses complete words - you can't not understand what it says.
     */
    public double elapsedTimeMilliseconds;

    /*
     * Uses "is" before the boolean to indicate a state. 
     */
    public boolean isEnabled;

    /*
     * Descriptive method name.
     * Descriptive parameter name.
     */
    public void updateElapsedTime(double elapsedTimeMilliseconds) {
        this.elapsedTimeMilliseconds = elapsedTimeMilliseconds;
    }
}
```

# Java
Any code written in Java only needs to follow a couple style guidelines. To be honest, you can probably figure out what
most of those are by just reading some source code.

## Line Length
Alright. This is the only one I'm really focused on. The rest of them? It's okay if you don't space something exactly
how I would have. But I would *strongly prefer* if you were to adhere to these guidelines.

Lines should be no longer than 80 characters long, unless splitting the line would significantly decrease readability.
In other words, just try to keep your line lengths below 80 characters. It's really annoying trying to split-screen
two different editors and not being able to read a line because it's incredibly long. So yeah.
```java
public class Demo {
    /*
     * Here's an example of a bad line (over 80 characters).
     */
    public static double multiply(double a, double b, double c, double d, double e, double f) {
        return a * b * c * d * e * f;
    }
    
    /*
     * And here's an example of a good line (under 80 characters).
     */
    public static double multiply(double a,
                                  double b,
                                  double c,
                                  double d,
                                  double e,
                                  double f) {
        return a * b * c * d * e * f;
    }
}
```

Also - related - method parameters. Parameters *should* get their own lines, but I'm okay if they don't. Just make
sure nothing runs over 80 characters unless there's some seriously good reasoning why it can.

## Adequate Spacing
Make sure there's enough spacing around everything.

You should NOT do this.
```java
public class Demo {
    public static double multiply(double a,double b){
        return a*b;
    }
}
```

You should do this, instead.
```java
public class Demo {
    public static double multiply(double a,
                                  double b) {
        return a * b;
    }
}
```

If you're using an editor such as IntelliJ's IDEA, this formatting will probably be done automatically for you. If
you didn't know, you can press Ctrl+Alt+L to automatically reformat the code in whatever file you're currently working
on. It's pretty helpful, and you should probably do it.

## Comments
There's a couple different types of comments. I'd encourage you to document everything as well as you can, because
there's really no harm in doing so, aside from increasing file size - but really - is a couple extra kB going to hurt
anybody? No. That's what I thought.

### JavaDoc Comments
JavaDoc comments should adhere to all of Oracle's JavaDoc style guidelines with one exception. Oracle doesn't want you
to use end tags, but we do. Example:
```java
public class Demo {
    /**
     * This comment is done how Oracle officially suggests.
     * 
     * <p>
     * Here's a new paragraph, without an end tag.
     */
    public void oracle() {
        
    }

    /**
     * This comment is a real rule-breaker here.
     * 
     * <p>
     * Here's a new paragraph, WITH an end tag. Crazy.
     * </p>
     */
    public void notOracle() {
        
    }
}
```

### In-Method Comments
If you're documenting a method, you should use line comments, as so.
```java
public class Demo {
    public double veryConfusingAndComplexMath(double a,
                                              double b) {
        // This math is really hard, so bear with me.
        double c = a + 1;
        double d = b + 1;
        
        // Okay. Here's the hard part.
        return c + d;
    }
}
```

Of course, that code is totally useless in every way, shape and form. But you get the point.

### In-Class Comments
If you're writing a non-JavaDoc comment that's just hanging around inside a class or interface, you should style it
as follows.
```java
public class Demo {
    /*
     * Here's a comment that has absolutely nothing to do with anything
     * in the code! But you get the styling, right? Block comments. 
     * 
     * Each new paragraph/idea should get a line in-between it and the
     * last paragraph/idea. 
     */
    
    public void coolStuff() {
        
    }
}
```

### Putting it all together - a complete comment
Here's an example of our `Demo` class, but it's commented appropriately. Once again - all of this is totally useless
code, and the only purpose of including it is to demonstrate how comments should be formatted.
```java
/**
 * A very useful demonstration class used for doing very useful demonstration
 * things. All of this is very real and useful code.
 * 
 * <p>
 * Here's another paragraph, just for fun. It's cool, right?
 * </p>
 * 
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Demo {
    /**
     * Perform some very confusing and complex math using two numbers.
     * 
     * <p>
     * This is very confusing and complex, so it's okay if you don't get
     * it. Here are the steps.
     * <ul>
     *     <li>Do some confusing math.</li>
     *     <li>Do some complex math.</li>
     *     <li>That's it.</li>
     * </ul>
     * </p>
     * 
     * @param a one of the two numbers to do confusing math with.
     * @param b one of the two numbers to do confusing math with.
     * @return the result of some complex math.
     */
    public double veryConfusingAndComplexMath(double a,
                                              double b) {
        // This math is really hard, so bear with me.
        double c = a + 1;
        double d = b + 1;

        // Okay. Here's the hard part.
        return c + d;
    }
}
```

## Method Definitions
Methods should have each of their parameters on a separate line. This is done for two reasons - it improves
readability, and it helps code to be under 80 characters in length.

### Bad Method Definition
```java
public class MethodDefinitions {
    public void add(double a, double b, double c) {
        return a + b + c;
    }
}
```

### Good Method Definition
```java
public class MethodDefinitions {
    public void add(double a,
                    double b,
                    double c) {
        return a + b + c;
    }
}
```
