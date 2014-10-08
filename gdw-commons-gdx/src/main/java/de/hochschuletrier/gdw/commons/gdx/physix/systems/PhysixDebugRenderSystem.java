package de.hochschuletrier.gdw.commons.gdx.physix.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixDebugRenderSystem extends EntitySystem {

    private final Box2DDebugRenderer renderer = new Box2DDebugRenderer();
    private final World world;
    private final Matrix4 projectionMatrix = new Matrix4();
    private final float scale;

    public PhysixDebugRenderSystem(World world, float scale) {
        this.world = world;
        this.scale = scale;
        
        renderer.setDrawBodies(true);
        renderer.setDrawInactiveBodies(true);
        renderer.setDrawContacts(true);
        renderer.setDrawJoints(true);
    }

    @Override
    public void update(float deltaTime) {
        if (world != null) {
            DrawUtil.batch.end();
            projectionMatrix.set(DrawUtil.batch.getProjectionMatrix());
            projectionMatrix.scale(scale, scale, 1f);
            renderer.render(world, projectionMatrix);
            DrawUtil.batch.begin();
        }
    }
}
