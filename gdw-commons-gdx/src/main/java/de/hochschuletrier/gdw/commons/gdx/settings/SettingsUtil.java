package de.hochschuletrier.gdw.commons.gdx.settings;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author Santo Pfingsten
 */
public class SettingsUtil {

    public static void reset(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                if (Setting.class.isAssignableFrom(field.getType())) {
                    try {
                        Setting setting = (Setting) field.get(null);
                        setting.reset();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
