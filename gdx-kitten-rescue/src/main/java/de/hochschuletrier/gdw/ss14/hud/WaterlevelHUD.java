package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WaterlevelHUD extends HudComponent implements GameInputAdapter {
    private float currentPercent = 100;
    private boolean buttonDown = false;
    
    private WaterlevelHUD(AssetManagerX assetManager) {
        super(assetManager);
    }
    
    public WaterlevelHUD(AssetManagerX assetManager, float height, float width) {
        super(assetManager);
        
        super.width = width;
        super.height = height;
        
        InputManager.getInstance().addGameInputAdapter(this);
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        if (buttonDown) {
            currentPercent -= 10.0f * Gdx.graphics.getDeltaTime();
            if (currentPercent < 0) {
                currentPercent = 0f;
            }
        } else {
            if (currentPercent < 100) {
                currentPercent += 5.0f * Gdx.graphics.getDeltaTime();
                if (currentPercent > 100) {
                    currentPercent = 100.0f;
                }
            }
        }
        
        DrawUtil.fillRect(position.x + (getWidth() - (getWidth() * (currentPercent / 100f))), position.y, getWidth() * (currentPercent / 100f), getHeight(), Color.BLUE);
        DrawUtil.drawRect(position.x, position.y, getWidth(), getHeight(), Color.BLACK);
    }
    
    @Override
    public void move(int screenX, int screenY) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveUp(float scale) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveDown(float scale) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveLeft(float scale) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveRight(float scale) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void laserButtonPressed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void waterPistolButtonDown() {
        buttonDown = true;
    }

    @Override
    public void waterPistolButtonUp() {
        buttonDown = false;
    }

    @Override
    public void menueButtonPressed() {
        // TODO Auto-generated method stub
        
    }
}
