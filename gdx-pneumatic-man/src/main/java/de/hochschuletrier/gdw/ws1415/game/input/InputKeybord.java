package de.hochschuletrier.gdw.ws1415.game.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;

public class InputKeybord extends IteratingSystem implements InputProcessor{


    
    
    public InputKeybord()
    {
        super(Family.all(InputComponent.class).get());
        // TODO Auto-generated constructor stub
    }

    
    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean keyDown(int keycode) {
          if(keycode == Input.Keys.LEFT)
            {
                
            }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }


}
