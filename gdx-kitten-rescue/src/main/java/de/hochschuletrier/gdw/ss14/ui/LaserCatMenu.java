package de.hochschuletrier.gdw.ss14.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;


public abstract class LaserCatMenu implements ScreenListener
{
	protected UIImage menuCatImage;
	protected Image titleTextImage;
	// For debug drawing
	protected ShapeRenderer shapeRenderer; 
    private static final Logger logger = LoggerFactory.getLogger(DogBehaviour.class);	//
    
	// Vererbtes Zeug
	//
	protected Table widgetFrame;
	protected SoundListener soundListener;
	protected Table table;
	protected static Skin catSkin;
	protected Stage stage;
	protected float heightOfWidgetFrame;
	protected float widthOfWidgetFrame;
	protected float frameDuration;

	
	// Abstrakte (vorgeschriebene) Attribute
	protected UIButton button[];
	protected Label label[];
	protected String name[];
	protected int numberOfButtons;
	
	public void init(AssetManagerX assetManager)
	{
		//Variables
		heightOfWidgetFrame = 0.25f;
		widthOfWidgetFrame = 0.65f;
		frameDuration = 0.05f;
				
		// Adjusts the table and adds it to the stage
		Viewport viewport = new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),new OrthographicCamera());
		stage = new Stage(viewport);
		
		// Space for main-menu, including background
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
		
	
		// Sets Input so it can reach different layers, depending on focus
		Main.inputMultiplexer.addProcessor(stage);
		
		// Sets the Background for every Menu
		table.setBackground(catSkin.getDrawable("main-menu-background"));

		
		//TitleTextImage
		titleTextImage= new Image(catSkin.getDrawable("game-title"));
		table.add(titleTextImage).top().size(Value.percentWidth(0.8f,  table),Value.percentHeight(0.25f, table)).expandX();
		table.row();
		
		// container for center labels and buttons, no background of its own
		widgetFrame = new Table();
		table.add(widgetFrame).bottom().size(Value.percentWidth(widthOfWidgetFrame, table), Value.percentHeight(heightOfWidgetFrame,table));
		table.row();
		
		// MainCat Image
		menuCatImage = new UIImage(catSkin.getDrawable("main-menu-cat"));
		menuCatImage.setAnimation(catSkin, "menuCat", 0.1f);
		table.add(menuCatImage).bottom().expandY();

		// Debug Lines
		shapeRenderer = new ShapeRenderer();
		table.debug(Debug.all);
		widgetFrame.debug(Debug.all);
		
		soundListener=new SoundListener();
		
		Main.getInstance().addScreenListener(this);
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
			if(event.getListenerActor().getName().equals("bell"))
				SoundManager.performAction(UIActions.BELLCLICKED);
			else
				SoundManager.performAction(UIActions.BUTTONCLICKED);						
		}
		public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor)
		{
			super.enter(event, x, y, pointer, fromActor);
			
			if (this.isPressed())
					return;
			System.out.println("enter");
                        menuCatImage.animate(true);
			if(event.getListenerActor().getName().equals("bell"))
				SoundManager.performAction(UIActions.BELLOVER);
			else
				SoundManager.performAction(UIActions.BUTTONOVER);
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer,
				Actor toActor)
		{

			super.exit(event, x, y, pointer, toActor);
			//System.out.println("exit");
            menuCatImage.animate(false);

			
		}
	}
	
	@Override
	public void resize(int width, int height) {
	    // TODO Auto-generated method stub
	    logger.debug("Changing viewport to width: " + width + "height: " + height);
	    stage.getViewport().update(width, height, true);
	}
	
	
}
