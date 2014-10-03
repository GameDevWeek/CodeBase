package de.hochschuletrier.gdw.ss14.sandbox.ecs;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;

import java.util.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class EntityManager
{
    private int currentId = 0; // next unused ID
    private List<Integer> entityList;
    private HashMap<Class, HashMap<Integer, ? extends Component>> componentStorage;

    public EntityManager()
    {
        entityList = new LinkedList<Integer>();
        componentStorage = new HashMap<Class, HashMap<Integer, ? extends Component>>();
    }

    public int generateNewEntityID()
    {
        // TODO: maybe use java UUIDs?
        if (currentId < Integer.MAX_VALUE)
        {
            return currentId++;
        }
        else // all IDs taken
        {
            // check if there's a free id
            for (int i = 1; i < Integer.MAX_VALUE; i++)
            {
                if (!entityList.contains(i))
                    return i;
            }

            // else throw error!
            throw new Error("All entity IDs taken!");
        }
    }

    public int createEntity()
    {
        int newID = generateNewEntityID();

        if (newID < 0)
        {
            // should never be the case! else something somewhere goes wrong :s
            throw new Error("Generating Entity-ID caused bufferoverflow!");
        }
        else
        {
            entityList.add(newID);
            return newID;
        }
    }

    public void deleteEntity(Integer entity) // argument passed as an object to remove key (else removes index!!)
    {
        entityList.remove(entity);

        for (HashMap<Integer, ? extends Component> store : componentStorage.values())
        {
            store.remove(entity);
        }
    }

    public void deleteAllEntities()
    {
        entityList.clear();

        for (HashMap<Integer, ? extends Component> store : componentStorage.values())
        {
            store.clear();
        }
    }

    public <T extends Component> void addComponent(int entity, T component)
    {
        Class classToAdd;

        if (component instanceof PhysicsComponent)
        {
            classToAdd = component.getClass().getSuperclass();
        }
        else
        {
            classToAdd = component.getClass();
        }

        HashMap<Integer, ? extends Component> storage = componentStorage.get(classToAdd);

        // there's no key set yet for the given component, create a new key
        if (storage == null)
        {
            storage = new HashMap<Integer, T>();
            componentStorage.put(classToAdd, storage);
        }

        ((HashMap<Integer, T>) storage).put(entity, component);
    }


    public <T extends Component> T getComponent(int entity, Class<T> componentType)
    {
        T result = null;
        HashMap<Integer, ? extends Component> storage = componentStorage.get(componentType);

        if (storage != null)
        {
            result = (T) storage.get(entity);

            if (result == null)
            {
                Gdx.app.error("ECS", entity + " has no component of class " + componentType);
            }
        }
        else
        {
            Gdx.app.error("ECS", "There are no entities with the given component! (" + componentType + ")");
        }

        return result;
    }

    public <T extends Component> List<T> getAllComponentsOfType(Class<T> componentType)
    {

        HashMap<Integer, ? extends Component> store = componentStorage.get(componentType);

        if (store == null)
        {
            return new LinkedList<T>();
        }
        else
        {
            List<T> result = new ArrayList<T>((java.util.Collection<T>) store.values());
            return result;
        }
    }

    private <T extends Component> Set<Integer> getAllEntitiesWithComponent(Class<T> componentType)
    {
        HashMap<Integer, ? extends Component> storage = componentStorage.get(componentType);

        if (storage == null)
        {
            return new HashSet<Integer>();
        }

        return storage.keySet();
    }

    public Array<Integer> getAllEntitiesWithComponents(Class... components)
    {
        Array entityArray = new Array();

        // get all entities with the first component
        Set keySet = getAllEntitiesWithComponent(components[0]);

        // then iterate over these entities to check if they got all the other components
        Iterator it = keySet.iterator();

        while (it.hasNext())
        {
            boolean hasAllComponents = true;
            Integer entity = (Integer) it.next();

            for (int i = 1; i < components.length; ++i)
            {
                if (getComponent(entity, components[i]) == null)
                {
                    hasAllComponents = false;
                    break;
                }
            }

            if (hasAllComponents)
            {
                entityArray.add(entity);
            }
        }

        return entityArray;

    }
}
