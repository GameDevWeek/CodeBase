package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.ImpactSoundComponent;

public class ImpactSoundComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "ImpactSound";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        ImpactSoundComponent component = engine.createComponent(ImpactSoundComponent.class);
        component.sound = assetManager.getSound(properties.getString("sound", "click"));
        component.minImpulseStrength = properties.getFloat("minImpulseStrength", 20);
        component.minSpeed = properties.getFloat("minSpeed", 20);
        component.minDelay = properties.getInt("minDelay", 100);
        component.lastPlayed.reset();
        entity.add(component);
    }
}