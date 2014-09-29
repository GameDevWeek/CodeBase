package de.hochschuletrier.gdw.ss14.sandbox.ecs;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.*;

import java.util.*;

public class Engine
{
    private static SystemComparator comparator = new SystemComparator();
    private Array<ECSystem> systems;

    public Engine()
    {
        systems = new Array<ECSystem>();
    }

    public void addSystem(ECSystem system)
    {
        systems.add(system);
        systems.sort(comparator);
    }

    public void removeSystem(ECSystem system)
    {
        systems.removeValue(system, true);
    }

    public void update(float delta)
    {
        for (ECSystem system : systems)
        {
            system.update(delta);
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
