package de.hochschuletrier.gdw.commons.gdx.settings;

import com.badlogic.gdx.Preferences;
import java.util.HashSet;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Setting<T> {

    private final HashSet<SettingListener<T>> listeners = new HashSet();
    protected final Preferences prefs;
    protected final String key;

    public Setting(Preferences prefs, String key) {
        this.prefs = prefs;
        this.key = key;
    }

    public abstract void reset();
    
    public void addListener(SettingListener<T> listener) {
        listeners.add(listener);
    }
    
    public void removeListener(SettingListener<T> listener) {
        listeners.remove(listener);
    }
    
    protected void notifyListeners(T value) {
        for (SettingListener<T> listener : listeners) {
            listener.onSettingChanged(this, value);
        }
    }
}
