package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;

public class InputKeyboardSystem extends IteratingSystem implements InputProcessor{

    boolean jump = false;
    
    boolean pause = false;
    
    boolean left = false;
    
    boolean right = false;
    
    public InputKeyboardSystem()
    {
        super(Family.all(InputComponent.class).get());
    }

    
    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        InputComponent inputComponent = entity.getComponent(InputComponent.class);
        inputComponent.reset();
        if(jump)
        {
            inputComponent.jump = true;
        }
        if(right)
        {
            inputComponent.direction++;
        }
        if(left)
        {
            inputComponent.direction--;
        }
        if(pause)
        {
            inputComponent.pause = true;
        }
        
    }
    
    
    @Override
    public boolean keyDown(int keycode) {
        switch(keycode)
        {
            case Input.Keys.UP:
            case Input.Keys.W: jump = true; break;
            case Input.Keys.LEFT:
            case Input.Keys.A: left = true; break;
            case Input.Keys.RIGHT:
            case Input.Keys.D: right = true; break;
            case Input.Keys.ESCAPE:
            case Input.Keys.P: pause = true; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode)
        {
            case Input.Keys.UP:
            case Input.Keys.W: jump = false; break;
            case Input.Keys.LEFT:
            case Input.Keys.A: left = false; break;
            case Input.Keys.RIGHT:
            case Input.Keys.D: right = false; break;
            case Input.Keys.ESCAPE:
            case Input.Keys.P: pause = false; break;
        }
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
