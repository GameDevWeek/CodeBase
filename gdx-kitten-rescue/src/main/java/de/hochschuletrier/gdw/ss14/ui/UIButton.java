package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UIButton extends Button {
	private Skin skin;
	
	public UIButton (Skin skin) {
		super(skin);
		this.skin = skin;
	}
	
	public UIButton (Skin skin, String styleName) {
		super(skin, styleName);
		this.skin = skin;
	}

	public UIButton (Actor child, Skin skin, String styleName) {
		super(child, skin, styleName);
		this.skin = skin;
	}

	public UIButton (Actor child, ButtonStyle style) {
		super(child, style);
	}

	public UIButton (ButtonStyle style) {
		super(style);
	}

	/** Creates a button without setting the style or size. At least a style must be set before using this button. */
	public UIButton () {
		super();
	}
	
	private Skin getSkin() {
		return this.skin;
	}
	
	public void setStyle(String skin) {
		this.setStyle(this.getSkin().get(skin, ButtonStyle.class));
	}
}
