package de.hochschuletrier.gdw.ss14.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WaterlevelUI implements GameInputAdapter {
    private AssetManagerX assetManager;
    private float currentPercent = 100;
    private Vector2 rectPos;
    private float rectWidth;
    private float rectHeight;
    private boolean buttonDown = false;
    
    public WaterlevelUI(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        rectWidth = 300f;
        rectHeight = 25f;
        //InputManager.getInstance().addGameInputAdapter(this);
        
    }
    
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        if (buttonDown) {
            currentPercent -= 5.0f * Gdx.graphics.getDeltaTime();
            if (currentPercent < 0) {
                currentPercent = 0f;
            }
        } else {
            if (currentPercent < 100) {
                currentPercent += 10.0f * Gdx.graphics.getDeltaTime();
                if (currentPercent > 100) {
                    currentPercent = 100.0f;
                }
            }
        }
        
        Vector2 rectPos = new Vector2(Gdx.graphics.getWidth() - rectWidth - 50f, 50f);
        
        DrawUtil.fillRect(rectPos.x, rectPos.y, rectWidth * (currentPercent / 100f), rectHeight, Color.BLUE);
        DrawUtil.drawRect(rectPos.x, rectPos.y, rectWidth, rectHeight, Color.BLACK);
        
        
        
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
