package de.hochschuletrier.gdw.commons.gdx.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ReflectionPool;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Music Manager
 *
 * @author Santo Pfingsten
 */
public class MusicManager {

    private static boolean muted;
    private static float globalVolume = 1;
    private static Music currentMusic;
    private static final LinkedList<Fade> fades = new LinkedList();
    private static final ReflectionPool<Fade> pool = new ReflectionPool(Fade.class);

    private static class Fade implements Poolable {

        public Music music;
        public float time;
        public float totalTime;
        public boolean fadeOut;

        @Override
        public void reset() {
            music = null;
            time = 0;
            totalTime = 0;
            fadeOut = false;
        }

        private boolean update(float deltaTime) {
            time += deltaTime;
            if (time >= totalTime) {
                time = totalTime;
                if (fadeOut) {
                    music.stop();
                    return true;
                }
                music.setVolume(muted ? 0 : globalVolume);
            } else {
                if (muted) {
                    music.setVolume(0);
                } else if (fadeOut) {
                    music.setVolume(globalVolume * (1 - (time / totalTime)));
                } else {
                    music.setVolume(globalVolume * (time / totalTime));
                }
            }
            return false;
        }
    }

    private static Fade getFade(Music music) {
        for (Fade fade : fades) {
            if (fade.music == music) {
                return fade;
            }
        }
        return null;
    }

    private static void addFade(Music music, float fadeTime, boolean fadeOut) {
        Fade fade = getFade(music);
        if (fade != null) {
            fade.time = (fade.time / fade.totalTime) * fadeTime;
            fade.totalTime = fadeTime;
            fade.fadeOut = fadeOut;
        } else {
            fade = pool.obtain();
            fade.music = music;
            fade.totalTime = fadeTime;
            fade.fadeOut = fadeOut;
            fade.update(0);
            fades.add(fade);
        }
    }

    public static void play(Music music, float fadeTime) {

        if (currentMusic != null && currentMusic != music) {
            addFade(currentMusic, fadeTime, true);
        }

        if (music != null) {
            addFade(music, fadeTime, false);
            music.setLooping(true);
            music.play();
        }
        currentMusic = music;
    }

    public static void stop() {
        for (Fade fade : fades) {
            fade.music.stop();
            pool.free(fade);
        }
        fades.clear();
    }

    public static void update(float deltaTime) {
        for (Iterator<Fade> iterator = fades.iterator(); iterator.hasNext();) {
            Fade fade = iterator.next();
            if (fade.update(deltaTime)) {
                if (fade.music == currentMusic) {
                    currentMusic = null;
                }
                pool.free(fade);
                iterator.remove();
            }
        }
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void setMuted(boolean muted) {
        MusicManager.muted = muted;
    }

    public static float getGlobalVolume() {
        return globalVolume;
    }

    public static void setGlobalVolume(float globalVolume) {
        MusicManager.globalVolume = globalVolume;
    }
}
