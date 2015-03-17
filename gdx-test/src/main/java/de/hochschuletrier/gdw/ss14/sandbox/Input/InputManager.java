package de.hochschuletrier.gdw.ss14.sandbox.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class InputManager extends SandboxGame {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(AssetManagerX assetManager) {
		InputKeybord inputProcessor = new InputKeybord();
		Gdx.input.setInputProcessor(inputProcessor);
		
	}

	@Override
	public void update(float delta) {
		DrawUtil.setColor(Color.GREEN);
		DrawUtil.fillRect(Gdx.input.getX(), Gdx.input.getY(), 35, 35);
		
		

		
	}
	

}
