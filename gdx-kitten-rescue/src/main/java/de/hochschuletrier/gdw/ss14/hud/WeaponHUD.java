package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WeaponHUD extends HudComponent {
    private ToolState currentState;
    
    private Texture laser;
    private Texture laser_off;
   
    
    private float overlap = 0.1f;
    
    private float currentOverlap1 = 0;
    private float currentOverlap2 = overlap;
    
    private float overlapSpeed = 3f;
    private float progress = 0;
    
    private boolean move = false;
    
    public WeaponHUD(AssetManagerX assetManager) {
        super(assetManager);
        
        laser = assetManager.getTexture("weapon_laser");
        laser_off = assetManager.getTexture("weapon_laser_off");
        
        
        super.width = laser.getWidth() * (1.0f + overlap);
        super.height = laser.getHeight() * (1.0f + overlap);
        
        currentState = ToolState.LASER;
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        EntityManager entityManager = EntityManager.getInstance();
        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laserComp = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);
        
        if (currentState != laserComp.toolState) {
            move = true;
            currentState = laserComp.toolState;
        }
        
        if (move) {
            progress += Gdx.graphics.getDeltaTime() * overlapSpeed;
            if (currentState == ToolState.LASER) {
                currentOverlap1 = MathUtils.lerp(overlap, 0f, progress);
                currentOverlap2 = MathUtils.lerp(0f, overlap, progress);
            } else {
                currentOverlap1 = MathUtils.lerp(0f, overlap, progress);
                currentOverlap2 = MathUtils.lerp(overlap, 0f, progress);
            }
            
            if (progress > 1) {
                progress = 0;
                move = false;
            }
        }
        
        if (currentState == ToolState.LASER) {
            DrawUtil.batch.draw(laser_off, getX() + (laser_off.getWidth() * getScale() * currentOverlap1), getY() + (laser_off.getHeight() * getScale() * currentOverlap1),
                    laser_off.getWidth() * getScale(), laser_off.getHeight() * getScale(), 0, 0, 
                    laser_off.getWidth(), laser_off.getHeight(), false, true);
            
            DrawUtil.batch.draw(laser, getX() + (laser.getWidth() * getScale() * currentOverlap2), getY() + (laser.getHeight() * getScale() * currentOverlap2),
                    laser.getWidth() * getScale(), laser.getHeight() * getScale(), 0, 0, 
                    laser.getWidth(), laser.getHeight(), false, true);
        } else {
            DrawUtil.batch.draw(laser, getX() + (laser.getWidth() * getScale() * currentOverlap2), getY() + (laser.getHeight() * getScale() * currentOverlap2),
                    laser.getWidth() * getScale(), laser.getHeight() * getScale(), 0, 0, 
                    laser.getWidth(), laser.getHeight(), false, true);
            
            DrawUtil.batch.draw(laser_off, getX() + (laser_off.getWidth() * getScale() * currentOverlap1), getY() + (laser_off.getHeight() * getScale() * currentOverlap1),
                    laser_off.getWidth() * getScale(), laser_off.getHeight() * getScale(), 0, 0, 
                    laser_off.getWidth(), laser_off.getHeight(), false, true);
        }
        
        
        // DrawUtil.drawRect(getX(), getY(), super.getWidth(), super.getHeight(), Color.RED);
    }
}
