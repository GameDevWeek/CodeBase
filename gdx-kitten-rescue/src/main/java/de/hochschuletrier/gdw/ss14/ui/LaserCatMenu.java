package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;

public abstract class LaserCatMenu
{
	private static Image menuCatImage, titleTextImage;
	// For debug drawing
	private ShapeRenderer shapeRenderer; 
	protected static SoundListener soundListener;

	
	//
	// Vererbtes Zeug
	//
	protected static Table widgetFrame;
	protected static Table table;
	protected Skin catSkin, basicSkin;
	protected static Stage stage;
	protected static float heightOfWidgetFrame;
	protected static float widthOfWidgetFrame;

	
	// Abstrakte (vorgeschriebene) Attribute
	protected Button button[];
	protected Label label[];
	protected String name[];
	protected int numberOfButtons;
	
	public void init(AssetManagerX assetManager)
	{
		//Variables
		heightOfWidgetFrame = 0.25f;
		widthOfWidgetFrame = 0.8f;
		
		
		// Adjusts the table and adds it to the stage
		stage = new Stage();
		
		// Space for main-menu, including background
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
		basicSkin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		
	
		// Sets Input so it can reach different layers, depending on focus
		Main.inputMultiplexer.addProcessor(stage);
		
		// Sets the Background for every Menu
		table.setBackground(catSkin.getDrawable("main-menu-background"));

		
		//TitleTextImage
		titleTextImage= new Image(catSkin.getDrawable("print-exemple"));
		table.add(titleTextImage).top().size(Value.percentWidth(0.8f,  table),Value.percentHeight(0.25f, table)).expandX();
		table.row();
		
		// container for center labels and buttons, no background of its own
		widgetFrame = new Table();
		table.add(widgetFrame).align(Align.center).size(Value.percentWidth(widthOfWidgetFrame, table), Value.percentHeight(heightOfWidgetFrame,table)).space(20);
		table.row();

	
		//catSkin.getDrawable("title");
		
		// MainCat Image
		menuCatImage = new Image(catSkin.getDrawable("main-menu-cat"));
		table.add(menuCatImage).bottom().expandY();

		// Debug Lines
		shapeRenderer = new ShapeRenderer();
		table.debug(Debug.all);
		//widgetFrame.debug(Debug.all);
		
		LaserCatMenu.soundListener=new SoundListener();
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
			widgetFrame.add(button[i]).size(Value.percentWidth(widthOfWidgetFrame/numberOfButtons, table)).top().space(20).spaceTop(10);
		}
		name = null;
}

	public void dispose()
	{
		stage.dispose();
		shapeRenderer.dispose();
	}

	public void render()
	{
		stage.draw();
	}

	public void update(float delta)
	{
		// TODO Auto-generated method stub
		stage.act(Gdx.graphics.getDeltaTime());
	}
	
	
	protected class SoundListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			SoundManager.performAction(UIActions.BELLCLICKED);
		}
		public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor)
		{
			if (!this.isPressed())
				SoundManager.performAction(UIActions.BELLOVER);
		}
	}
	
}
