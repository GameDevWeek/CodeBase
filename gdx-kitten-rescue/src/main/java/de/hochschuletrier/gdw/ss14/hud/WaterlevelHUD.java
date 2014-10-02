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
    //private Vector2 rectPos;
    private float rectWidth;
    private float rectHeight;
    private boolean buttonDown = false;
    
    private float posX = 0;
    private float posY = 0;
    
    private float xOffset;
    private float yOffset;
    
    public WaterlevelHUD(AssetManagerX assetManager) {
        this(assetManager, null);
    }
    
    public WaterlevelHUD(AssetManagerX assetManager, HudComponent parent) {
        super(assetManager, parent);
        xOffset = 0;
        yOffset = 0;
        
        calcPos();
        
        if (parent == null) {
            rectWidth = 300f;
        } else {
            rectWidth = parent.getWidth();
        }
        
        rectHeight = 25f;
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
        
        calcPos();
        DrawUtil.fillRect(posX, posY, rectWidth * (currentPercent / 100f), rectHeight, Color.BLUE);
        DrawUtil.drawRect(posX, posY, rectWidth, rectHeight, Color.BLACK);
    }

    private void calcPos() {
        if (parent != null) {
            posX = parent.getX();
            posY = parent.getY() + parent.getHeigth();
        } else {
            posX = Gdx.graphics.getWidth() - rectWidth - xOffset;
            posY = yOffset;
        }
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

    @Override
    public float getX() {
        return posX;
    }

    @Override
    public float getY() {
        return posY;
    }

    @Override
    public float getWidth() {
        return this.rectWidth;
    }

    @Override
    public float getHeigth() {
        return this.rectHeight;
    }
}
