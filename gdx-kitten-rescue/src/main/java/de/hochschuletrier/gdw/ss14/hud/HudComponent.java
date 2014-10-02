package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class HudComponent {
	
    protected Vector2 position;
    protected float width;
    protected float height;
    protected float scale = 1.0f;
    
	protected AssetManagerX assetManager;

	public abstract void render();
	
	public HudComponent(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		this.position = new Vector2(0f, 0f);
	}
	
	public Vector2 getPosition() {
	    return position.cpy();
	}
	
	public void setPosition(Vector2 pos) {
	    this.position = pos.cpy();
	}
	
	public void setPosition(float x, float y) {
	    this.position = new Vector2(x, y);
	}
	
	public float getX() {
	    return this.position.x;
	}
	
	public float getY() {
	    return this.position.y;
	}
	
	public float getWidth() {
	    return this.width * this.scale;
	}
	
	public float getHeight() {
	    return this.height * this.scale;
	}

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
