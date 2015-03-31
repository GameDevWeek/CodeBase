package de.hochschuletrier.gdw.commons.gdx.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class ComponentFactory<PT> {

    protected PooledEngine engine;
    protected AssetManagerX assetManager;

    public abstract String getType();

    public void init(PooledEngine engine, AssetManagerX assetManager) {
        this.engine = engine;
        this.assetManager = assetManager;
    }

    public abstract void run(Entity entity, SafeProperties meta, SafeProperties properties, PT param);
}
