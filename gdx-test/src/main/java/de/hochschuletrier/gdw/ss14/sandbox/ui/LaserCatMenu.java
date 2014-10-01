package de.hochschuletrier.gdw.ss14.sandbox.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.management.GarbageCollectorMXBean;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public abstract class LaserCatMenu extends SandboxGame
{
	private static Stage stage;
	private static Image menuCatImage;
	// For debug drawing
	private ShapeRenderer shapeRenderer;

	
	//
	// Vererbtes Zeug
	//
	protected static Table widgetFrame;
	protected static Table table;
	protected Skin catSkin, basicSkin;
	
	// Abstrakte (vorgeschriebene) Attribute
	protected Button button[];
	protected Label label[];
	protected String name[];
	protected int numberOfButtons;
	
	
	@Override
	public void init(AssetManagerX assetManager)
	{
		//Variables
		
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
		table.add(widgetFrame).align(Align.center).size(Value.percentWidth(0.6f, table), Value.percentHeight(0.25f,table));
		
		// MainCat Image
		
		table.add();
		
		// Skinning and Adding the Labels
		catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
		basicSkin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		
		//catSkin.getDrawable("title");
		
		

		// Debug Lines
		shapeRenderer = new ShapeRenderer();
		table.debug(Debug.all);
		widgetFrame.debug(Debug.all);
	}
	
	protected void addButtonsToFrame()
	{
		button = new Button[numberOfButtons];
		label = new Label[numberOfButtons];

		for(int i=0; i<numberOfButtons; i++)
		{
			label[i] = new Label(name[i], basicSkin);
			widgetFrame.add(label[i]).expandX().space(20).spaceBottom(10);
		}
		
		widgetFrame.row();
		for(int i = 0; i<numberOfButtons; i++)
		{
			button[i] = new Button(catSkin, "bell");
			widgetFrame.add(button[i]).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);
		}
		name = null;
		
		
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
	}

	@Override
	public void update(float delta)
	{
		// TODO Auto-generated method stub
		stage.act(Gdx.graphics.getDeltaTime());

	}


}
