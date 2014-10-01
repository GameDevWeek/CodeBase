//package de.hochschuletrier.gdw.ss14.sandbox.credits;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class CreditsAnimation {
//
//    private List<Widget> widgets = new ArrayList<>();
//    private boolean reset = false;
//    private CreditsInfo credits;
//    private HashMap<String, ArrayList<PathMover.Destination>> paths = new HashMap<>();
//    private HashMap<String, Style> styles = new HashMap<>();
//    private int numMovers = 0;
//    private AssetManagerX assetManager;
//
//    private class Style {
//
//        public ArrayList<PathMover.Destination> path;
//        public Color color;
//        public BitmapFont font;
//
//        public Style(String path, String color, String font, int size) {
//            this.path = paths.get(path);
//            this.color = new Color(Integer.parseInt(color, 16));
//            this.font = assetManager.getFont(font, size);
//        }
//    }
//
//    public void addWidget(Widget w) {
//        widgets.add(w);
//    }
//
//    public void init(AssetManagerX assetManager) {
//        this.assetManager = assetManager;
//        credits = AssetLoader.getInstance().getCredits();
//        for (CreditsInfo.Path path : credits.paths) {
//            ArrayList<PathMover.Destination> list = new ArrayList<>();
//            paths.put(path.name, list);
//            for (CreditsInfo.Path.Destination dest : path.destinations) {
//                list.add(new PathMover.Destination(dest.x, dest.y, dest.speed, dest.delay == null ? 0 : dest.delay));
//            }
//        }
//
//        for (CreditsInfo.Style style : credits.styles) {
//            styles.put(style.name, new Style(style.path, style.color, style.font));
//        }
//        reset();
//    }
//
//    public void render() {
//        for (Widget w : widgets) {
//            w.render();
//        }
//
//        if (reset) {
//            try {
//                reset();
//            } catch (Exception e) {
//                widgets.clear();
//            }
//        }
//    }
//
//    public final void reset() {
//        reset = false;
//        widgets.clear();
//        numMovers = 0;
//
//        PathMover mover;
//        int delay = 0;
//        for (CreditsInfo.Text text : credits.texts) {
//            Style style = styles.get(text.style);
//            if (style != null) {
//                delay += text.delay;
//                mover = new PathMover(style.path, false).delay(delay);
//                mover.done(this);
//                numMovers++;
//
//                String str = text.value;
//                Label l = Label.create(str, 0, 0, style.font.getWidth(str), 20).font(style.font).color(style.color).align(Align.CENTER).follow(mover);
//
//                addWidget(l);
//            }
//        }
//
//        for (CreditsInfo.Animation anim : credits.animations) {
//
//            if (anim.animation) {
//                Animation animation = al.getAnimation(anim.name);
//                Animated w = Animated.create(animation, anim.x, anim.y, anim.width, anim.height).angle(anim.angle);
//                ArrayList<PathMover.Destination> path = paths.get(anim.path);
//                if (path != null) {
//                    mover = new PathMover(path, anim.oriented).delay(anim.delay);
//                    mover.done(this);
//                    numMovers++;
//                    w.follow(mover);
//                }
//                addWidget(w);
//            } else {
//                Image image = al.getImage(anim.name);
//                Label w = Label.create("", anim.x, anim.y, anim.width, anim.height)
//                        .image(image).angle(anim.angle);
//
//                ArrayList<PathMover.Destination> path = paths.get(anim.path);
//                if (path != null) {
//                    mover = new PathMover(path, anim.oriented).delay(anim.delay);
//                    mover.done(this);
//                    numMovers++;
//                    w.follow(mover);
//                }
//                addWidget(w);
//            }
//        }
//    }
//
//    @Override
//    public void onAction() {
//        numMovers--;
//        if (numMovers <= 0) {
//            reset = true;
//        }
//    }
//}
