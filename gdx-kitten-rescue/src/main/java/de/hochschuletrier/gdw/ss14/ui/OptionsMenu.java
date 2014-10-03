package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.states.GameStateEnum;
import de.hochschuletrier.gdw.ss14.states.KittenGameState;

public class OptionsMenu extends LaserCatMenu
{
	private OptionsMenuListener optionsMenuListener;
	private float currentVolume;
	private KittenGameState previousState;

	public void init(AssetManagerX assetManager, KittenGameState previousState)
	{
		super.init(assetManager);
		this.previousState = previousState;
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Volume Up";
		name[1] = "Volume Down";
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
		button = new UIButton[numberOfButtons];
		label = new Label[numberOfButtons];

		for (int i = 0; i < numberOfButtons; i++)
		{
			label[i] = new Label(name[i], basicSkin);
			widgetFrame.add(label[i]).expandX().space(20).spaceBottom(10);
		}

		widgetFrame.row();
		button[0] = new UIButton(catSkin, "sound_push");
		button[0].setName("button");
		button[1] = new UIButton(catSkin, "sound_reduce");
		button[1].setName("button");
		button[2] = new UIButton(catSkin, "bell"); // Placeholder for image
		button[2].setName("bell");
		button[3] = new UIButton(catSkin, "bell"); // Placeholder for image
		button[3].setName("bell");
		
		//UIButton j = new UIButton(catSkin, "sound_push");
		//j.setStyle(j.getSkin().get("bell", ButtonStyle.class));

		for (Button b : button)
			widgetFrame.add(b).size(
					Value.percentWidth(widthOfWidgetFrame / 6, table)).top()
					.space(20).spaceTop(10);

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
					//button[i].setStyle(button[i].getSkin().get("bell", ButtonStyle.class));
					LocalMusic.setSystemVolume((float) ((currentVolume + 0.1) < 1.0 ? currentVolume + 0.1 : 1.0));
					System.out.println("Increase Volume");
					break;
				case 1:
					LocalMusic.setSystemVolume((float) ((currentVolume - 0.1) > 0 ? currentVolume - 0.1	: 0));
					System.out.println("Decrease Volume");

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
