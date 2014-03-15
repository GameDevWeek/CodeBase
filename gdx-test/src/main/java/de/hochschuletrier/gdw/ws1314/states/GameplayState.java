package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.DefaultOrthoCameraController;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.game.Game;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class GameplayState extends GameState implements InputProcessor {

    private Game game;
    private Sound click;
	private Texture crosshair;
	private BitmapFont verdana_24;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private DefaultOrthoCameraController controller;

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
		crosshair = assetManager.getTexture("crosshair");
        click = assetManager.getSound("click");
        verdana_24 = assetManager.getFontX("verdana_24");
        controller = new DefaultOrthoCameraController(Main.getInstance().getCamera());
		DrawUtil.batch.setProjectionMatrix(Main.getInstance().getCamera().combined);
        game = new Game();
        Main.inputMultiplexer.addProcessor(this);
        Main.inputMultiplexer.addProcessor(controller);
    }

    @Override
    public void render() {
		DrawUtil.batch.setProjectionMatrix(Main.getInstance().getCamera().combined);

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);

		DrawUtil.batch.draw(crosshair, cursor.x - crosshair.getWidth() * 0.5f, cursor.y
				- crosshair.getHeight() * 0.5f);

        DrawUtil.batch.draw(game.getVase().getRegion(), game.getVase().getPosition().x,
                game.getVase().getPosition().y, 0f, 0f, game.getVase().getRegion()
                .getRegionWidth(), game.getVase().getRegion().getRegionHeight(),
                game.getManager().scaleInv, game.getManager().scaleInv, game.getVase()
                .getRotation());

        game.render();
    }

    @Override
    public void update(float delta) {
        controller.update();
		controller.update();
        game.update(delta);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
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
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0)
            game.addBall(screenX, screenY);
        else
            game.getVase().setPosition(new Vector2(screenX, screenY));
		click.play();
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
