package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import de.hochschuletrier.gdw.commons.utils.Timer;

public class ImpactSoundComponent extends Component {
    public final Timer lastPlayed = new Timer();
    public final Sound sound;
    public final float minImpulseStrength;
    public final float minSpeed;
    public final long minDelay;

    public ImpactSoundComponent(Sound sound, float minImpulseStrength, float minSpeed, long minDelay) {
        this.sound = sound;
        this.minImpulseStrength = minImpulseStrength;
        this.minSpeed = minSpeed;
        this.minDelay = minDelay;
    }
}
