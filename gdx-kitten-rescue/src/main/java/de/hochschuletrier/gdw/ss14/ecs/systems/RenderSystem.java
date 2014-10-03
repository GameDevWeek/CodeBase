package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import java.util.PriorityQueue;

/**
 * Draws all RenderComponents.
 *
 * @author David Neubauer
 */
public class RenderSystem extends ECSystem {

    //private ShaderProgram redTintedShader;
    private RenderComponent renderComponents[];
    private PhysicsComponent physicsComponents[];
    
    public RenderSystem(EntityManager entityManager, int priority) {

        super(entityManager, priority);
        initializeShaders();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Array<Integer> entites = entityManager.getAllEntitiesWithComponents(RenderComponent.class, PhysicsComponent.class);

        RenderComponent renderCompo;
        PhysicsComponent physicsCompo;

        // Count components
        int arrayLength = entites.size;

        // init arrays
        if (renderComponents == null
                || physicsComponents == null
                || renderComponents.length != arrayLength
                || physicsComponents.length != arrayLength) {
            renderComponents = new RenderComponent[arrayLength];
            physicsComponents = new PhysicsComponent[arrayLength];
        }

        // save components to arrays
        int arrayPosition = 0;
        for (Integer integer : entites) {
            renderComponents[arrayPosition] = entityManager.getComponent(integer, RenderComponent.class);
            physicsComponents[arrayPosition] = entityManager.getComponent(integer, PhysicsComponent.class);
            arrayPosition++;
        }

        //sort arrays
        sortiere(renderComponents, physicsComponents);
        
        Color currentTintColor = null;
        
        int colorChangeCount = 0;
        
        // draw
        arrayPosition = 0;
        for (Integer integer : entites) {
            renderCompo = renderComponents[arrayPosition];
            physicsCompo = physicsComponents[arrayPosition];

            if (renderCompo.texture != null) {

                if (!isSameColor(currentTintColor, renderCompo.tintColor)) {
                    
                    //Gdx.gl20.glColorMask(true, false, false, true);
                    //DrawUtil.batch.setShader(redTintedShader);
                    currentTintColor = renderCompo.tintColor;
                    
                    if (currentTintColor != null)
                        DrawUtil.batch.setColor(currentTintColor);
                    else
                        DrawUtil.batch.setColor(new Color(1,1,1,1));
                        
                    colorChangeCount ++;
                }

                DrawUtil.batch.draw(renderCompo.texture,
                        physicsCompo.getPosition().x - (renderCompo.texture.getRegionWidth() / 2),
                        physicsCompo.getPosition().y - (renderCompo.texture.getRegionHeight() / 2),
                        renderCompo.texture.getRegionWidth() / 2,
                        renderCompo.texture.getRegionHeight() / 2,
                        renderCompo.texture.getRegionWidth(),
                        renderCompo.texture.getRegionHeight(),
                        1f,
                        1f,
                        (float) (physicsCompo.getRotation() * 180 / Math.PI));
            }
            arrayPosition++;
        } // end for
        
        //System.out.println("\n\n" + "ColorChangeCount = "+colorChangeCount + "\n\n");
        
        DrawUtil.setColor(new Color(1,1,1,1));
    }

    private void initializeShaders() {
        /*FileHandle vertShader = Gdx.files.internal("data/shaders/passThrough.vs");
         FileHandle fragShader = Gdx.files.internal("data/shaders/redTinted.fs");
         redTintedShader = new ShaderProgram(vertShader, fragShader);
        
         System.out.println(redTintedShader.getLog());*/
    }

    public static void sortiere(RenderComponent x[], PhysicsComponent p[]) {
        qSort(x, 0, x.length - 1, p);
    }

    public static void qSort(RenderComponent x[], int links, int rechts, PhysicsComponent p[]) {
        if (links < rechts) {
            int i = partition(x, links, rechts, p);
            qSort(x, links, i - 1, p);
            qSort(x, i + 1, rechts, p);
        }
    }

    public static int partition(RenderComponent r[], int links, int rechts, PhysicsComponent p[]) {
        
        RenderComponent pivot; 
        int i, j;
        
        RenderComponent renderCompHelp;
        PhysicsComponent physicsCompHelp;
        
        pivot = r[rechts];
        i = links;
        j = rechts - 1;
        
        while (i <= j) {
            
            // Sort after z-index or, if same, after tint color
            if ((r[i].zIndex > pivot.zIndex) ||
                    ((r[i].zIndex == pivot.zIndex) && (isGreaterColor(r[i].tintColor, pivot.tintColor)))) {
                
                // tausche x[i] und x[j]
                renderCompHelp = r[i];
                r[i] = r[j];
                r[j] = renderCompHelp;
                physicsCompHelp = p[i];
                p[i] = p[j];
                p[j] = physicsCompHelp;
                j--;
            } else {
                i++;
            }
        }
        
        // tausche x[i] und x[rechts]
        renderCompHelp = r[i];
        r[i] = r[rechts];
        r[rechts] = renderCompHelp;
        physicsCompHelp = p[i];
        p[i] = p[rechts];
        p[rechts] = physicsCompHelp;

        return i;
    }
    
    public static boolean isSameColor( Color a, Color b ) {
        
        if ((a == null) && (b == null))
            return true;
        else if ((a == null) || (b == null))
            return false;
        
        boolean isSameColor = true;
        float delta = 0.0001f;
        
        isSameColor = isSameColor && (Math.abs(a.r - b.r) < delta);
        isSameColor = isSameColor && (Math.abs(a.g - b.g) < delta);
        isSameColor = isSameColor && (Math.abs(a.b - b.b) < delta);
        isSameColor = isSameColor && (Math.abs(a.a - b.a) < delta);
        
        return isSameColor;
    }
    
    public static boolean isGreaterColor( Color a, Color b ) {
        
        // null = null
        if ((a == null) && (b == null))
            return false;
        
        // null < any color
        else if ((a == null) && (b != null))
            return false;
        
        // any color > null
        else if ((a != null) && (b == null))
            return true;
        
        boolean isGreater = false;
        
        isGreater = isGreater || (a.r > b.r);
        isGreater = isGreater || (a.g > b.g);
        isGreater = isGreater || (a.b > b.b);
        isGreater = isGreater || (a.a > b.a);
        
        return isGreater;
    }
}
