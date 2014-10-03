package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Santo Pfingsten
 */
public class TextItem extends Item {
    public final TextStyle style;
    public final String originalText;
    public String shownText;

    public ArrayList<TextChar> chars;
    private TextAnimation anim;

    public TextItem(String text, TextStyle style) {
        originalText = text;
        shownText = text;
        this.style = style;
    }

    @Override
    public void update(float delta) {
        animationTime += delta;
        if (anim != null) {
            switch (anim) {
                case FADE_IN:
                    updateFadeIn();
                    break;
                case FADE_OUT:
                    updateFadeOut();
                    break;
                case CONSTRUCT:
                case CONSTRUCT_TYPE:
                    updateConstruct(delta);
                    break;
                case TYPE:
                    updateType();
                    break;
                case UNTYPE:
                    updateUntype();
                    break;
            }
        }
    }

    private void updateFadeIn() {
        opacity = animationTime / totalAnimationTime;
        if (opacity >= 1.0f) {
            opacity = 1.0f;
            anim = null;
        }
    }

    private void updateFadeOut() {
        opacity = 1.0f - (animationTime / totalAnimationTime);
        if (opacity <= 0.0f) {
            opacity = 0.0f;
            anim = null;
        }
    }

    private void updateType() {
        int numChars = (int) Math.floor(animationTime / totalAnimationTime);
        if (numChars < originalText.length()) {
            shownText = originalText.substring(0, numChars);
        } else {
            shownText = originalText;
            anim = null;
        }
    }

    private void updateUntype() {
        int numChars = originalText.length() - (int) Math.floor(animationTime / totalAnimationTime);
        if (numChars > 0) {
            shownText = originalText.substring(0, numChars);
        } else {
            shownText = "";
            anim = null;
        }
    }

    private void updateConstruct(float delta) {
        Iterator<TextChar> it = chars.iterator();
        while (it.hasNext()) {
            TextChar tc = it.next();
            if (tc.update(delta, totalAnimationTime)) {
                shownText = originalText.substring(0, shownText.length() + 1);
                it.remove();
            }
        }
    }

    private void generateConstructChars(AnimatorData.Path.Animation animation) {
        chars = new ArrayList();
        for (int i = 0; i < originalText.length(); i++) {
            chars.add(new TextChar(style.font, originalText, i, animation, totalAnimationTime));
        }
    }

    @Override
    public void render() {
        style.color.a = opacity;
        if (anim != null) {
            style.font.setColor(style.color);
            switch (anim) {
                case FADE_IN:
                case FADE_OUT:
                    style.font.draw(DrawUtil.batch, originalText, position.x, position.y);
                    break;
                case CONSTRUCT:
                case CONSTRUCT_TYPE:
                    style.font.draw(DrawUtil.batch, shownText, position.x, position.y);
                    for (TextChar tc : chars) {
                        tc.render(style.font, style.color, position, totalAnimationTime);
                    }
                    break;
                case TYPE:
                case UNTYPE:
                    style.font.draw(DrawUtil.batch, shownText, position.x, position.y);
                    break;
            }
            style.font.setScale(1);
        } else {
            style.font.setColor(style.color);
            style.font.draw(DrawUtil.batch, shownText, position.x, position.y);
        }
    }

    @Override
    public void startAnimation(AnimatorData.Path.Animation animation) {
        try {
            anim = TextAnimation.valueOf(animation.animation.toUpperCase());
            animationTime = 0;
            if (animation.animationTime != null) {
                totalAnimationTime = animation.animationTime * 0.001f;
            }
            opacity = 1.0f;
            switch (anim) {
                case CONSTRUCT:
                case CONSTRUCT_TYPE:
                    shownText = "";
                    generateConstructChars(animation);
                    break;
                case TYPE_INSTANT:
                    shownText = originalText;
                    anim = null;
                    break;
                case UNTYPE_INSTANT:
                    shownText = "";
                    anim = null;
                    break;
                case FADE_IN:
                    opacity = 0.0f;
                    break;
                case TYPE:
                    shownText = "";
                    break;
                case UNTYPE:
                    shownText = originalText;
                    break;
            }
        } catch (IllegalArgumentException e) {
        }
    }
}
