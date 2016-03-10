package de.hochschuletrier.gdw.ws1516.game.components;

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

    @Override
    public void reset() {
        sound = null;
        minImpulseStrength = 0;
        minSpeed = 0;
        minDelay = 0;
        lastPlayed.stop();
    }
}
