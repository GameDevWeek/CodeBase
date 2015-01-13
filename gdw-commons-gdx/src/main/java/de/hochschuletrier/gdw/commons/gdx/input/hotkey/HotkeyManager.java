package de.hochschuletrier.gdw.commons.gdx.input.hotkey;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Santo Pfingsten
 */
public class HotkeyManager {

    private static final HashMap<Integer, Hotkey> hotkeys = new HashMap();

    public static InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public static void add(Hotkey hotkey) {
        hotkeys.put(hotkey.key, hotkey);
    }

    public static void remove(Hotkey hotkey) {
        Integer key = getKey(hotkey);
        hotkeys.remove(key);
    }

    public static void clear() {
        hotkeys.clear();
    }

    private static Integer getKey(Hotkey hotkey) {
        for (Map.Entry<Integer, Hotkey> entrySet : hotkeys.entrySet()) {
            if (entrySet.getValue() == hotkey) {
                return entrySet.getKey();
            }
        }
        return null;
    }

    private static final InputProcessor inputProcessor = new InputAdapter() {

        @Override
        public boolean keyDown(int keycode) {
            int key = Hotkey.calculateKey(Hotkey.getPressedBits(), keycode);
            Hotkey hotkey = hotkeys.get(key);
            if (hotkey != null) {
                hotkey.runnable.run();
                return true;
            }
            return false;
        }
    };
}
