/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.wpilib;

import edu.wpi.first.wpilibj.Encoder;
import me.wobblyyyy.pathfinder2.robot.sensors.AbstractEncoder;

/**
 * Wrapper for wpilib's {@link Encoder}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPIEncoder extends AbstractEncoder {
    private final Encoder encoder;
    private final boolean isRaw;

    /**
     * Create a new {@code WPIEncoder}.
     *
     * @param encoder the encoder to use.
     * @param isRaw   is the encoder in raw mode? If the encoder is in raw
     *                mode, it will not use scaling. To be honest, I'm not
     *                really too sure what that means, but it's included in
     *                WPI's encoder, so I'm including it here too. You
     *                know the vibe.
     */
    public WPIEncoder(Encoder encoder, boolean isRaw) {
        this.encoder = encoder;
        this.isRaw = isRaw;
    }

    /**
     * Create a new {@code WPIEncoder}.
     *
     * @param encoder the encoder to use.
     */
    public WPIEncoder(Encoder encoder) {
        this(encoder, false);
    }

    /**
     * Create a new {@code WPIEncoder}.
     *
     * @param encoderChannelA  one of the encoder's channels.
     * @param encoderChannelB  one of the encoder's channels.
     * @param reverseDirection should the encoder's direction be reversed?
     */
    public WPIEncoder(
        int encoderChannelA,
        int encoderChannelB,
        boolean reverseDirection
    ) {
        this(new Encoder(encoderChannelA, encoderChannelB, reverseDirection));
    }

    @Override
    public int getRawTicks() {
        return isRaw ? encoder.getRaw() : encoder.get();
    }
}
