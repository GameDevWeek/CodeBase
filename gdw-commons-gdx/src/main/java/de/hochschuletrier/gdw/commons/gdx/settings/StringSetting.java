package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;

/**
 *
 * @author Santo Pfingsten
 */
public class StringSetting extends Setting {

    final String def;

    public StringSetting(Preferences prefs, String key, String def) {
        super(prefs, key);
        this.def = def;
    }

    public void reset() {
        set(def);
    }

    public void set(String value) {
        prefs.putString(key, value);
    }

    public String get() {
        return prefs.getString(key, def);
    }

}
