package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.ui.OptionsMenu;

public class OptionsMenuState extends GameState implements InputProcessor {
    
	 private OptionsMenu optionsMenu;
	 InputInterceptor inputProcessor;
	 private LocalMusic music;
	
    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        
        // do we need this ??????
        this.music = Main.MusicManager.getMusicStreamByStateName(GameStates.MAINMENU);
        Sound click = assetManager.getSound("click");
        
        
        inputProcessor = new InputInterceptor(this);
        Main.inputMultiplexer.addProcessor(inputProcessor);
    }
    
    @Override
    public void render() {
    	  Main.getInstance().screenCamera.bind();
          optionsMenu.render();
    }
    
    @Override
    public void update(float delta) {
    	optionsMenu.update(delta);
    	this.music.update();
    }
    
    @Override
    public void onEnter() {
    	if (this.music.isMusicPlaying()) {
			this.music.setFade('i',4000);
		} else {
			this.music.play("menu");
		}
        optionsMenu = new OptionsMenu();
        optionsMenu.init(assetManager);
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(false);
    }
    
    @Override
    public void onEnterComplete() {
    }

    @Override
    public void onLeave() {
    	if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2000);
		}
    	optionsMenu.dispose();
        inputProcessor.setActive(false);
        inputProcessor.setBlocking(false);
    }

    @Override
    public void onLeaveComplete() {
    }
    
    @Override
    public void dispose() {
    }

	@Override
	public boolean keyDown(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
