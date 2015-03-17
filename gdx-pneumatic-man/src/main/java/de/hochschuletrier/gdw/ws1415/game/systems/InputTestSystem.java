package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;


public class InputTestSystem extends IteratingSystem {

	

	public InputTestSystem() {
		super(Family.all(InputComponent.class).get());
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if(entity.getComponent(InputComponent.class).jump){
			System.out.println("Jump");	
		}
		else if(entity.getComponent(InputComponent.class).pause){
			System.out.println("Pause");
		}
		else if(entity.getComponent(InputComponent.class).direction == 1){
			System.out.println("Rechts");
		}
		else if(entity.getComponent(InputComponent.class).direction == 0){
			System.out.println("Stehen");
		}
		else if(entity.getComponent(InputComponent.class).direction == -1){
			System.out.println("Links");
		}
	}
	
	
	

}
