package de.hochschuletrier.gdw.commons.gdx.settings;

/**
 *
 * @author Santo Pfingsten
 */
public interface SettingListener<T> {

    public void onSettingChanged(Setting setting, T value);
}
