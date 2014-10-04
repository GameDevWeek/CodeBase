package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.hud.IngameHUD;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.sound.CatSoundListener;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends KittenGameState implements InputProcessor {

    private Game game;
    private Sound helicopter;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private LocalMusic music, hurryMusic;

    private final SoundEmitter emitter = new SoundEmitter();
	private static CatSoundListener playerSoundListener;
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private final Vector2 position = new Vector2(100, 100);
    private float totalMapWidth, totalMapHeight;
    
    private IngameHUD ingameHUD;

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        helicopter = assetManager.getSound("ouchWall");
        InputManager.init();
        game = new Game(assetManager);
        game.init(assetManager);
        this.music = Main.MusicManager.getMusicStreamByStateName(GameStateEnum.GAMEPLAY);
        this.playerSoundListener = new CatSoundListener(assetManager);
        Main.inputMultiplexer.addProcessor(this);
       
        ingameHUD = new IngameHUD(assetManager);
        
        // Setup camera
        /*TiledMap map = game.getMap();
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);*/
    }

    @Override
    public void render() {
    	
    	// camera.bind();
    	
    	
        /*camera.bind();

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);

        game.render();
        DrawUtil.fillRect(position.x - 10, position.y -10, 20, 20, Color.RED);*/
        game.render();        
        ingameHUD.render();
    }

    @Override
    public void update(float delta) {
        InputManager.getInstance().update(); 
    	this.music.update();
        /*emitter.update();
        emitter.setPosition(cursor.x, cursor.y, 0);
        game.update(delta);
        camera.update(delta);
        
        float speed = 1000.0f;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            position.x -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            position.x += delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            position.y -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            position.y += delta * speed;

        position.x = Math.max(10, Math.min(totalMapWidth-10, position.x));
        position.y = Math.max(10, Math.min(totalMapHeight-10, position.y));
        camera.setDestination(position);*/
        
        game.update(delta);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter(KittenGameState previousState) {
		if (this.music.isMusicPlaying()) {
			this.music.setFade('i', 3000);
		} else {
			this.music.play("ingame_calm");
		}
        emitter.dispose();
        InputManager.getInstance().registerProcessor();
        Gdx.input.setCursorCatched(true);
        //emitter.play(helicopter, true);
    }

    @Override
    public void onLeave(KittenGameState nextState) {
		if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 3000);
		}
        emitter.dispose();
        InputManager.getInstance().unregisterProcessor();
        Gdx.input.setCursorImage(null, 0, 0);
        Gdx.input.setCursorCatched(false);
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
        return false;
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
