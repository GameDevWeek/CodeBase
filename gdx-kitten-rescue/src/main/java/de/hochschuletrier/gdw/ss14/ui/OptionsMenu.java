package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.states.GameStateEnum;
import de.hochschuletrier.gdw.ss14.states.KittenGameState;

public class OptionsMenu extends LaserCatMenu
{
	private OptionsMenuListener optionsMenuListener;
	private Float currentVolume;
	private String volumeString;
	private KittenGameState previousState;

	public void init(AssetManagerX assetManager, KittenGameState previousState)
	{
		super.init(assetManager);
		currentVolume = LocalMusic.getSystemVolume();
		this.previousState = previousState;
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Volume Down";
		name[1] = "Volume Up";
		name[2] = "Credits";
		name[3] = "Return";
		addButtonsToFrame();

		optionsMenuListener = new OptionsMenuListener();

		for (Button b : button)
		{
			b.addListener(LaserCatMenu.soundListener);
			b.addListener(this.optionsMenuListener);
		}

		
	}

	protected void addButtonsToFrame()
	{
		button = new Button[numberOfButtons];
		label = new Label[numberOfButtons];

		for (int i = 0; i < numberOfButtons; i++)
		{
			label[i] = new Label(name[i], basicSkin);
		}
		
		widgetFrame.add(label[0]).expandX().space(20).spaceBottom(10);
		
		Label spacer = new Label("", basicSkin);
		widgetFrame.add(spacer).expandX();
		
		widgetFrame.add(label[1]).expandX().space(20).spaceBottom(10);
		widgetFrame.add(label[2]).expandX().space(20).spaceBottom(10);
		widgetFrame.add(label[3]).expandX().space(20).spaceBottom(10);

		

		widgetFrame.row();
		button[0] = new Button(catSkin, "sound_reduce");
		button[0].setName("button");		
		
		Label volumeLabel = new Label(currentVolume.toString(), basicSkin);

		button[1] = new Button(catSkin, "sound_push");
		button[1].setName("button");
		button[2] = new Button(catSkin, "bell"); // Placeholder for image
		button[2].setName("bell");
		button[3] = new Button(catSkin, "bell"); // Placeholder for image
		button[3].setName("bell");

		widgetFrame.add(button[0]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

		widgetFrame.add(volumeLabel).center();
		
		widgetFrame.add(button[1]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);		
		widgetFrame.add(button[2]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);
		widgetFrame.add(button[3]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

		
		
		name = null;
	}

	private class OptionsMenuListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			currentVolume = LocalMusic.getSystemVolume();
			for (int i = 0; i < numberOfButtons; i++)
			{
				if (button[i] != event.getListenerActor())
					continue;

				switch (i)
				{
				case 0:
					LocalMusic.setSystemVolume((float) ((currentVolume - 0.1) > 0 ? currentVolume - 0.1	: 0));
					System.out.println("Decrease Volume");
					break;
				case 1:
					LocalMusic.setSystemVolume((float) ((currentVolume + 0.1) < 1.0 ? currentVolume + 0.1 : 1.0));
					System.out.println("Increase Volume");
					break;
				case 2:
					System.out.println("Open Credits");
					break;
				default:
					previousState.getEnum().activate();
					break;
				}
			}
		}
	}
}
