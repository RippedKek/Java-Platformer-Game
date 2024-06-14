package objects;

import main.Game;

public class Spike extends GameObject {

	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(64, 16);
		xDrawOffset = 0; //Because spike size spreads throughout the whole width of the png
		yDrawOffset = (int)(Game.SCALE * 16);
		hitbox.y += yDrawOffset; //For fitting on the floor
	}

}
