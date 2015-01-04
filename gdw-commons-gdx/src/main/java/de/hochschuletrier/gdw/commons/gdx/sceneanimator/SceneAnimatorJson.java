package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import de.hochschuletrier.gdw.commons.gdx.sceneanimator.text.TextAlign;
import de.hochschuletrier.gdw.commons.jackson.JacksonList;
import de.hochschuletrier.gdw.commons.jackson.JacksonMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Santo Pfingsten
 */
public class SceneAnimatorJson {

    public Float timeFactor;
    @JacksonMap(TextStyle.class)
    public Map<String, TextStyle> textStyles;
    @JacksonMap(Path.class)
    public Map<String, Path> paths;
    @JacksonMap(Queue.class)
    public Map<String, Queue> queues;

    public static class TextStyle {

        public String color;
        public String font;
        public TextAlign align;
    }

    public static class Path {

        PathType type;
        @JacksonList(Destination.class)
        public List<Destination> destinations;

        public static class Destination {

            public Float x;
            public Float y;
            public Float speed;
        }
    }

    public static class Queue {

        public Integer layer;
        public Integer time;
        public String next;
        public String finalNext;
        @JacksonList(Item.class)
        public List<Item> items;
        @JacksonList(Animation.class)
        public List<Animation> animations;

        public static class Animation {

            public Integer time;
            public String animation;
            public Integer frametime;
            public String group;
            // For text explosion animation
            public Integer minRadius;
            public Integer maxRadius;
            public Integer minAngle;
            public Integer maxAngle;
            public Integer minCurveAngle;
            public Integer maxCurveAngle;
            public Integer animationTime;
            public Integer trailStepTime;
            public Integer trailMaxSteps;
        }
    }

    public static class Item {

        public String path;
        public String group;
        public Float opacity;
        public ItemType type;
        public Integer delay;
        public Integer x;
        public Integer y;
        // For text:
        public String style;
        public String text;
        // For animations
        public String resource;
        public Float scale;
        public Float angle;
        public Boolean oriented;
    }
}
