package de.hochschuletrier.gdw.ss14.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class MainMenu extends LaserCatMenu
{
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Start";
		name[1] = "Levels";
		name[2] = "Options";
		name[3] = "Exit";
		addButtonsToFrame();
			
		
//		label_start = new Label("Start", basicSkin);
//		label_options = new Label("Options", basicSkin);
//		label_exit = new Label("Exit", basicSkin);
//		widgetFrame.add(label_start).expandX().space(20).spaceBottom(10);
//		widgetFrame.add(label_options).expandX().space(20).spaceBottom(10);
//		widgetFrame.add(label_exit).expandX().space(20).spaceBottom(10);
//		
//		Label testLabel1 = new Label("Test", basicSkin);	
//		widgetFrame.add(testLabel1).expandX().space(20).spaceBottom(10);
//		
//		Label testLabel2 = new Label("Test", basicSkin);
//		widgetFrame.add(testLabel2).expandX().space(20).spaceBottom(10);
//		
//		widgetFrame.row();
//
//		// Skinning and adding the buttons
//		button_start = new Button(catSkin, "bell");
//		button_options = new Button(catSkin, "bell");
//		button_exit = new Button(catSkin, "bell");
//		widgetFrame.add(button_start).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);
//		widgetFrame.add(button_options).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);
//		widgetFrame.add(button_exit).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);
//		
//		Button testButton1 = new Button(catSkin, "bell");
//		widgetFrame.add(testButton1).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);
//		
//		Button testButton2 = new Button(catSkin, "bell");
//		widgetFrame.add(testButton2).height(Value.percentHeight(0.25f,table)).top().space(20).spaceTop(10);

	}
}
