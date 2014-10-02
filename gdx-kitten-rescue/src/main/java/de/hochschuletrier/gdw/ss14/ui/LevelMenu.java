package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.states.GameStates;

public class LevelMenu extends LaserCatMenu
{
	private LevelMenuListener levelMenuListener;
	private int levelIndex;
	private Label levelLabel;
	private String levelString;
	
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Start";
		name[1] = "Level+";
		name[2] = "Level-";
		name[3] = "Return";
		addButtonsToFrame();

		levelMenuListener = new LevelMenuListener();

		for (Button b : button)
		{
			b.addListener(LaserCatMenu.soundListener);
			b.addListener(this.levelMenuListener);
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
		levelIndex = 0;
		levelLabel = new Label(("Level: " + levelIndex), basicSkin);
		widgetFrame.add(levelLabel);
		name = null;
	}

	private class LevelMenuListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			for (int i = 0; i < numberOfButtons; i++)
			{
				if (button[i] != event.getListenerActor())
					continue;

				switch (i)
				{
				case 0:
					GameStates.GAMEPLAY.activate();
					break;
				case 1:
					levelIndex = levelIndex > 0 ? (levelIndex-1) : 0;
					levelLabel.setText("Level: " + levelIndex);
					System.out.println("Decrease Level");
					break;
				case 2:
					levelIndex = levelIndex > 0 ? (levelIndex+1) : 0;
					levelLabel.setText("Level: " + levelIndex);
					System.out.println("Increase Level");
					break;
				case 3:
					GameStates.MAINMENU.activate();
					break;
				}
			}
		}
	}
}
