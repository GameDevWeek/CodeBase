package de.hochschuletrier.gdw.commons.gdx.input.hotkey;

import com.badlogic.gdx.Input;

/**
 *
 * @author Santo Pfingsten
 */
public enum HotkeyModifier {

    CTRL(Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT),
    ALT(Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT),
    SHIFT(Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT);

    final int keycode1;
    final int keycode2;

    HotkeyModifier(int keycode1, int keycode2) {
        this.keycode1 = keycode1;
        this.keycode2 = keycode2;
    }
}
