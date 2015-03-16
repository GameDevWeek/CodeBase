package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GoalComponent extends Component implements Pool.Poolable{
	
	//Die Anzahl an Kollegen die man noch retten muss um das Levelziel zu erreichen. Für jeden geretteten Kollegen
	//wird die Variable um 1 reduziert. Wenn keine Kollegen mehr gerettet werden müssen (die Variable also 0 erreicht)
	//hat man das Level geschafft.
	public int unsaved_colleagues;

	@Override
	public void reset() {
		unsaved_colleagues = 0;
	}
	
}
