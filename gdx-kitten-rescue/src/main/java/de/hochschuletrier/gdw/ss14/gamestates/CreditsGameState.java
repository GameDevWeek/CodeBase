package de.hochschuletrier.gdw.ss14.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.ui.CreditsMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreditsGameState extends KittenGameState implements InputProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(CreditsGameState.class);

    private CreditsMenu creditsMenu;
    InputInterceptor inputProcessor;
    private LocalMusic music;
    private SceneAnimator sceneAnimator;

    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        
        this.music = Main.MusicManager.getMusicStreamByStateName(GameStateEnum.CREDITS);
        
        try {
            sceneAnimator = new SceneAnimator(assetManager, "data/json/credits.json");
        } catch (Exception ex) {
            logger.error("Error loading credits", ex);
        }
    }

    @Override
    public void render() {
        
        //DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.DARK_GRAY);
        
        //Main.getInstance().screenCamera.bind();

//        Main.getInstance().screenCamera.bind();

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.DARK_GRAY);
        if(sceneAnimator != null) {
            Viewport vp = creditsMenu.getStage().getViewport();
//            vp.getLeftGutterWidth(), vp.getTopGutterHeight()
            sceneAnimator.render();
        }
        
        DrawUtil.batch.end();
        creditsMenu.render();
        DrawUtil.batch.begin();
    }

    @Override
    public void update(float delta) {
        
        if(sceneAnimator != null)
            sceneAnimator.update(delta);
        creditsMenu.update(delta);
    }

    @Override
    public void onEnter(KittenGameState previousState) {
    	this.music.play("pause_loop");
        creditsMenu = new CreditsMenu();
        creditsMenu.init(assetManager);
    }

    @Override
    public void onEnterComplete() {
    }

    @Override
    public void onLeave(KittenGameState nextState) {
    	this.music.stop();
        creditsMenu.dispose();
    }

    @Override
    public void onLeaveComplete() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
