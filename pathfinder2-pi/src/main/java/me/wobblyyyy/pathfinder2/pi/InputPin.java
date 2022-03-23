package me.wobblyyyy.pathfinder2.pi;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.PullResistance;

public class InputPin {
    private final DigitalInputConfig config;
    private final DigitalInput input;

    public InputPin(
        Context context,
        String id,
        String name,
        int address,
        PullResistance pullResistance,
        long debounce,
        String provider
    ) {
        this.config =
            DigitalInput
                .newConfigBuilder(context)
                .id(id)
                .name(name)
                .address(address)
                .pull(pullResistance)
                .debounce(debounce)
                .provider(provider)
                .build();

        this.input = context.create(this.config);
    }

    public DigitalInput getInput() {
        return input;
    }
}
