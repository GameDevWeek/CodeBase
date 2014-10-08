package de.hochschuletrier.gdw.ss14.game.contactlisteners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.ss14.game.components.ImpactSoundComponent;

public class ImpactSoundListener extends PhysixContactAdapter {
    private static final ComponentMapper<ImpactSoundComponent> mapper = ComponentMapper.getFor(ImpactSoundComponent.class);

    @Override
    public void postSolve(PhysixContact contact, ContactImpulse impulse) {
        Sound ignore = play(contact.getMyPhysixBody(), impulse, null);
        play(contact.getOtherPhysixBody(), impulse, ignore);
    }

    private Sound play(PhysixBody body, ContactImpulse impulse, Sound ignore) {
        Entity owner = body.getOwner();
        if(owner != null) {
            ImpactSoundComponent isc = mapper.get(owner);
            if(isc != null && isc.sound != ignore && isc.lastPlayed.get() > isc.minDelay) {
                float impulseStrength = Math.abs(impulse.getNormalImpulses()[0]);
                if(impulseStrength > isc.minImpulseStrength) {
                    float speed = body.getLinearVelocity().len();
                    if(speed > isc.minSpeed) {
                        isc.lastPlayed.reset();
                        SoundEmitter.playGlobal(isc.sound, false, body.getX(), body.getY(), 0);
                        return isc.sound;
                    }
                }
            }
        }
        return null;
    }
    
}
