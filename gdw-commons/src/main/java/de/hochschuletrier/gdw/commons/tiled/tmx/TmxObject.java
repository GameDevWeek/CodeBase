package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxObject {

    @XStreamAlias("properties")
    protected SafeProperties properties;
    @XStreamAlias("polyline")
    protected TmxPointList polyline;
    @XStreamAlias("polygon")
    protected TmxPointList polygon;
    @XStreamAlias("image")
    protected TmxImage image;
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String name;
    @XStreamAlias("type")
    @XStreamAsAttribute
    protected String type;
    @XStreamAlias("gid")
    @XStreamAsAttribute
    protected Integer gid;
    @XStreamAlias("x")
    @XStreamAsAttribute
    protected float x;
    @XStreamAlias("y")
    @XStreamAsAttribute
    protected float y;
    @XStreamAlias("width")
    @XStreamAsAttribute
    protected Float width;
    @XStreamAlias("height")
    @XStreamAsAttribute
    protected Float height;

    public SafeProperties getProperties() {
        return properties;
    }

    public void setProperties(SafeProperties value) {
        this.properties = value;
    }

    public TmxPointList getPolyline() {
        return polyline;
    }

    public void setPolyline(TmxPointList value) {
        this.polyline = value;
    }

    public TmxPointList getPolygon() {
        return polygon;
    }

    public void setPolygon(TmxPointList value) {
        this.polygon = value;
    }

    public TmxImage getImage() {
        return image;
    }

    public void setImage(TmxImage value) {
        this.image = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer value) {
        this.gid = value;
    }

    public float getX() {
        return x;
    }

    public void setX(int value) {
        this.x = value;
    }

    public float getY() {
        return y;
    }

    public void setY(int value) {
        this.y = value;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float value) {
        this.width = value;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float value) {
        this.height = value;
    }
}
