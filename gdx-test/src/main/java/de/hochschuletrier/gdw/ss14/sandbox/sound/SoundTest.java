package de.hochschuletrier.gdw.ss14.sandbox.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

/**
 *
 * @author Santo Pfingsten
 */
public class SoundTest extends SandboxGame {

    private final SoundEmitter emitter = new SoundEmitter();
    private Sound helicopter;
    private final Vector2 cursor = new Vector2();

    public SoundTest() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        helicopter = assetManager.getSound("helicopter");
        emitter.play(helicopter, true);
    }

    @Override
    public void dispose() {
        emitter.dispose();
    }

    @Override
    public void update(float delta) {
        emitter.update();
        emitter.setPosition(cursor.x, cursor.y, 0);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }
}
