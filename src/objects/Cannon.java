package objects;

import main.Game;

public class Cannon extends GameObject{
	
	private int tileY; //for checking if player and cannon is on same tile height

	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / Game.TILES_SIZE;
		initHitbox(40,26);
		//For centering the cannon on a tile
		hitbox.x -= (int)(4 * Game.SCALE);
		hitbox.y += (int)(6 * Game.SCALE);
	}
	
	public void update() {
		if(doAnimation)
			updateAnimationTick();
	}
	
	public int getTileY() {
		return tileY;
	}

}
