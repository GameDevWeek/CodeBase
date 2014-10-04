package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.GameStateEnum;


public class StartSreen {
    // For debug drawing
    private ShapeRenderer shapeRenderer; 
    
    protected StartScreenListener actionListener;
    protected Table table;
    protected Skin catSkin;
    protected Stage stage;

    protected TextButton startButton;
    
    public void init(AssetManagerX assetManager){
        // Adjusts the table and adds it to the stage
        stage = new Stage();
        
        // Adjusts the table and adds it to the stage
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
        startButton = new TextButton("Start", catSkin, "start_button");

        // Sets Input so it can reach different layers, depending on focus
        Main.inputMultiplexer.addProcessor(stage);
        startButton.addListener(this.actionListener);
        
        // Sets the Background for Menu
        table.setBackground(catSkin.getDrawable("startScreen"));
        table.add(startButton).bottom();
        table.debug(Debug.all);
    }
    public void dispose(){
        stage.dispose();
        shapeRenderer.dispose();
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
                GameStateEnum.MAINMENU.activate();              
        }
    }

}