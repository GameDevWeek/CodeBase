package de.hochschuletrier.gdw.ss14.game.contactlisteners;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.ss14.game.ComponentMappers;
import de.hochschuletrier.gdw.ss14.game.components.ImpactSoundComponent;

public class ImpactSoundListener extends PhysixContactAdapter {

    @Override
    public void postSolve(PhysixContact contact, ContactImpulse impulse) {
        Sound ignore = play(contact.getMyComponent(), impulse, null);
        play(contact.getOtherComponent(), impulse, ignore);
    }

    private Sound play(PhysixBodyComponent component, ContactImpulse impulse, Sound ignore) {
        if (component != null) {
            ImpactSoundComponent isc = ComponentMappers.impactSound.get(component.getEntity());
            if (isc != null && isc.sound != ignore && isc.lastPlayed.get() > isc.minDelay) {
                float impulseStrength = Math.abs(impulse.getNormalImpulses()[0]);
                if (impulseStrength > isc.minImpulseStrength) {
                    float speed = component.getLinearVelocity().len();
                    if (speed > isc.minSpeed) {
                        isc.lastPlayed.reset();
                        SoundEmitter.playGlobal(isc.sound, false, component.getX(), component.getY(), 0);
                        return isc.sound;
                    }
                }
            }
        }
        return null;
    }
}
