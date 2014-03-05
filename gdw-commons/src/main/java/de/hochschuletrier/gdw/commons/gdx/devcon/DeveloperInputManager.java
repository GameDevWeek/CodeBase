package de.hochschuletrier.gdw.commons.gdx.devcon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Helper class to easily switch between console and normal input mode
 *
 * @author Santo Pfingsten
 */
public class DeveloperInputManager {
    private static DevConsoleView console;
    private static InputProcessor inputProcessor;

    public static void init(DevConsoleView console) {
        DeveloperInputManager.console = console;
        Gdx.input.setInputProcessor(mainProcessor);
        inputProcessor = emptyProcessor;
    }

    public static void setInputProcessor(InputProcessor inputProcessor) {
        if (inputProcessor == null) {
            DeveloperInputManager.inputProcessor = emptyProcessor;
        } else {
            DeveloperInputManager.inputProcessor = inputProcessor;
        }
    }

    private static InputProcessor getActiveInputProcessor() {
        if (console.isVisible()) {
            return console.getInputProcessor();
        }
        return inputProcessor;
    }

    private static final InputProcessor emptyProcessor = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    };

    private static final InputProcessor mainProcessor = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            return getActiveInputProcessor().keyDown(keycode);
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.F12) {
                console.setVisible(!console.isVisible());
                return true;
            }
            return getActiveInputProcessor().keyUp(keycode);
        }

        @Override
        public boolean keyTyped(char character) {
            return getActiveInputProcessor().keyTyped(character);
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return getActiveInputProcessor().touchDown(screenX, screenY, pointer, button);
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return getActiveInputProcessor().touchUp(screenX, screenY, pointer, button);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return getActiveInputProcessor().touchDragged(screenX, screenY, pointer);
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return getActiveInputProcessor().mouseMoved(screenX, screenY);
        }

        @Override
        public boolean scrolled(int amount) {
            return getActiveInputProcessor().scrolled(amount);
        }
    };
}
