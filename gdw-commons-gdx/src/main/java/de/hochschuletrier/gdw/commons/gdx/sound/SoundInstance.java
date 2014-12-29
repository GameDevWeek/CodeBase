package de.hochschuletrier.gdw.commons.gdx.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Pool;
import java.lang.reflect.Field;
import static org.lwjgl.openal.AL10.*;

/**
 * Control a sound once it was played and get more info from it than gdx supplies.
 * OpenAL backend required.
 *
 * @author Santo Pfingsten
 */
public class SoundInstance implements Pool.Poolable {

    Sound sound;
    long id;
    int sourceId;

    void init(Sound sound, boolean loop) {
        this.sound = sound;
        this.id = loop ? sound.loop() : sound.play();
        try {
            Field field = OpenALAudio.class.getDeclaredField("soundIdToSource");
            field.setAccessible(true);
            LongMap<Integer> map = (LongMap<Integer>) field.get(Gdx.audio);
            sourceId = map.get(id);
        } catch (Exception e) {
            throw new RuntimeException("Error getting soundIdToSource", e);
        }
        setReferenceDistance(50);
        alSourcef(sourceId, AL_GAIN, 1.0f);
    }

    @Override
    public void reset() {
        sound = null;
        id = 0;
        sourceId = 0;
    }

    public void setReferenceDistance(float value) {
        alSourcef(sourceId, AL_REFERENCE_DISTANCE, value * SoundEmitter.getWorldScale());
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public boolean isPaused() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PAUSED;
    }

    public boolean isStopped() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_STOPPED;
    }

    public void stop() {
        sound.stop(id);
    }

    public void pause() {
        sound.pause(id);
    }

    public void resume() {
        sound.resume(id);
    }

    public void setVolume(float volume) {
        sound.setVolume(id, volume);
    }

    public void setPan(float pan, float volume) {
        sound.setPan(id, pan, volume);
    }

    public void setPitch(float pitch) {
        sound.setPitch(id, pitch);
    }

    public void setLooping(boolean looping) {
        sound.setLooping(id, looping);
    }

    public void setPriority(int priority) {
        sound.setPriority(id, priority);
    }

    void setPosition(float x, float y, float z) {
        float scale = SoundEmitter.getWorldScale();
        alSource3f(sourceId, AL_POSITION, x * scale, y * scale, z * scale);
    }
}
