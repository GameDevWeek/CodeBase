package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

/**
 * Created by oliver on 16.03.15.
 *
 * Contains the facing direction. For selecting the right Image and for AI
 */
public class DirectionComponent extends Component{

    public Direction facingDirection;

}
