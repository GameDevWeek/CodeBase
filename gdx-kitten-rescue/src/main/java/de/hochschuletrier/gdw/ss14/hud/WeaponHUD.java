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
    
    public WeaponHUD(AssetManagerX assetManager) {
        super(assetManager);
        
        laser = assetManager.getTexture("weapon_laser");
        waterpistol = assetManager.getTexture("weapon_waterpistol");
        
        super.width = laser.getWidth();
        super.height = laser.getHeight();
        
        InputManager.getInstance().addGameInputAdapter(this);
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        Texture tmp;
        if (isLaser) {
            tmp = laser;
        } else {
            tmp = waterpistol;
        }
        
        // DrawUtil.drawRect(getX(), getY(), super.getWidth(), super.getHeigth(), Color.RED);
        
        DrawUtil.batch.draw(tmp, getX(), getY(), super.getWidth(), super.getHeigth(), 0, 0, tmp.getWidth(), tmp.getHeight(), false, true);
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
