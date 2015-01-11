package de.hochschuletrier.gdw.commons.gdx.audio;

import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE;
import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE_CLAMPED;

/**
 * Sound distance models available
 * OpenAL backend required.
 *
 * @author Santo Pfingsten
 */
public enum SoundDistanceModel {

    LINEAR(AL_LINEAR_DISTANCE),
    LINEAR_CLAMPED(AL_LINEAR_DISTANCE_CLAMPED),
    EXPONENT(AL_EXPONENT_DISTANCE),
    EXPONENT_CLAMPED(AL_EXPONENT_DISTANCE_CLAMPED),
    INVERSE(AL_INVERSE_DISTANCE),
    INVERSE_CLAMPED(AL_INVERSE_DISTANCE_CLAMPED);

    private final int model;

    SoundDistanceModel(int model) {
        this.model = model;
    }

    public void activate() {
        alDistanceModel(model);
    }
}
