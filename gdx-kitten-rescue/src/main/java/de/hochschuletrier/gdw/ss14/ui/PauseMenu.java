package de.hochschuletrier.gdw.ss14.ui;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class PauseMenu extends LaserCatMenu
{
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 3;
		name = new String[numberOfButtons];
		name[0] = "Resume";
		name[1] = "Options";
		name[2] = "Exit";
		//addButtonsToFrame();
	}
}
