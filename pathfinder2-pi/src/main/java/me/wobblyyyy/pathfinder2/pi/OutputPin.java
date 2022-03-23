package me.wobblyyyy.pathfinder2.pi;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;

public class OutputPin {
    private final DigitalOutputConfig config;
    private final DigitalOutput output;

    public OutputPin(
        Context context,
        String id,
        String name,
        int address,
        DigitalState shutdownState,
        DigitalState initialState,
        String provider
    ) {
        this.config =
            DigitalOutput
                .newConfigBuilder(context)
                .id(id)
                .name(name)
                .address(address)
                .shutdown(shutdownState)
                .initial(initialState)
                .provider(provider)
                .build();

        this.output = context.create(this.config);
    }

    public DigitalOutput getOutput() {
        return output;
    }
}
