package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WeaponHUD extends HudComponent implements GameInputAdapter {
    private boolean isLaser = true;
    
    private Texture laser;
    private Texture waterpistol;
    
    private float overlap = 0.3f;
    
    public WeaponHUD(AssetManagerX assetManager) {
        super(assetManager);
        
        laser = assetManager.getTexture("weapon_laser");
        waterpistol = assetManager.getTexture("weapon_waterpistol");
        
        super.width = laser.getWidth() * (1.0f + overlap);
        super.height = laser.getHeight() * (1.0f + overlap);
        
        InputManager.getInstance().addGameInputAdapter(this);
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        Texture back, front;
        if (isLaser) {
            front = laser;
            back = waterpistol;
        } else {
            front = waterpistol;
            back = laser;
        }
        
        // back
        DrawUtil.batch.draw(back, getX(), getY(),
                back.getWidth() * getScale(), back.getHeight() * getScale(), 0, 0, 
                back.getWidth(), back.getHeight(), false, true);
        
        // front
        DrawUtil.batch.draw(front, getX() + (back.getWidth() * getScale() * overlap), getY() + (back.getHeight() * getScale() * overlap),
                front.getWidth() * getScale(), front.getHeight() * getScale(), 0, 0, 
                front.getWidth(), front.getHeight(), false, true);
        
        DrawUtil.drawRect(getX(), getY(), super.getWidth(), super.getHeight(), Color.RED);
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
        isLaser = !isLaser;
    }

    @Override
    public void waterPistolButtonDown() {
    }

    @Override
    public void waterPistolButtonUp() {
    }

    @Override
    public void menueButtonPressed() {
        // TODO Auto-generated method stub
        
    }
}
