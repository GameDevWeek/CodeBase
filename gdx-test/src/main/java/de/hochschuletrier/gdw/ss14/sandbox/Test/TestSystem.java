package de.hochschuletrier.gdw.ss14.sandbox.Test;



import java.util.List;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class TestSystem extends ECSystem{

	public TestSystem(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		List arr =  entityManager.getAllComponentsOfType(TestComponent.class);
		
		for(int i = 0; i < arr.size(); i++){
			TestComponent t = (TestComponent)arr.get(i);
			System.out.println(t.s);
		}
	}
	
	

}
