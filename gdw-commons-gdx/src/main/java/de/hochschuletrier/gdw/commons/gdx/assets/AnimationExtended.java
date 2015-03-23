package de.hochschuletrier.gdw.commons.gdx.assets;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationExtended {

    private static final Logger logger = LoggerFactory.getLogger(AnimationExtended.class);

    public enum PlayMode {

        NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM
    }

    final TextureRegion[] keyFrames;
    Frame[] frameTimes;
    public float animationDuration = 0f;
    private final PlayMode playMode;
    TreeMap<Frame, Integer> frames = new TreeMap();
    Frame current = new Frame(0, 0);

    public AnimationExtended(PlayMode playMode, float[] frameDurations,
            TextureRegion... keyFrames) {
        this.keyFrames = keyFrames;
        this.playMode = playMode;
        int index = 0;

        for (TextureRegion t : keyFrames) {
            t.flip(false, true);
        }

        while(index<keyFrames.length) {
            for (float f : frameDurations) {
                frames.put(new Frame(animationDuration, f), index);
                animationDuration += f;
                ++index;
                if(index >= keyFrames.length)
                    break;
            }
        }
    }

    public TextureRegion getKeyFrame(float stateTime) {
        int frameNumber = getKeyFrameIndex(stateTime);
        if (keyFrames[frameNumber] == null) {
            logger.warn("No keyframe at stateTime, returning nullpointer");
        }
        return keyFrames[frameNumber];
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public int getKeyFrameIndex(float stateTime) {
        if (keyFrames.length == 1) {
            return 0;
        }

        current.startTime = stateTime;

        Entry<Frame, Integer> fetchedEntry = frames.floorEntry(current);
        int frameNumber = fetchedEntry.getValue();
        switch (playMode) {
            case NORMAL:
                frameNumber = Math.min(frames.keySet().size() - 1, frameNumber);
                break;
            case LOOP:
                current.startTime = stateTime % animationDuration;
                // sucht höchst kleinsten key zu current
                frameNumber = frames.floorEntry(current).getValue();
                // frameNumber = frames.floorEntry(current).getValue();
                break;
            case LOOP_PINGPONG:
                current.startTime = stateTime % (animationDuration * 2);
                fetchedEntry = frames.floorEntry(current);
                frameNumber = fetchedEntry.getValue();
                if (current.startTime >= animationDuration) {
                    Frame backFrame = new Frame(current.startTime, 0);
                    backFrame.startTime = current.startTime - animationDuration;
                    fetchedEntry = frames.floorEntry(backFrame);
                    frameNumber = fetchedEntry.getValue();

                }
                break;
            case LOOP_RANDOM:
                frameNumber = MathUtils.random(keyFrames.length - 1);
                break;
            case REVERSED:
                current.startTime = (animationDuration - stateTime) % animationDuration;
                current.startTime = Math.max(current.startTime, 0);
                frameNumber = frames.floorEntry(current).getValue();
                break;
            case LOOP_REVERSED:
                current.startTime = animationDuration - (stateTime % animationDuration);
                frameNumber = frames.floorEntry(current).getValue();
                // frameNumber = keyFrames.length - frameNumber - 1;
                break;

            default:
                // play normal otherwise
                frameNumber = Math.min(keyFrames.length - 1, frameNumber);
                break;
        }

        return frameNumber;
    }

    class Frame implements Comparable<Frame> {

        float startTime;
        float duration;

        public Frame(float startTime, float duration) {
            this.startTime = startTime;
            this.duration = duration;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Frame)) {
                return false;
            }
            Frame f = (Frame) obj;
            return f.startTime >= this.startTime
                    && f.startTime < this.duration + this.startTime;
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            hashCode = 17 * hashCode + Float.floatToIntBits(this.startTime);
            hashCode = 17 * hashCode + Float.floatToIntBits(this.duration);
            return hashCode;
        }

        protected void setEntry(float val) {
            startTime = val;
        }

        @Override
        public String toString() {
            return "[" + startTime + ", " + duration + "]";
        }

        /*
         * Compares this object with the specified object for order. Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         */
        @Override
        public int compareTo(Frame f) { // this ist current selber
            if (f.equals(this)) {// current im bereich
                return 0;
            }
            if (this.startTime + this.duration > f.startTime + f.duration) { // current
                return +1;
            }
            if (this.startTime + this.duration < f.startTime + f.duration) {
                return -1;
            }
            return 1;
        }
    }
}
