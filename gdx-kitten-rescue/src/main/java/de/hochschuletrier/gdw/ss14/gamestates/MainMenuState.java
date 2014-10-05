package de.hochschuletrier.gdw.ss14.gamestates;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;

import de.hochschuletrier.gdw.ss14.ui.*;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends KittenGameState implements InputProcessor {

    
    private MainMenu mainMenu;
    InputInterceptor inputProcessor;
    private LocalMusic music;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

     // do we need this block ??????
        Texture logo = assetManager.getTexture("logo");
        AnimationExtended walking = assetManager.getAnimation("walking");
        this.music = Main.MusicManager.getMusicStreamByStateName(GameStateEnum.MAINMENU);
        Sound click = assetManager.getSound("click");
        
//        music.play();

        inputProcessor = new InputInterceptor(this) {
            @Override
            public boolean keyUp(int keycode) {
                return isActive && mainProcessor.keyUp(keycode);
            }
        };
        Main.inputMultiplexer.addProcessor(inputProcessor);
    }

    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        mainMenu.render();
}

    @Override
    public void update(float delta) {
    	mainMenu.update(delta);
    	this.music.update();
    }

    @Override
    public void onEnter(KittenGameState previousState) {
		if (this.music.isMusicPlaying()) {
			this.music.setFade('i',4000);
		} else {
			this.music.play("menu");
		}
        mainMenu = new MainMenu();
        mainMenu.init(assetManager);
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(false);
    }

    @Override
    public void onLeave(KittenGameState nextState) {
		if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2000);
		}
    	mainMenu.dispose();
        inputProcessor.setActive(false);
        inputProcessor.setBlocking(false);
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
