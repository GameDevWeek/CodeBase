package de.hochschuletrier.gdw.commons.gdx.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import java.util.ArrayList;
import java.util.Iterator;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

/**
 * Like a speaker in the world, can be moved.
 * OpenAL backend required.
 *
 * @author Santo Pfingsten
 */
public class SoundEmitter implements Disposable {

    public enum Mode {

        STEREO,
        MONO;
    }

    static boolean muted;
    static float globalVolume = 1;
    private static float WORLD_SCALE = 1 / 1000.0f;
    private static final SoundEmitter globalEmitter = new SoundEmitter();

    protected static final Pool<SoundInstance> pool = new ReflectionPool(SoundInstance.class);
    protected final ArrayList<SoundInstance> instances = new ArrayList();
    protected static Mode mode = Mode.STEREO;
    private static final Vector3 listenerPosition = new Vector3();

    public static void setListenerPosition(float x, float y, float z, Mode mode) {
        listenerPosition.set(x, y, z);
        SoundEmitter.mode = mode;
        if (mode == Mode.STEREO) {
            alListener3f(AL_POSITION, x * WORLD_SCALE, y * WORLD_SCALE, z * WORLD_SCALE);
        } else {
            alListener3f(AL_POSITION, 0, 0, 0);
        }
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void setMuted(boolean muted) {
        SoundEmitter.muted = muted;
    }

    public static float getGlobalVolume() {
        return globalVolume;
    }

    public static void setGlobalVolume(float globalVolume) {
        SoundEmitter.globalVolume = globalVolume;
    }

    /**
     * Make this value smaller if you feel your virtual head is too small.
     * i.e. if a sound moving from left ear to right ear has too little cross-fading
    
     * @param scale the scale by which all incoming coordinates (and distances) are scaled, default is 1/1000
     */
    public final static void setWorldScale(float scale) {
        WORLD_SCALE = scale;
    }

    public final static float getWorldScale() {
        return WORLD_SCALE;
    }

    public final static void updateGlobal() {
        globalEmitter.update();
    }

    public final static SoundInstance playGlobal(Sound sound, boolean loop) {
        SoundInstance si = globalEmitter.play(sound, loop);
        si.setPosition(listenerPosition.x, listenerPosition.y, listenerPosition.z);
        return si;
    }

    public final static SoundInstance playGlobal(Sound sound, boolean loop, float x, float y, float z) {
        SoundInstance si = globalEmitter.play(sound, loop);
        si.setPosition(x, y, z);
        return si;
    }

    public final static void disposeGlobal() {
        globalEmitter.dispose();
    }

    public void update() {
        Iterator<SoundInstance> it = instances.iterator();
        while (it.hasNext()) {
            SoundInstance si = it.next();
            if (si.isStopped()) {
                it.remove();
                pool.free(si);
            } else {
                si.setVolume(si.volume);
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        if (mode == Mode.STEREO) {
            for (SoundInstance si : instances) {
                si.setPosition(x, y, z);
            }
        } else {
            float distance = listenerPosition.dst(x, y, z);

            for (SoundInstance si : instances) {
                si.setPosition(0, 0, distance);
            }
        }
    }

    public SoundInstance play(Sound sound, boolean loop) {
        SoundInstance si = pool.obtain();
        si.init(sound, loop);
        instances.add(si);
        return si;
    }

    @Override
    public void dispose() {
        for (SoundInstance si : instances) {
            si.stop();
            pool.free(si);
        }
        instances.clear();
    }
}
