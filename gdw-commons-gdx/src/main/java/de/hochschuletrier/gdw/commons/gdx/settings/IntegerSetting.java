package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;

/**
 *
 * @author Santo Pfingsten
 */
public class IntegerSetting extends Setting {

    final int def;

    public IntegerSetting(Preferences prefs, String key, int def) {
        super(prefs, key);
        this.def = def;
    }

    @Override
    public void reset() {
        set(def);
    }

    public void set(int value) {
        prefs.putInteger(key, value);
    }

    public int get() {
        return prefs.getInteger(key, def);
    }
}
