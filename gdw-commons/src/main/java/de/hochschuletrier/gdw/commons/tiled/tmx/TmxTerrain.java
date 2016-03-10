package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxTerrain {
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String name;
    @XStreamAlias("tile")
    @XStreamAsAttribute
    protected Integer tile;
    
    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Integer getTile() {
        return tile;
    }

    public void setTile(Integer value) {
        this.tile = value;
    }
}
