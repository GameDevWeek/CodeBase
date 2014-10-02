package de.hochschuletrier.gdw.ss14.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;

public class MainMenu extends LaserCatMenu
{
	//private UiActionListener actionListener;
	
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
		
		button = new Button[numberOfButtons];
		for(int i=0; i<numberOfButtons; i++)
		{
			button[i] = new Button(catSkin, "bell");
			button[i].setName("bell");
		}
		
		addButtonsToFrame();
		for (Button b:button)
		{
			b.addListener(LaserCatMenu.soundListener);
//			b.addListener(this.actionListener);
		}
	}
	
//	private class UiActionListener extends ClickListener
//	{
//		public void clicked(InputEvent event, float x, float y)
//		{
//			public void clicked(InputEvent event, float x, float y)
//			{
//				for(int i=0; i<numberOfButtons; i++)
//				{
//					if(button[i] == event.getListenerActor())
//					{
//						switch (i)
//						{
//							case 0:
//								
//								break;
//							case 1:
//								break;
//							case 2:
//								break;
//							case 3:
//								break;
//							case 4:
//								break;
//							default:
//								System.out.println("You just fucked up");
//						}		
//						break;
//					}
//				}
//			}
//			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor)
//			{
//				if(event.getListenerActor().getName().equals("bell"))
//					SoundManager.performAction(UIActions.BELLCLICKED);
//				else
//					SoundManager.performAction(UIActions.BUTTONCLICKED);
//			}
//		}
//	}
//
}