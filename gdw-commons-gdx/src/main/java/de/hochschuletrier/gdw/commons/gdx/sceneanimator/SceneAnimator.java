package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import de.hochschuletrier.gdw.commons.gdx.sceneanimator.text.TextItem;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.text.TextStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite.AnimationItem;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite.TextureItem;
import de.hochschuletrier.gdw.commons.gdx.utils.ColorUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * This is alpha, some things might not work yet..
 *
 * @author Santo Pfingsten
 */
public class SceneAnimator {

    private final HashMap<String, TextStyle> textStyles = new HashMap();
    private final HashMap<String, Queue> queues = new HashMap();
    private final Array<Queue> queueArray = new Array();
    private final HashMap<String, Path<Vector2>> paths = new HashMap();
    private final float timeFactor;

    public interface Getter {

        BitmapFont getFont(String name);

        AnimationExtended getAnimation(String name);

        Texture getTexture(String name);
    }

    public SceneAnimator(Getter getter, String filename) throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
            InstantiationException, ParseException {
        SceneAnimatorJson credits = JacksonReader.read(filename, SceneAnimatorJson.class);

        timeFactor = credits.timeFactor != null ? credits.timeFactor : 1;
        initStyles(credits, getter);
        initPaths(credits);
        initQueues(credits, getter);
    }

    private void initStyles(SceneAnimatorJson credits, Getter getter) {
        for (Map.Entry<String, SceneAnimatorJson.TextStyle> entry : credits.textStyles.entrySet()) {
            SceneAnimatorJson.TextStyle value = entry.getValue();
            BitmapFont font = getter.getFont(value.font);
            Color color = ColorUtil.fromHexString(value.color);
            textStyles.put(entry.getKey(), new TextStyle(font, color, value.align));
        }
    }

    private void initPaths(SceneAnimatorJson credits) {
        for (Map.Entry<String, SceneAnimatorJson.Path> entry : credits.paths.entrySet()) {
            SceneAnimatorJson.Path value = entry.getValue();
            ArrayList<Destination> destinations = new ArrayList();

            for (SceneAnimatorJson.Path.Destination dest : value.destinations) {
                Destination destination = new Destination();
                destination.x = dest.x != null ? dest.x : 0;
                destination.y = dest.y != null ? dest.y : 0;
                destination.speed = dest.speed != null ? dest.speed : 0;
                destinations.add(destination);
            }

            LinearPath path = new LinearPath(destinations);
            paths.put(entry.getKey(), path);
        }
    }

    private void initQueues(SceneAnimatorJson credits, Getter getter) {
        Vector2 temp = new Vector2();
        TextStyle style;
        float startTime, angle, opacity, scale;
        int layer;
        boolean oriented;
        String group;
        for (Map.Entry<String, SceneAnimatorJson.Queue> entry : credits.queues.entrySet()) {
            SceneAnimatorJson.Queue value = entry.getValue();

            float itemStartTime = 0;
            ArrayList<Item> items = new ArrayList();
            for (SceneAnimatorJson.Item itemData : value.items) {
                startTime = itemData.delay != null ? (itemData.delay * 0.001f) : 0;
                angle = itemData.angle != null ? itemData.angle : 0;
                oriented = itemData.oriented != null ? itemData.oriented : false;
                opacity = itemData.opacity != null ? itemData.opacity : 1;
                group = itemData.group != null ? itemData.group : "";
                scale = itemData.scale != null ? itemData.scale : 1;

                itemStartTime += startTime;

                Item item = null;
                switch (itemData.type) {
                    case TEXT:
                        style = textStyles.get(itemData.style);
                        item = new TextItem(group, itemStartTime, angle, opacity, itemData.text, style);

                        if (itemData.x != null && itemData.y != null) {
                            item.setPosition(temp.set(itemData.x, itemData.y));
                        }
                        break;
                    case TEXTURE:
                        item = new TextureItem(group, scale, itemStartTime, angle, oriented, opacity, itemData.resource, getter);
                        if (itemData.x != null && itemData.y != null) {
                            item.setPosition(itemData.x, itemData.y);
                        }
                        break;
                    case ANIMATION:
                        item = new AnimationItem(group, scale, itemStartTime, angle, oriented, opacity, itemData.resource, getter);
                        if (itemData.x != null && itemData.y != null) {
                            item.setPosition(itemData.x, itemData.y);
                        }
                        break;
                }
                if (item != null) {
                    item.path = (itemData.path != null) ? paths.get(itemData.path) : null;
                    items.add(item);
                }
            }

            ArrayList<Animation> animations = new ArrayList();
            if (value.animations != null) {
                for (SceneAnimatorJson.Queue.Animation anim : value.animations) {
                    Animation animation = new Animation();
                    animation.time = anim.time != null ? (anim.time * 0.001f) : 0;
                    animation.animation = anim.animation;
                    animation.animationTime = anim.animationTime != null ? (anim.animationTime * 0.001f) : 0;
                    animation.trailStepTime = anim.trailStepTime != null ? (anim.trailStepTime * 0.001f) : 0;
                    animation.trailMaxSteps = anim.trailMaxSteps != null ? anim.trailMaxSteps : 0;
                    animation.minRadius = anim.minRadius != null ? anim.minRadius : 0;
                    animation.maxRadius = anim.maxRadius != null ? anim.maxRadius : 50;
                    animation.minAngle = anim.minAngle != null ? anim.minAngle : 0;
                    animation.maxAngle = anim.maxAngle != null ? anim.maxAngle : 360;
                    animation.minCurveAngle = anim.minCurveAngle != null ? anim.minCurveAngle : 45;
                    animation.maxCurveAngle = anim.maxCurveAngle != null ? anim.maxCurveAngle : 135;
                    animation.group = anim.group != null ? anim.group : "";
                    animations.add(animation);
                }
            }

            startTime = value.time != null ? (value.time * 0.001f) : 0;
            layer = value.layer != null ? value.layer : 0;
            Queue queue = new Queue(startTime, layer, items, animations);
            queues.put(entry.getKey(), queue);
        }

        // Solve next and finalNext
        for (Map.Entry<String, SceneAnimatorJson.Queue> entry : credits.queues.entrySet()) {
            SceneAnimatorJson.Queue value = entry.getValue();
            Queue queue = queues.get(entry.getKey());
            if (value.next != null) {
                queue.next = queues.get(value.next);
            }
            if (value.finalNext != null) {
                queue.finalNext = queues.get(value.finalNext);
            }
        }

        for (Queue queue : queues.values()) {
            queueArray.add(queue);
        }
        queueArray.sort((Queue a, Queue b) -> a.layer - b.layer);
    }

    public void reset() {
        for (Queue queue : queueArray) {
            queue.reset();
        }
    }

    public void render() {
        for (Queue queue : queueArray) {
            queue.render();
        }
    }

    public void update(float delta) {
        delta *= timeFactor;
        boolean done = true;
        for (Queue queue : queueArray) {
            queue.update(delta);
            if (!queue.isDone()) {
                done = false;
            }
        }
        if (done) {
            reset();
        }
    }
}
