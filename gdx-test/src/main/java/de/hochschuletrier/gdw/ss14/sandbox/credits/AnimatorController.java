package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.sandbox.credits.AnimatorData.Path.Animation;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimatorController {
    private final HashMap<String, TextStyle> textStyles = new HashMap();
    private final HashMap<String, Queue> queues = new HashMap();
    private final HashMap<String, Path> paths = new HashMap();

    private static class TextStyle {
        public Color color;
        public BitmapFont font;
        public Align align;
    }

    private static class Queue {
        public Path path;
        public int time;
        public Queue next;
        public Queue finalNext;
        public ArrayList<Item> items;
    }

    private static class Path {
        public ArrayList<Destination> destinations;
        public ArrayList<Animation> animations;
        
        private static class Destination {
            public final Vector2 position;
            public final float speed;
            
            public Destination(float x, float y, float speed) {
                position = new Vector2(x, y);
                this.speed = speed;
            }
        }
    }

    private static abstract class Item {
        public int startTime;
        public int animationTime;
        public Vector2 position;
        public float angle;
        
        public abstract void update(float deltaTime);
        public abstract void render();
        public abstract void startAnimation(Animation animation);
    }

    public static enum TextAnimation {
        FADE_IN,
        FADE_OUT,
        CONSTRUCT,
        TYPE,
        UNTYPE,
        UNTYPE_INSTANT
    }
    private static class TextItem extends Item {
        public float opacity;
        public int frametime;
        public String shownText;
        public String originalText;
        public Color color;
        public BitmapFont font;
        public TextStyle style;
        public ArrayList<TextChar> chars;
        private TextAnimation anim;
        
        @Override
        public void update(float delta) {
            animationTime += delta;
            if(anim != null) {
                switch(anim) {
                    case FADE_IN:
                        //todo: add opacity
                        if(opacity >= 1.0f) {
                            opacity = 1.0f;
                            anim = null;
                        }
                        break;
                    case FADE_OUT:
                        //todo: subtract opacity
                        if(opacity <= 0.0f) {
                            opacity = 0.0f;
                            anim = null;
                        }
                        break;
                    case CONSTRUCT:
                        break;
                    case TYPE:
                        break;
                    case UNTYPE:
                        break;
                }
            }
        }
        
        @Override
        public void render() {
            
        }
        
        @Override
        public void startAnimation(Animation animation) {
            try { 
                anim = TextAnimation.valueOf(animation.animation.toUpperCase());
                switch(anim) {
                    case CONSTRUCT:
                        break;
                    case UNTYPE_INSTANT:
                        shownText = originalText;
                        anim = null;
                        break;
                }
            } catch(IllegalArgumentException e) {
            }
        }
    }

    private static class TextChar {
        public Vector position;
        public String text;
    }

    private static class AnimationItem extends Item {
        public float scale;
        Texture texture;
        AnimationExtended animation;
        
        @Override
        public void update(float deltaTime) {
            
        }
        
        @Override
        public void render() {
            if(texture != null) {
                float w = texture.getWidth() * scale;
                float h = texture.getHeight() * scale;
                DrawUtil.batch.draw(texture, position.x-w*0.5f, position.y-h*0.5f, w, h);
            }
        }
        
        @Override
        public void startAnimation(Animation animation) {
            
        }
    }
}
