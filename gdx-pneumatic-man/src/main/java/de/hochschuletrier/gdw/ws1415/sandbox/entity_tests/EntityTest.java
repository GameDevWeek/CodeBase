package de.hochschuletrier.gdw.ws1415.sandbox.entity_tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

public class EntityTest extends SandboxGame {

    private static final Logger logger = LoggerFactory
            .getLogger(EntityTest.class);
    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE,
            GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE,
            GameConstants.COMPONENT_POOL_MAX_SIZE);

    private final HealthSystem Health = new HealthSystem(0);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();

    public EntityTest() {
        engine.addSystem(Health);
    }

    @Override
    public void dispose() {

    }

    Entity BlockEntity;

    @Override
    public void init(AssetManagerX assetManager) {

        BlockEntity = engine.createEntity();
        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.reset();
        logger.info("Health Value: " + Health.Value);
        BlockEntity.add(Health);
        engine.addEntity(BlockEntity);

        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        /*
         * totalMapWidth = map.getWidth() * map.getTileWidth(); totalMapHeight =
         * map.getHeight() * map.getTileHeight();
         */
        camera.setBounds(0, 0, 1000, 1000);
        camera.updateForced();
    }

    @Override
    public void update(float delta) {
        engine.update(delta);
        camera.update(delta);

        float X = Gdx.graphics.getWidth() * 0.5f;
        float Y = Gdx.graphics.getHeight() * 0.5f;
        float DH = 100.0f;
        float DW = 100.0f;

        HealthComponent HealthOfBlock = BlockEntity
                .getComponent(HealthComponent.class);
        DrawUtil.fillRect(X - DW * 0.5f, Y - DH * 0.5f, DW, DH, new Color(
                HealthOfBlock.Value / 10.0f, 1.0f, 1.0f, 1.0f));
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            HealthOfBlock.Value -= 1;
        }
    }
}
