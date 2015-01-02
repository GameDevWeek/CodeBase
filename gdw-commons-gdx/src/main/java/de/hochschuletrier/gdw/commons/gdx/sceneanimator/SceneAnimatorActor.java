package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * An Actor for Scene2D
 *
 * @author Santo Pfingsten
 */
public class SceneAnimatorActor extends Group {

    private final SceneAnimator animator;

    public SceneAnimatorActor(SceneAnimator animator) {
        super();
        this.animator = animator;
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        animator.render();
    }

    @Override
    public void act(float delta) {
        animator.update(delta);
    }
}
