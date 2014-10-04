package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import de.hochschuletrier.gdw.commons.jackson.JacksonList;
import de.hochschuletrier.gdw.commons.jackson.JacksonMap;
import java.util.List;
import java.util.Map;

public class AnimatorData {
    @JacksonMap(TextStyle.class)
    public Map<String, TextStyle> textStyles;
    @JacksonMap(Path.class)
    public Map<String, Path> paths;
    @JacksonMap(Queue.class)
    public Map<String, Queue> queues;

    public static class TextStyle {
        public String color;
        public String font;
        public Integer size;
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
        public Integer time;
        public String path;
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
            // For text explosion animation
            public Integer minRadius;
            public Integer maxRadius;
            public Integer minAngle;
            public Integer maxAngle;
            public Integer minCurveAngle;
            public Integer maxCurveAngle;
            public Integer animationTime;
        }
    }

    public static class Item {
        public Float opacity;
        public ItemType type;
        public Integer delay;
        public Integer x;
        public Integer y;
        // For text:
        public String style;
        public String text;
        // For animations
        public String animation;
        public Float scale;
        public Float angle;
        public Boolean oriented;
    }
}