package de.hochschuletrier.gdw.ws1415.game;

import java.util.Enumeration;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.game.components.AIComponent;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.BlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.EventBoxType;

public class EntityCreator {

    public static Entity createAndAddPlayer(float x, float y, float rotation, PooledEngine engine) {
        Entity player = engine.createEntity();

        player.add(engine.createComponent(AnimationComponent.class));
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(DamageComponent.class));
        player.add(engine.createComponent(SpawnComponent.class));
        player.add(engine.createComponent(InputComponent.class));

        engine.addEntity(player);
        return player;
    }

    public static Entity createAndAddEnemy(float x, float y, float rotation, PooledEngine engine) {
        Entity enemy = engine.createEntity();

        enemy.add(engine.createComponent(DamageComponent.class));
        enemy.add(engine.createComponent(AIComponent.class));
        enemy.add(engine.createComponent(AnimationComponent.class));
        enemy.add(engine.createComponent(PositionComponent.class));
        enemy.add(engine.createComponent(SpawnComponent.class));

        engine.addEntity(enemy);
        return enemy;
    }

    public static Entity createAndAddEventBox(EventBoxType type, float x, float y, PooledEngine engine) {
        Entity box = engine.createEntity();

        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));

        engine.addEntity(box);
        return box;
    }

    public static Entity createAndAddInvulnerableFloor(PooledEngine engine, PhysixSystem physixSystem, Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = new PhysixBodyComponent();
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(1).friction(1f).shapeBox(width, height).restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        
        entity.add(bodyComponent);

        BlockComponent blockComp = new BlockComponent();
        entity.add(blockComp);
        
        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddVulnerableFloor(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height) {
        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = new PhysixBodyComponent();
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(1).friction(1f).shapeBox(width, height).restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        BlockComponent blockComp = new BlockComponent();
        entity.add(blockComp);
        
        engine.addEntity(entity);
        return entity;

    }
}
