package de.hochschuletrier.gdw.ss14.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class MainMenu extends LaserCatMenu
{
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Start";
		name[1] = "Levels";
		name[2] = "Options";
		name[3] = "Exit";
		addButtonsToFrame();
	}
}