package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.ui.MainMenu;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState implements InputProcessor {

    public static final int WALKING_SPEED = 100;
    
	private LocalMusic music;
    private Sound click;
    private Texture logo;
    float stateTime = 0f;
    private AnimationExtended walking;
    private float x = 0;
    private MainMenu mainMenu;
    InputInterceptor inputProcessor;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        mainMenu = new MainMenu();
        mainMenu.init(assetManager);
        logo = assetManager.getTexture("logo");
        walking = assetManager.getAnimation("walking");
        this.music = Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU);
        click = assetManager.getSound("click");
//        music.play();

        inputProcessor = new InputInterceptor(this) {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (GameStates.GAMEPLAY.isActive()) {
                            GameStates.MAINMENU.activate(new SplitHorizontalTransition(100).reverse(), null);
                        } else {
                            GameStates.GAMEPLAY.activate(new SplitHorizontalTransition(100), null);
                        }
                        return true;
                }
                return isActive && mainProcessor.keyUp(keycode);
            }
        };
        Main.inputMultiplexer.addProcessor(inputProcessor);
    }

    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        mainMenu.render();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);

        DrawUtil.batch.draw(logo, 0, 0, logo.getWidth(), logo.getHeight(), 0, 0,
                logo.getWidth(), logo.getHeight(), false, true);

        DrawUtil.batch.draw(walking.getKeyFrame(stateTime), x,
                512, walking.getKeyFrame(0f).getRegionWidth(), -walking.getKeyFrame(0f).getRegionHeight());
    }

    @Override
    public void update(float delta) {
    	music.update();
    	mainMenu.update(delta);
        stateTime += delta;
        x += delta * WALKING_SPEED;
        if (x > 1024) {
            x = -walking.getKeyFrame(0f).getRegionWidth();
        }
    }

    @Override
    public void onEnter() {
		if (this.music.isMusicPlaying()) {
			this.music.setFade('i', 4000);
		} else {
			this.music.play("menu");
		}
    	
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(true);
    }

    @Override
    public void onLeave() {
		if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 4000);
		}
		
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
        SoundEmitter.playGlobal(click, false, screenX, screenY, 0);
        return true;
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
