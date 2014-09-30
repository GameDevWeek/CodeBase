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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class LaserCatMenu extends SandboxGame
{
	private Stage stage;
	private Table table;
	private Table widgetFrame;
	private Skin catSkin, basicSkin;
	private Texture menuBackground;
	private TextButton button_start, button_options, button_exit;
	private Label label_start, label_options, label_exit;
	
	// Variables
	private int numberOfButtons;
	private int horizontalSpaces;
	
	// For debug drawing
	private ShapeRenderer shapeRenderer;

	@Override
	public void init(AssetManagerX assetManager)
	{
		//Variables
		numberOfButtons = 3;
		
		// Adjusts the table and adds it to the stage
		stage = new Stage();
		
		// Space for main-menu, including background
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
	
		// Sets Input so it can reach different layers, depending on focus
		Main.inputMultiplexer.addProcessor(stage);
		
		// container for center labels and buttons, no background of its own
		widgetFrame = new Table();
		table.add(widgetFrame).align(Align.center).size(Value.percentWidth(0.75f, table), Value.percentHeight(0.25f,table));
		
		// Skinning and Adding the Labels
		catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
		basicSkin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		
		//catSkin.getDrawable("title");
		
		label_start = new Label("Start", basicSkin);
		label_options = new Label("Options", basicSkin);
		label_exit = new Label("Exit", basicSkin);
		widgetFrame.add(label_start).expandX();
		widgetFrame.add(label_options).expandX();
		widgetFrame.add(label_exit).expandX();
		widgetFrame.row();

		// Skinning and adding the buttons
		button_start = new TextButton("Game Start", basicSkin);
		button_options = new TextButton("Options", basicSkin);
		button_exit = new TextButton("Exit", basicSkin);
		widgetFrame.add(button_start).expandY();
		widgetFrame.add(button_options).expandY();
		widgetFrame.add(button_exit).expandY();


		// Debug Lines
		shapeRenderer = new ShapeRenderer();
		table.debug(Debug.all);
		widgetFrame.debug(Debug.all);
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


}
