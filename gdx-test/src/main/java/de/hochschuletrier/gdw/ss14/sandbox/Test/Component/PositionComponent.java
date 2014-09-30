package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;


import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class PositionComponent implements Component{
	
	public Vector2 position;
	
	public PositionComponent(Vector2 spawnPosition){
		position = spawnPosition;
	}
}
