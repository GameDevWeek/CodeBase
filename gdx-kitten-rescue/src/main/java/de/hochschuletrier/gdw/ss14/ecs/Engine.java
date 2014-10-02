package de.hochschuletrier.gdw.ss14.ecs;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.*;

import java.util.*;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */
public class Engine
{
    private static SystemComparator comparator = new SystemComparator();
    private Array<ECSystem> systems;

    public Engine()
    {
        systems = new Array<ECSystem>();
    }

    public ECSystem getSystemOfType(Class systemtype){
        for(ECSystem system : systems){
            if(systemtype.isInstance(system)){
                return system;
            }
        }
        return null;
    }
    
    public void addSystem(ECSystem system)
    {
        systems.add(system);
        systems.sort(comparator);
    }

    public void removeSystem(ECSystem system)
    {
        system.shutdown();
        
        systems.removeValue(system, true);
    }

    public void update(float delta)
    {
        for (ECSystem system : systems)
        {
            system.update(delta);
        }
    }

    public void render()
    {
        for (ECSystem system : systems)
        {
            system.render();
        }
    }

    private static class SystemComparator implements Comparator<ECSystem>
    {

        @Override
        public int compare(ECSystem a, ECSystem b)
        {
            int result;

            if (a.getPriority() > b.getPriority())
            {
                result = 1;
            }
            else if (a.getPriority() == b.getPriority())
            {
                result = 0;
            }
            else
            {
                result = -1;
            }

            return result;
        }
    }
}
