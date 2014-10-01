/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss14.sandbox.graphics;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationWithVariableFrameTime;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.ecs.Engine;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.systems.AnimationSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.RenderSystem;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

/**
 *
 * @author rftpool13
 */
public class AnimationMain extends SandboxGame {

    Engine engine;
    EntityManager manager;

    @Override
    public void init(AssetManagerX assetManager) {
        engine = new Engine();
        manager = new EntityManager();

        engine.addSystem(new AnimationSystem(manager, 50));
        engine.addSystem(new RenderSystem(manager, 100));

        // Entity "Dummy"
        int dummy = manager.generateNewEntityID();
        // Component Render from entity dummy
        RenderComponent c1 = new RenderComponent();
        manager.addComponent(dummy, c1);
        //Component Animation
        AnimationComponent c2 = new AnimationComponent();
        c2.animation = new AnimationWithVariableFrameTime[2];
        c2.animation[CatStateEnum.HIT.ordinal()]
                = loadAnimation("data/animations/walking_1.png", 5, 1, new float[] {0.1f, 0.5f, 0.1f, 0.1f, 0.1f}, Animation.PlayMode.LOOP);
        c2.animation[CatStateEnum.IDLE.ordinal()]
                = loadAnimation("data/animations/walking.png", 10, 1, 0.2f, Animation.PlayMode.LOOP);
        manager.addComponent(dummy, c2);
        //Physics Dummy
        PhysicsComponent c3 = new PhysicsComponent();
        c3.dummyPosition.x = 0;
        c3.dummyPosition.y = 0;
        manager.addComponent(dummy, c3);
        // State informations
        CatPropertyComponent c4 = new CatPropertyComponent();
        c4.state = CatStateEnum.IDLE;
    }

    private AnimationWithVariableFrameTime loadAnimation(String path, int cols, int row, float frameDuration, Animation.PlayMode playMode) {

        Texture tex;
        TextureRegion[][] tmp;
        TextureRegion[] frames;

        tex = new Texture(path);
        tmp = TextureRegion.split(tex, tex.getWidth() / cols, tex.getHeight() / row);
        frames = new TextureRegion[cols * row];
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        AnimationWithVariableFrameTime ani = new AnimationWithVariableFrameTime(frameDuration, frames);
        ani.setPlayMode(playMode);
        return ani;
    }

    private AnimationWithVariableFrameTime loadAnimation(String path, int cols, int row, float frameDurations[], Animation.PlayMode playMode) {
        AnimationWithVariableFrameTime ani = loadAnimation(path, cols, row, 0, playMode);
        ani.setFrameDurations(frameDurations);
        return ani;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(0, 0, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.render();
    }

    @Override
    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            Array<Integer> entities = manager.getAllEntitiesWithComponents(CatPropertyComponent.class);

            CatPropertyComponent catCompo;

            for (Integer integer : entities) {
                catCompo = manager.getComponent(integer, CatPropertyComponent.class);

                catCompo.state = CatStateEnum.HIT;
            }
        }
        engine.update(delta);
    }

}
