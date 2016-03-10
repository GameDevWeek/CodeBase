package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("terraintypes")
public class TmxTerrainTypes extends TmxLayerBase {

    @XStreamImplicit(itemFieldName = "terrain")
    protected List<TmxTerrain> objects;

    public List<TmxTerrain> getObjects() {
        if (objects == null) {
            objects = new ArrayList();
        }
        return this.objects;
    }
}
