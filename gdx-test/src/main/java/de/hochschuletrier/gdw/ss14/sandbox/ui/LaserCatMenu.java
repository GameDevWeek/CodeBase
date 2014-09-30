package de.hochschuletrier.gdw.ss14.sandbox.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.MouseInputListener;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class LaserCatMenu extends SandboxGame
{
    private Stage stage;
    private Table table;
    private Skin skin;
    private Drawable menuBackground;
    private TextButton button_start, button_options, button_exit;
    // For debug drawing
    private ShapeRenderer shapeRenderer;
    
    @Override
    public void init(AssetManagerX assetManager) {
        
        
        // Adjusts the table and adds it to the stage
        stage = new Stage();
        //Gdx.input.setInputProcessor(stage);
        Main.inputMultiplexer.addProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
        shapeRenderer = new ShapeRenderer();
        // Sets the Background
         //table.setBackground(assetManager.getTexture("menuBackground"));
        // Skinny Stuff
        skin = new Skin(Gdx.files.internal("data/skins/basic.json"));        
        button_start = new TextButton("Game Start", skin);
        button_options = new TextButton("Options", skin);
        button_exit = new TextButton("Exit", skin);
        
        menuBackground = skin.getDrawable("dialogDim");

        table.setBackground(menuBackground);
        
        table.add(button_start).expandX();
        table.row();
        table.add(button_options);
        table.row();
        table.add(button_exit);
        
        table.debug(Debug.all);
        
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void render() {
       // Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
        stage.draw();

        //table.drawDebug(shapeRenderer);
        //DrawUtil.batch.draw(menuBackground, 0, 0, menuBackground.getWidth(), menuBackground.getHeight(), 0, 0,
          //      menuBackground.getWidth(), menuBackground.getHeight(), false, true);
        
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        //stage.act(Gdx.graphics.getDeltaTime());

    }   
    
    

//    public void resize (int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
    

}
