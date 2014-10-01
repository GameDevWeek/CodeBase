package de.hochschuletrier.gdw.commons.gdx.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Extended animation with variable frame durations. If no variable frame
 * durations are set, default frame durations will be used.
 *
 * @author David Neubauer
 */
public class AnimationWithVariableFrameTime extends Animation {

    private float frameDurations[] = null;

    /**
     * Constructor, storing the frame duration and key frames.
     *
     * @param defaultFrameDuration the time between frames in seconds.
     * @param keyFrames the {@link TextureRegion}s representing the frames.
     */
    public AnimationWithVariableFrameTime(float defaultFrameDuration, Array<? extends TextureRegion> keyFrames) {
        super(defaultFrameDuration, keyFrames);
    }

    /**
     * Constructor, storing the frame duration, key frames and play type.
     *
     * @param defaultFrameDuration the time between frames in seconds.
     * @param keyFrames the {@link TextureRegion}s representing the frames.
     * @param playMode the animation playback mode.
     */
    public AnimationWithVariableFrameTime(float defaultFrameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(defaultFrameDuration, keyFrames, playMode);
    }

    /**
     * Constructor, storing the frame duration and key frames.
     *
     * @param defaultFrameDuration the time between frames in seconds.
     * @param keyFrames the {@link TextureRegion}s representing the frames.
     */
    public AnimationWithVariableFrameTime(float defaultFrameDuration, TextureRegion... keyFrames) {
        super(defaultFrameDuration, keyFrames);
    }

    /**
     * Will override the frameDuration. Use this only if you need different
     * frame lengths. Can be set to NULL.
     *
     * @param frameDurations the durantion for every frame in seconds.
     */
    public void setFrameDurations(float frameDurations[]) {
        if (frameDurations == null) {
            this.frameDurations = null;
            return;
        }
        if (frameDurations.length != super.getKeyFrames().length) {
            throw new RuntimeException("Need as many FrameDurations ("
                    + frameDurations.length
                    + ") as there are frames("
                    + super.getKeyFrames().length
                    + ")!");
        }
        this.frameDurations = frameDurations;
    }

    public float[] getFrameDurations() {
        return frameDurations;
    }

    /**
     * Returns the current frame number.
     *
     * @param stateTime
     * @return current frame number
     */
    @Override
    public int getKeyFrameIndex(float stateTime) {
        if (frameDurations == null) {
            return super.getKeyFrameIndex(stateTime);
        }

        float elapsedTime = 0;
        int frameNumber = 0;

        elapsedTime += frameDurations[frameNumber];
        while (elapsedTime < stateTime) {
            frameNumber = (frameNumber + 1) % frameDurations.length;
            elapsedTime += frameDurations[frameNumber];
        }

        switch (super.getPlayMode()) {
            case NORMAL:
                frameNumber = (elapsedTime > this.getAnimationDuration()) ? super.getKeyFrames().length - 1 : frameNumber;
                break;
            case LOOP:
                frameNumber = frameNumber % super.getKeyFrames().length;
                break;
            case LOOP_PINGPONG:
                throw new RuntimeException("Not implemented");
            case LOOP_RANDOM:
                throw new RuntimeException("Not implemented");
            case REVERSED:
                throw new RuntimeException("Not implemented");
            case LOOP_REVERSED:
                throw new RuntimeException("Not implemented");
        }
        return frameNumber;
    }

    /**
     * @return the duration of the entire animation, number of frames times
     * frame duration, in seconds
     */
    @Override
    public float getAnimationDuration() {
        float duration = 0;
        for (float f : frameDurations) {
            duration += f;
        }
        return duration;
    }
}
