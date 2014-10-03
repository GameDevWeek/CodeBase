package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;

public class UIAnimatedButton extends Button {
	private Skin skin;
	private boolean animated;
	private int actualFrame = 0;
	private int frames;
	private String stylename;
	
	public UIAnimatedButton (Skin skin) {
		super(skin);
		this.skin = skin;
	}
	
	public UIAnimatedButton (Skin skin, String styleName) {
		super(skin, styleName);
		this.skin = skin;
	}
	
	public UIAnimatedButton (Skin skin, String styleName, int frames) {
		super(skin, styleName);
		this.skin = skin;
		this.stylename = styleName;
		this.frames = frames;
	}

	public UIAnimatedButton (Actor child, Skin skin, String styleName) {
		super(child, skin, styleName);
		this.skin = skin;
	}

	public UIAnimatedButton (Actor child, ButtonStyle style) {
		super(child, style);
	}

	public UIAnimatedButton (ButtonStyle style) {
		super(style);
	}

	/** Creates a button without setting the style or size. At least a style must be set before using this button. */
	public UIAnimatedButton () {
		super();
	}
	
	private Skin getSkin() {
		return this.skin;
	}
	
	public void setStyle(String skin) {
		this.setStyle(this.getSkin().get(skin, ButtonStyle.class));
	}

	@Override
	public void act(float delta) {
		System.out.println(" IT IS " + delta);
	}
}
