package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.gamestates.KittenGameState;

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
		this.currentInput = InputManager.readSettings();
		numberOfInputs = DeviceType.values().length;
		this.previousState = previousState;
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Volume Down";
		name[1] = "Volume Up";
		name[2] = "Input: " + currentInput.toString();
		name[3] = "Return";
		addButtonsToFrame();

		optionsMenuListener = new OptionsMenuListener();

		for (UIButton b : button)
		{
			b.addListener(soundListener);
			b.addListener(this.optionsMenuListener);
		}
		button[2].setOverAnimation(catSkin, "bell", frameDuration);
		button[3].setOverAnimation(catSkin, "bell", frameDuration);

		
	}

	protected void addButtonsToFrame()
	{
		button = new UIButton[numberOfButtons];
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


		

		widgetFrame.row();
		button[0] = new UIButton(catSkin, "sound_reduce");
		button[0].setName("button");		
		
		currentVolume = LocalMusic.getSystemVolume();
		volumeLabel = new Label (Integer.toString((int)currentVolume*10), catSkin);

		button[1] = new UIButton(catSkin, "sound_push");
		button[1].setName("button");
		button[2] = new UIButton(catSkin, "bell");
		button[2].setName("bell");
		button[3] = new UIButton(catSkin, "bell");
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
				System.out.println("Enter UIButton-SwitchCase");
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
					System.out.println("Switch Input");
					currentInput = DeviceType.values()[(currentInput.ordinal()+1)%numberOfInputs];
					
					label[2].setText("Input: " + currentInput.toString());
					break;
				default:
				    InputManager.getInstance().changeInputDevice(currentInput);
					previousState.getEnum().activate();
					break;
				}
			}
		}
	}
}
