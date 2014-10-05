package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;

public class MainMenu extends LaserCatMenu
{
	private MainMenuListener actionListener;
	
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Levels";
		name[1] = "Options";
		name[2] = "Credits";
		name[3] = "Exit Game";
		
		addButtonsToFrame();
		actionListener = new MainMenuListener();
		
		for (UIButton b:button)
		{
			b.addListener(soundListener);
			b.addListener(this.actionListener);
			b.setOverAnimation(catSkin, "bell", frameDuration);
		}
	}
	
	protected void addButtonsToFrame()
	{
		button = new UIButton[numberOfButtons];
		label = new Label[numberOfButtons];

		for(int i=0; i<numberOfButtons; i++)
		{
			label[i] = new Label(name[i], catSkin);
			widgetFrame.add(label[i]).expandX().space(20).spaceBottom(10);
		}
		
		widgetFrame.row();
		for(int i = 0; i<numberOfButtons; i++)
		{
			button[i] = new UIButton(catSkin, "bell");
			button[i].setName("bell");
			widgetFrame.add(button[i]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		}
		name = null;
	}
	
	private class MainMenuListener extends ClickListener
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
							GameStateEnum.LEVELMENU.activate();
							break;
						case 1:
							GameStateEnum.OPTIONSMENU.activate();
							break;
						case 2:
							GameStateEnum.CREDITS.activate();
							break;
						case 3:
							System.exit(0);
							break;
						default:
							System.out.println("You just fucked up");		
							break;
				}
			}
		}
	}

}