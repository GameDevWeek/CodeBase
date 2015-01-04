package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;

/**
 *
 * @author Santo Pfingsten
 */
public class BooleanSetting extends Setting {

    final boolean def;

    public BooleanSetting(Preferences prefs, String key, boolean def) {
        super(prefs, key);
        this.def = def;
    }

    @Override
    public void reset() {
        set(def);
    }

    public void set(boolean value) {
        prefs.putBoolean(key, value);
    }

    public boolean get() {
        return prefs.getBoolean(key, def);
    }
}
