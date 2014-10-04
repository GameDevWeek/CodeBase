package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.ColorUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimatorController {

    private final HashMap<String, TextStyle> textStyles = new HashMap();
    private final HashMap<String, Queue> queues = new HashMap();
    private final HashMap<String, Path> paths = new HashMap();
    
    public AnimatorController(AssetManagerX assetManager, String filename) throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
            InstantiationException, ParseException {
        AnimatorData credits = JacksonReader.read(filename, AnimatorData.class);

        initStyles(credits, assetManager);
        initPaths(credits);
        initQueues(credits);
    }

    private void initStyles(AnimatorData credits, AssetManagerX assetManager) {
        for(Map.Entry<String, AnimatorData.TextStyle> entry: credits.textStyles.entrySet()) {
            AnimatorData.TextStyle value = entry.getValue();
            BitmapFont font = assetManager.getFont(value.font, value.size);
            Color color = ColorUtil.fromHexString(value.color);
            textStyles.put(entry.getKey(), new TextStyle(font, color, value.align));
        }
    }

    private void initPaths(AnimatorData credits) {
        for(Map.Entry<String, AnimatorData.Path> entry: credits.paths.entrySet()) {
            AnimatorData.Path value = entry.getValue();
            Path path = new Path();
            
            for(AnimatorData.Path.Destination dest: value.destinations) {
                Destination destination = new Destination();
                destination.x = dest.x != null ? dest.x : 0;
                destination.y = dest.y != null ? dest.y : 0;
                path.addDestination(destination);
            }
            
            for(AnimatorData.Path.Animation anim: value.animations) {
                Animation animation = new Animation();
                animation.time = anim.time != null ? (anim.time * 0.001f) : 0;
                animation.animation = anim.animation;
                animation.animationTime = anim.animationTime != null ? (anim.animationTime * 0.001f) : 0;
                animation.frametime = anim.frametime != null ? (anim.frametime * 0.001f) : 0;
                animation.minRadius = anim.minRadius != null ? anim.minRadius : 0;
                animation.maxRadius = anim.maxRadius != null ? anim.maxRadius : 50;
                animation.minAngle = anim.minAngle != null ? anim.minAngle : 0;
                animation.maxAngle = anim.maxAngle != null ? anim.maxAngle : 360;
                animation.minCurveAngle = anim.minCurveAngle != null ? anim.minCurveAngle : 45;
                animation.maxCurveAngle = anim.maxCurveAngle != null ? anim.maxCurveAngle : 135;
                path.addAnimation(animation);
            }
            paths.put(entry.getKey(), path);
        }
    }

    private void initQueues(AnimatorData credits) {
        TextStyle style;
        float startTime, angle, opacity;
        for(Map.Entry<String, AnimatorData.Queue> entry: credits.queues.entrySet()) {
            AnimatorData.Queue value = entry.getValue();
            
            ArrayList<Item> items = new ArrayList();
            for(AnimatorData.Item item: value.items) {
                startTime = item.delay != null ? (item.delay * 0.001f) : 0;
                angle = item.angle != null ? item.angle : 0;
                opacity = item.opacity != null ? item.opacity : 1;
                
                switch(item.type) {
                    case TEXT:
                        style = textStyles.get(item.style);
                        items.add(new TextItem(item.text, style, startTime, angle, opacity));
                        break;
                    case SPRITE:
                        break;
                }
            }
            startTime = value.time != null ? (value.time * 0.001f) : 0;
            Path path = (value.path != null) ? paths.get(value.path) : null;
            Queue queue = new Queue(startTime, path, items);
            queues.put(entry.getKey(), queue);
        }
        
        // Solve next and finalNext
        for(Map.Entry<String, AnimatorData.Queue> entry: credits.queues.entrySet()) {
            AnimatorData.Queue value = entry.getValue();
            Queue queue = queues.get(entry.getKey());
            if(value.next != null)
                queue.next = queues.get(value.next);
            if(value.finalNext != null)
                queue.finalNext = queues.get(value.finalNext);
        }
    }
    
    public void reset() {
        for(Queue queue: queues.values()) {
            queue.reset();
        }
    }
}
