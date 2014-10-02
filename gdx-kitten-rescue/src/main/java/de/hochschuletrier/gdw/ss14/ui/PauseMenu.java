package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.states.GameStates;

public class PauseMenu extends LaserCatMenu
{
	private PauseMenuListener pauseMenuListener;
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 3;
		name = new String[numberOfButtons];
		name[0] = "Resume";
		name[1] = "Options";
		name[2] = "Exit";
		addButtonsToFrame();
		
		pauseMenuListener = new PauseMenuListener();
		
		for (Button b:button)
		{
			b.addListener(LaserCatMenu.soundListener);
			b.addListener(this.pauseMenuListener);
		}
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
			button[i].setName("bell");
			widgetFrame.add(button[i]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		}
		name = null;
	}
	
	private class PauseMenuListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			for(int i=0; i<numberOfButtons; i++)
			{
				if(button[i] != event.getListenerActor())
					continue;
				
					switch (i)
					{
						case 0:
							GameStates.GAMEPLAY.activate();
							break;
						case 1:
							GameStates.OPTIONSMENU.activate();
							break;
						case 2:
							GameStates.MAINMENU.activate();
							break;
						default:
							System.out.println("You just fucked up");		
							break;
				}
			}
		}
	}
}
