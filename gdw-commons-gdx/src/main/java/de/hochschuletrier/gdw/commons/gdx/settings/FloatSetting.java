package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;

/**
 *
 * @author Santo Pfingsten
 */
public class FloatSetting extends Setting {

    final float def;

    public FloatSetting(Preferences prefs, String key, float def) {
        super(prefs, key);
        this.def = def;
    }

    @Override
    public void reset() {
        set(def);
    }

    public void set(float value) {
        prefs.putFloat(key, value);
    }

    public float get() {
        return prefs.getFloat(key, def);
    }
}
