package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.devcon.DeveloperInputManager;
import de.hochschuletrier.gdw.ws1314.shaders.DemoShader;
import static de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil.batch;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState implements InputProcessor {

    public static final int WALKING_SPEED = 100;

    private Music music;
    private Sound click;
    private ImageX logo;
    private ImageX crosshair;
    private AnimationX walking;
    private float x = 0;
    private final Vector2 cursor = new Vector2();
    private boolean useShader;

    private DemoShader demoShader;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        
        logo = assetManager.getImageX("logo");
        crosshair = assetManager.getImageX("crosshair");
        walking = assetManager.getAnimationX("walking");
        music = assetManager.getMusic("menu");
        click = assetManager.getSound("click");

        music.setLooping(true);
//        music.play();
        demoShader =  new DemoShader(Gdx.files.internal("data/shaders/demo.vertex.glsl"),
				Gdx.files.internal("data/shaders/demo.fragment.glsl"));
    }

    @Override
    public void render() {
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);

        logo.draw();
        
        if(useShader)
            DrawUtil.batch.setShader(demoShader);
        walking.draw(x, 512 - walking.getHeight());
		if(useShader)
            DrawUtil.batch.setShader(null);

        crosshair.draw(cursor.x - crosshair.getWidth() * 0.5f, cursor.y - crosshair.getHeight() * 0.5f);
    }

    @Override
    public void update(float delta) {
        walking.update(delta);
        x += delta * WALKING_SPEED;
        if (x > 1024) {
            x = -walking.getWidth();
        }
    }

    @Override
    public void onEnter() {
        DeveloperInputManager.setInputProcessor(this);
    }

    @Override
    public void onLeave() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
		if (keycode == Keys.F11) {
			useShader = !useShader;
		}
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        if (button == 0) {
            click.play();
        } else {
            GameStates.GAMEPLAY.init(assetManager);
            GameStates.GAMEPLAY.activate(new SplitHorizontalTransition(500).reverse(), null);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
