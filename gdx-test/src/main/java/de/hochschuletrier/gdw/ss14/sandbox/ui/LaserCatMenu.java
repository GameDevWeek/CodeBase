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

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class LaserCatMenu extends SandboxGame
{
	private Stage stage;
	private Table table;
	private Skin skin;
	private Texture menuBackground;
	private TextButton button_start, button_options, button_exit;
	// For debug drawing
	private ShapeRenderer shapeRenderer;

	@Override
	public void init(AssetManagerX assetManager)
	{
		// Adjusts the table and adds it to the stage
		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		table.center();
		stage.addActor(table);
		shapeRenderer = new ShapeRenderer();
		
		
		//Sets Input to all Instances
		//Gdx.input.setInputProcessor(stage);
		Main.inputMultiplexer.addProcessor(stage);
		
	
		// Sets the Background
		// menuBackground = assetManager.getTexture("menuBackground");
		// table.setBackground(assetManager.getTexture("menuBackground"));

		// Skinning the buttons
		skin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
		button_start = new TextButton("Game Start", skin);
		button_options = new TextButton("Options", skin);
		button_exit = new TextButton("Exit", skin);
		// button_exit.addListener(new MouseListener());

		table.add(button_start).spaceRight(20);
		table.add(button_options).spaceRight(20);
		table.add(button_exit);

		table.debug(Debug.all);
	}

	@Override
	public void dispose()
	{
		stage.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void render()
	{
		stage.draw();

		//table.drawDebug(shapeRenderer);
		
		// Taken from Santos MainMenuState-Class
		
//		Main.getInstance().screenCamera.bind();
//		DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);
//        DrawUtil.batch.draw(menuBackground, 0, 0, menuBackground.getWidth(), menuBackground.getHeight(), 0, 0,
//                menuBackground.getWidth(), menuBackground.getHeight(), false, true);
		
	
		// DrawUtil.batch.draw(menuBackground, 0, 0, menuBackground.getWidth(),
		// menuBackground.getHeight(), 0, 0,
		// menuBackground.getWidth(), menuBackground.getHeight(), false, true);

	}

	@Override
	public void update(float delta)
	{
		// TODO Auto-generated method stub
		stage.act(Gdx.graphics.getDeltaTime());

	}

	// public void resize (int width, int height) {
	// stage.getViewport().update(width, height, true);
	// }

}
