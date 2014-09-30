package de.hochschuletrier.gdw.ss14.sandbox.Test;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class TestComponent implements Component{
	
	public String s;
	
	public TestComponent(String s){
		this.s = s;
	}

}
