package de.hochschuletrier.gdw.ws1415.game;

import java.util.Enumeration;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1415.game.components.AIComponent;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.EventBoxType;

public class EntityCreator
{
    
    
    public static Entity createAndAddPlayer (float  x, float y, float rotation, PooledEngine engine)
    {
        Entity player = engine.createEntity();
        
        player.add(engine.createComponent(AnimationComponent.class));
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(DamageComponent.class));
        player.add(engine.createComponent(SpawnComponent.class));
        
        engine.addEntity(player);
        return player;
    }
    
    
    public static Entity createAndAddEnemy (float  x, float y, float rotation, PooledEngine engine)
    {
        Entity enemy = engine.createEntity();
        
        enemy.add(engine.createComponent(DamageComponent.class));
        enemy.add(engine.createComponent(AIComponent.class));
        enemy.add(engine.createComponent(AnimationComponent.class));
        enemy.add(engine.createComponent(PositionComponent.class));
        enemy.add(engine.createComponent(SpawnComponent.class));

        
        engine.addEntity(enemy);
        return enemy;
    }
    
    public static Entity createAndAddEventBox (EventBoxType type, float x, float y, PooledEngine engine)
    {
        Entity box = engine.createEntity();
        
        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));
        
        
        engine.addEntity(box);
        return box;
    }
}

