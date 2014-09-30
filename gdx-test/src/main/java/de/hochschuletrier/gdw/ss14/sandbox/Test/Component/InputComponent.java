package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import java.awt.Component;

import com.badlogic.gdx.math.Vector2;

public class InputComponent extends Component{
	public Vector2 whereToGo;
    
	public InputComponent(Vector2 whereToGo){
	   this.whereToGo = whereToGo;
	    
	}

}
