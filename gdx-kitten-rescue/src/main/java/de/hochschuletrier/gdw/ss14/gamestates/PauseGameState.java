package de.hochschuletrier.gdw.ss14.gamestates;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.ui.PauseMenu;

public class PauseGameState extends KittenGameState implements InputProcessor {
    
	 private PauseMenu pauseMenu;
	 InputInterceptor inputProcessor;
	 private LocalMusic music;
	
    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        
        // do we need this ??????
        this.music = Main.MusicManager.getMusicStreamByStateName(GameStateEnum.MAINMENU);
        Sound click = assetManager.getSound("click");
        
        
        inputProcessor = new InputInterceptor(this) {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (GameStateEnum.GAMEPLAY.isActive()) {
                            GameStateEnum.PAUSEGAME.activate(new SplitHorizontalTransition(800).reverse(), null);
                        } else {
                            GameStateEnum.GAMEPLAY.activate(new SplitHorizontalTransition(800), null);
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
          pauseMenu.render();
    }
    
    @Override
    public void update(float delta) {
    	pauseMenu.update(delta);
    	this.music.update();
    }
    
    @Override
    public void onEnter(KittenGameState previousState) {
    	if (this.music.isMusicPlaying()) {
			this.music.setFade('i',4000);
		} else {
			this.music.play("menu");
		}
        pauseMenu = new PauseMenu();
        pauseMenu.init(assetManager);
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(false);
    }
    
    @Override
    public void onEnterComplete() {
    }

    @Override
    public void onLeave(KittenGameState nextState) {
    	if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2000);
		}

        // TODO: remove this shit and properly fade music!
        if(nextState instanceof GameplayState)
        {
            if (this.music.isMusicPlaying())
            {
                this.music.stop();

            }
        }

    	pauseMenu.dispose();
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
