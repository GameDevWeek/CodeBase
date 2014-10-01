package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxTileOffset {
    @XStreamAlias("source")
    @XStreamAsAttribute
    protected Integer x;
    @XStreamAlias("trans")
    @XStreamAsAttribute
    protected Integer y;
}
