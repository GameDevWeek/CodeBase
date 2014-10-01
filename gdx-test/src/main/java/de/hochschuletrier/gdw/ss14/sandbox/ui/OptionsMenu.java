package de.hochschuletrier.gdw.ss14.sandbox.ui;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class OptionsMenu extends LaserCatMenu
{
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Volume+";
		name[1] = "Volume-";
		name[2] = "Mute";
		name[3] = "Credits";
		addButtonsToFrame();
	}
}
