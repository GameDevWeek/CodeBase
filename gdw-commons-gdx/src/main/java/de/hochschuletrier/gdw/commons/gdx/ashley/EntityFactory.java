package de.hochschuletrier.gdw.commons.gdx.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Santo Pfingsten
 */
public class EntityFactory<PT> {

    private HashMap<String, EntityInfo> entityInfos;
    private HashMap<String, ComponentFactory> componentFactories = new HashMap();
    private PooledEngine engine;

    public EntityFactory(String filename, Class mainClass) {
        try {
            entityInfos = JacksonReader.readMap(filename, EntityInfo.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing entity definitions from json", e);
        }

        try {
            String packageName = mainClass.getPackage().getName();
            for (Class clazz : ClassUtils.findClassesInPackage(packageName)) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && ComponentFactory.class.isAssignableFrom(clazz)) {
                    ComponentFactory factory = (ComponentFactory) clazz.newInstance();
                    componentFactories.put(factory.getType(), factory);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error finding component factories", e);
        }
    }

    public Map<String, EntityInfo> getEntityInfos() {
        return entityInfos;
    }

    public void init(PooledEngine engine, AssetManagerX assetManager) {
        for (ComponentFactory factory : componentFactories.values()) {
            factory.init(engine, assetManager);
        }
        this.engine = engine;
    }

    public Entity createEntity(String name, PT param) {
        Entity entity = engine.createEntity();
        EntityInfo info = entityInfos.get(name);
        for (Map.Entry<String, SafeProperties> entrySet : info.components.entrySet()) {
            ComponentFactory factory = componentFactories.get(entrySet.getKey());
            factory.run(entity, info.meta, entrySet.getValue(), param);
        }
        return entity;
    }
}
