package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;

public class EntitySystemShared {
    private static final int ENTITY_POOL_INITIAL_SIZE = 32;
    private static final int ENTITY_POOL_MAX_SIZE = 256;
    private static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    private static final int COMPONENT_POOL_MAX_SIZE = 256;
    
    public static final PooledEngine ENGINE = new PooledEngine(ENTITY_POOL_INITIAL_SIZE, ENTITY_POOL_MAX_SIZE, COMPONENT_POOL_INITIAL_SIZE, COMPONENT_POOL_MAX_SIZE);
    
    private static final ComponentMapper<PositionComponent> POSITION = ComponentMapper.getFor(PositionComponent.class);
}
