package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageComponent extends Component implements Pool.Poolable {
	
	//Gibt an wieviel Schaden dem Spieler zugefügt werden. 1 steht für 1 Schadenspunkt, 0 steht für keinen
	//Schadenspunkt und -1 zieht dem Spieler das ganze Leben ab.
	public int damage;

	@Override
	public void reset() {
		damage = -1;
	}
}
