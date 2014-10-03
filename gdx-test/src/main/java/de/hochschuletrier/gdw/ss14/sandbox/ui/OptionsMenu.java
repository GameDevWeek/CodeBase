package de.hochschuletrier.gdw.ss14.sandbox.ui;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;

public class OptionsMenu extends LaserCatMenu
{
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 5;
		name = new String[numberOfButtons];
		name[0] = "Volume+";
		name[1] = "Volume-";
		name[2] = "Mute";
		name[3] = "Credits";
		name[4] = "Difficulty";
		addButtonsToFrame();
	}
}
