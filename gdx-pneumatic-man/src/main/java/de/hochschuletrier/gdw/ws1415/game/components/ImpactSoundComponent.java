package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.utils.Timer;

public class ImpactSoundComponent extends Component implements Pool.Poolable {

    public final Timer lastPlayed = new Timer();
    public Sound sound;
    public float minImpulseStrength;
    public float minSpeed;
    public long minDelay;

    public void init(Sound sound, float minImpulseStrength, float minSpeed, long minDelay) {
        this.sound = sound;
        this.minImpulseStrength = minImpulseStrength;
        this.minSpeed = minSpeed;
        this.minDelay = minDelay;
        lastPlayed.reset();
    }

    @Override
    public void reset() {
        sound = null;
        minImpulseStrength = 0;
        minSpeed = 0;
        minDelay = 0;
        lastPlayed.stop();
    }
}
