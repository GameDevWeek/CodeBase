package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 * @author Santo Pfingsten
 */
public class Player extends Creature{
    private int groundContacts;

    public Player(float x, float y){
        super(x, y, 50, 200);
    }

    public void initPhysics(PhysixManager manager){
        super.initPhysics(manager);

    }

    public void update(float delta){
        float xVelo = 0, yVelo = 0, aVelo = 0, rotaAngle = 100f;



        float angle = super.getBody().getAngle()%((float) Math.PI*2)+((float) Math.PI*2), eighth = 0.125f;
        if(angle > ((float) Math.PI*2)){
            angle -= ((float) Math.PI*2);
        }

        float accuracy = 128;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            xVelo -= Physics.g_playerSpeed.get();
            aVelo -= rotaAngle*delta;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            xVelo += Physics.g_playerSpeed.get();
            aVelo += rotaAngle*delta;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
            yVelo -= Physics.g_playerSpeed.get();
            if(angle >= (float)Math.PI*(1/accuracy) && angle < (float)Math.PI){
                aVelo -= rotaAngle*delta;
            }else if((angle >= (float)Math.PI && angle <= (float)Math.PI*((accuracy*2)-1))){
                aVelo += rotaAngle*delta;
            }else {
                System.out.println("U: "+angle);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            yVelo += Physics.g_playerSpeed.get();
            if(angle >= 0 && angle < (float)Math.PI*(1/accuracy)*(accuracy-1)){
                aVelo += rotaAngle*delta;
            }else if((angle >= (float)Math.PI*(1/accuracy)*(accuracy+1) && angle <= (float)Math.PI*accuracy*2)){
                aVelo -= rotaAngle*delta;
            }else {
                System.out.println("D: "+angle);
            }
        }

        super.getBody().setAngularVelocity(aVelo);

        //physicsBody.setLinearVelocityX(xVelo);
        //physicsBody.setLinearVelocityY(yVelo);
    }
}
