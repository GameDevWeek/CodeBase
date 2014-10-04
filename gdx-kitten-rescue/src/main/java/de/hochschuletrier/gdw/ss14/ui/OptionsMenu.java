package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.game.GameSettings;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.states.KittenGameState;

public class OptionsMenu extends LaserCatMenu
{
	private OptionsMenuListener optionsMenuListener;
	private float currentVolume;
	private String volumeString;
	private Label volumeLabel;
	private DeviceType currentInput;
	private int numberOfInputs;
	private KittenGameState previousState;

	public void init(AssetManagerX assetManager, KittenGameState previousState)
	{
		super.init(assetManager);
		this.currentInput = GameSettings.getInstance().getInputDeviceType();
		numberOfInputs = DeviceType.values().length;
		this.previousState = previousState;
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Volume Down";
		name[1] = "Volume Up";
		name[2] = "Credits";
		name[3] = "Input: " + currentInput.toString();
		name[4] = "Return";
		addButtonsToFrame();

		optionsMenuListener = new OptionsMenuListener();

		for (UIAnimatedButton b : button)
		{
			b.addListener(LaserCatMenu.soundListener);
			b.addListener(this.optionsMenuListener);
		}

		
	}

	protected void addButtonsToFrame()
	{
		button = new UIAnimatedButton[numberOfButtons];
		label = new Label[numberOfButtons];

		for (int i = 0; i < numberOfButtons; i++)
		{
			label[i] = new Label(name[i], catSkin);
		}
		
		widgetFrame.add(label[0]).expandX().space(20).spaceBottom(10);
		
		Label spacer = new Label("", catSkin);
		widgetFrame.add(spacer).expandX();
		
		widgetFrame.add(label[1]).expandX().space(20).spaceBottom(10);
		widgetFrame.add(label[2]).expandX().space(20).spaceBottom(10);
		widgetFrame.add(label[3]).expandX().space(20).spaceBottom(10);
		widgetFrame.add(label[4]).expandX().space(20).spaceBottom(10);


		

		widgetFrame.row();
		button[0] = new UIAnimatedButton(catSkin, "sound_reduce");
		button[0].setName("button");		
		
		currentVolume = LocalMusic.getSystemVolume();
		volumeLabel = new Label (Integer.toString((int)currentVolume*10), catSkin);

		button[1] = new UIAnimatedButton(catSkin, "sound_push");
		button[1].setName("button");
		button[2] = new UIAnimatedButton(catSkin, "bell");
		button[2].setName("bell");
		button[3] = new UIAnimatedButton(catSkin, "bell");
		button[3].setName("bell");
		button[4] = new UIAnimatedButton(catSkin, "bell");
		button[4].setName("bell");

		widgetFrame.add(button[0]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

		widgetFrame.add(volumeLabel).center();
		
		widgetFrame.add(button[1]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);		
		widgetFrame.add(button[2]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);
		widgetFrame.add(button[3]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);
		widgetFrame.add(button[4]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

		
		
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
				System.out.println("Enter Button-SwitchCase");
				switch (i)
				{
				case 0:
					LocalMusic.setSystemVolume((float) ((currentVolume - 0.1) > 0 ? currentVolume - 0.1	: 0));
					System.out.println("Decrease Volume");
					volumeLabel.setText(Integer.toString((int)(LocalMusic.getSystemVolume()*10)));
					break;
				case 1:
					LocalMusic.setSystemVolume((float) ((currentVolume + 0.1) < 1.0 ? currentVolume + 0.1 : 1.0));
					System.out.println("Increase Volume");
					volumeLabel.setText(Integer.toString((int)(LocalMusic.getSystemVolume()*10)));
					break;
				case 2:
					System.out.println("Open Credits");
					break;
				case 3: 
					System.out.println("Switch Input");
					currentInput = DeviceType.values()[(currentInput.ordinal()+1)%numberOfInputs];
					
				default:
					previousState.getEnum().activate();
					break;
				}
			}
		}
	}
}
