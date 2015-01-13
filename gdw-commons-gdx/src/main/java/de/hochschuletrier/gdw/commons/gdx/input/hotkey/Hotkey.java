package de.hochschuletrier.gdw.commons.gdx.input.hotkey;

import com.badlogic.gdx.Gdx;

/**
 *
 * @author Santo Pfingsten
 */
public class Hotkey {

    final int key;
    final Runnable runnable;

    public Hotkey(Runnable runnable, int keycode, HotkeyModifier... modifiers) {
        key = calculateKey(calculateBits(modifiers), keycode);
        this.runnable = runnable;
    }

    public void register() {
        HotkeyManager.add(this);
    }

    public void unregister() {
        HotkeyManager.remove(this);
    }

    static int calculateKey(int modifierBits, int keycode) {
        return ((keycode << 8) & 0xffffff00) | (modifierBits & 0xff);
    }

    static int calculateBits(HotkeyModifier[] modifiers) {
        int bits = 0;
        for (HotkeyModifier modifier : modifiers) {
            bits |= 1 << modifier.ordinal();
        }
        return bits;
    }

    static int getPressedBits() {
        int bits = 0;
        for (HotkeyModifier modifier : HotkeyModifier.values()) {
            if (Gdx.input.isKeyPressed(modifier.keycode1) || Gdx.input.isKeyPressed(modifier.keycode2)) {
                bits |= 1 << modifier.ordinal();
            }
        }
        return bits;
    }

}
