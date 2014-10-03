package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

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
    
    private float currentOverlap1 = 0;
    private float currentOverlap2 = overlap;
    
    private float overlapSpeed = 3f;
    private float progress = 0;
    
    private boolean move = false;
    
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
        
        if (move) {
            progress += Gdx.graphics.getDeltaTime() * overlapSpeed;
            if (isLaser) {
                currentOverlap1 = MathUtils.lerp(overlap, 0f, progress);
                currentOverlap2 = MathUtils.lerp(0f, overlap, progress);
            } else {
                currentOverlap1 = MathUtils.lerp(0f, overlap, progress);
                currentOverlap2 = MathUtils.lerp(overlap, 0f, progress);
            }
            
            if (progress > 1 || progress < 0) move = false;
        }
        
        if (isLaser) {
            DrawUtil.batch.draw(waterpistol, getX() + (waterpistol.getWidth() * getScale() * currentOverlap1), getY() + (waterpistol.getHeight() * getScale() * currentOverlap1),
                    waterpistol.getWidth() * getScale(), waterpistol.getHeight() * getScale(), 0, 0, 
                    waterpistol.getWidth(), waterpistol.getHeight(), false, true);
            
            DrawUtil.batch.draw(laser, getX() + (laser.getWidth() * getScale() * currentOverlap2), getY() + (laser.getHeight() * getScale() * currentOverlap2),
                    laser.getWidth() * getScale(), laser.getHeight() * getScale(), 0, 0, 
                    laser.getWidth(), laser.getHeight(), false, true);
        } else {
            DrawUtil.batch.draw(laser, getX() + (laser.getWidth() * getScale() * currentOverlap2), getY() + (laser.getHeight() * getScale() * currentOverlap2),
                    laser.getWidth() * getScale(), laser.getHeight() * getScale(), 0, 0, 
                    laser.getWidth(), laser.getHeight(), false, true);
            
            DrawUtil.batch.draw(waterpistol, getX() + (waterpistol.getWidth() * getScale() * currentOverlap1), getY() + (waterpistol.getHeight() * getScale() * currentOverlap1),
                    waterpistol.getWidth() * getScale(), waterpistol.getHeight() * getScale(), 0, 0, 
                    waterpistol.getWidth(), waterpistol.getHeight(), false, true);
        }
        
        
        // DrawUtil.drawRect(getX(), getY(), super.getWidth(), super.getHeight(), Color.RED);
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
        if (!move) {
            move = true;
            progress = 0f;
        }
        
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
