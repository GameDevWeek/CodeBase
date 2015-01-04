package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Setting {

    protected final Preferences prefs;
    protected final String key;

    public Setting(Preferences prefs, String key) {
        this.prefs = prefs;
        this.key = key;
    }

    public abstract void reset();
}
