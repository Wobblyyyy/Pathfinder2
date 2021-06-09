package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Translation;

public interface Drive {
    Translation getTranslation();
    void setTranslation(Translation translation);
}
