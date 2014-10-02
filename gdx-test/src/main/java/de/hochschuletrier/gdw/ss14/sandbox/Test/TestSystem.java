package de.hochschuletrier.gdw.ss14.sandbox.Test;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

import java.util.List;

public class TestSystem extends ECSystem{

	public TestSystem(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void update(float delta) {
		List arr =  entityManager.getAllComponentsOfType(TestComponent.class);

        for(Object anArr : arr){
            TestComponent t = (TestComponent) anArr;
            System.out.println(t.s);
        }
	}

	@Override
	public void render() {}
}
