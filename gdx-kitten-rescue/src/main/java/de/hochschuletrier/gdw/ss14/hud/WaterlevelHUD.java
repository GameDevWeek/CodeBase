package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WaterlevelHUD extends HudComponent {
    private Texture waterbar;
    
    private float barPosX;
    private float barPosY;
    private float barWidth;
    private float barHeight;
    
    private WaterlevelHUD(AssetManagerX assetManager) {
        super(assetManager);
    }
    
    public WaterlevelHUD(AssetManagerX assetManager, float height, float width) {
        super(assetManager);
        
        waterbar = assetManager.getTexture("waterbar");
        
        super.width = waterbar.getWidth();
        super.height = waterbar.getHeight();
        
        barPosX = 75.0f;
        barPosY = 20.f;
        
        barWidth = 410.0f;
        barHeight = 45.0f;
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        
        EntityManager man = EntityManager.getInstance();
        Array<Integer> lasers = man.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = man.getComponent(lasers.first(), LaserPointerComponent.class);
        
        DrawUtil.fillRect(calcBarPosX() + (calcBarWidth() - (calcBarWidth() * laser.currentWaterlevel)), calcBarPosY(), calcBarWidth() * laser.currentWaterlevel, calcBarHeight(), Color.BLUE);
        //DrawUtil.drawRect(position.x, position.y, getWidth(), getHeight(), Color.BLACK);
        
        DrawUtil.batch.draw(waterbar, this.position.x, this.position.y, getWidth(), getHeight(), 0, 0, waterbar.getWidth(), waterbar.getHeight(), false, true);
    }
    
    private float calcBarPosX() {
        return (this.position.x + (barPosX * getScale()));
    }
    
    private float calcBarPosY() {
        return (this.position.y + (barPosY * getScale()));
    }
    
    private float calcBarWidth() {
        return this.barWidth * getScale();
    }
    
    private float calcBarHeight() {
        return this.barHeight * getScale();
    }
}
