package de.hochschuletrier.gdw.ss14.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;


public class StartScreen extends LaserCatMenu implements ScreenListener {
    // For debug drawing
    private ShapeRenderer shapeRenderer; 
    
    private StartScreenListener actionListener;
    private static final Logger logger = LoggerFactory.getLogger(DogBehaviour.class);	//


    protected TextButton startButton;
    
    public void init(AssetManagerX assetManager){
    	super.init(assetManager);
        catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
        // Adjusts the table and adds it to the stage
        table.clear();
        table.background(catSkin.getDrawable("startScreen"));
        stage.addActor(table);
        table.setFillParent(true);
        startButton = new TextButton("\nStart", catSkin, "start_button");

        // Sets Input so it can reach different layers, depending on focus
        Main.inputMultiplexer.addProcessor(stage);
        actionListener= new StartScreenListener();
        startButton.addListener(this.actionListener);
        
        table.bottom();        
        table.add(startButton).size(Value.percentWidth(0.15f, table));
        //table.debug(Debug.all);
//        dummyTable.debug(Debug.all);
    }
    public void dispose(){
        stage.dispose();
    }
    public void render(){
        stage.draw();
    }
    public void update(float delta){
        // TODO Auto-generated method stub
        stage.act(Gdx.graphics.getDeltaTime());
    }

    private class StartScreenListener extends ClickListener{
        public void clicked(InputEvent event, float x, float y){
			super.clicked(event, x, y);	
        	SoundManager.performAction(UIActions.BUTTONCLICKED);						
            GameStateEnum.MAINMENU.activate();    
        }
        
        
    	public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) 
        {
        	super.enter(event, x, y, pointer, fromActor);
        	System.out.println("enter StartScreenButton");
        }
    }

	
}