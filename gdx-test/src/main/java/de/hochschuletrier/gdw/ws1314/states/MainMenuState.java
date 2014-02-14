package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;

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
    private FontX verdana_24;
    private float x = 0;
    private final Vector2 cursor = new Vector2();
    private FpsCalculator fpsCalc = new FpsCalculator(200);

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
        verdana_24 = assetManager.getFontX("verdana_24");
        
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render() {
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);
        
        logo.draw();
        walking.draw(x, 512-walking.getHeight());
        
        crosshair.draw(cursor.x - crosshair.getWidth()*0.5f, cursor.y - crosshair.getHeight()*0.5f);
        
        verdana_24.drawRight(String.format("%.2f",fpsCalc.getFps()), Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - verdana_24.getLineHeight());
    }

    @Override
    public void update(float delta) {
        fpsCalc.addFrame();
        x += delta * WALKING_SPEED;
        if(x > 1024)
            x = -walking.getWidth();
    }

    @Override
    public void enter() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void leave() {
    }
    
    @Override
    public void dispose() {
        logo.dispose();
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        click.play();
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}