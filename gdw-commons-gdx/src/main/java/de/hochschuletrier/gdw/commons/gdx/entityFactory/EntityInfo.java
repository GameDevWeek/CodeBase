package de.hochschuletrier.gdw.commons.gdx.entityFactory;

import de.hochschuletrier.gdw.commons.jackson.JacksonMap;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import java.util.Map;

/**
 *
 * @author Santo Pfingsten
 */
public class EntityInfo {

    public SafeProperties meta;
    @JacksonMap(value = SafeProperties.class)
    public Map<String, SafeProperties> components;
}
