package de.hochschuletrier.gdw.ws1516.sandbox.event;

import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1516.events.TestEvent;
import de.hochschuletrier.gdw.ws1516.sandbox.SandboxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class EventTest extends SandboxGame implements TestEvent.Listener {

    private static final Logger logger = LoggerFactory.getLogger(EventTest.class);

    public EventTest() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        TestEvent.register(this);
    }

    @Override
    public void dispose() {
        TestEvent.unregister(this);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                TestEvent.emit("left mouse");
                break;
            case Input.Buttons.RIGHT:
                TestEvent.emit("right mouse");
                break;
            case Input.Buttons.MIDDLE:
                TestEvent.emit("middle mouse");
                break;
        }
        return false;
    }

    @Override
    public void onTestEvent(String message) {
        logger.info("Event received: {}", message);
    }
}
