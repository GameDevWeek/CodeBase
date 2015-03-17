package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageComponent extends Component implements Pool.Poolable {
	
	//Gibt an wieviel Schaden dem Spieler bzw einer Tile zugefügt wird. 1 steht für 1 Schadenspunkt, 0 steht für
	//keinen Schadenspunkt. Der boolean damageToPlayer gibt an ob der Schaden sich gegen den Spieler richtet (true)
	//oder gegen die Tile (false). Der boolean damageToTile funktioniert umgekehrt. Außerdem zieht -1 dem Spieler
	//das ganze Leben ab.
	
	public int damage;
	public boolean damageToPlayer;
	public boolean damageToTile;

	@Override
	public void reset() {
		damage = 0;
		damageToPlayer = false;
		damageToTile= false;
	}
}
