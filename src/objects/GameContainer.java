package objects;

import static utils.Constants.ObjectConstants.*;

import main.Game;

public class GameContainer extends GameObject {

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}
	
	private void createHitbox() {
		if(objType == BOX) {
			initHitbox(50, 36);
			xDrawOffset = (int)(7 * Game.SCALE);
			yDrawOffset = (int)(12 * Game.SCALE);
		}
		else {
			initHitbox(46, 50);
			xDrawOffset = (int)(8 * Game.SCALE);
			yDrawOffset = (int)(5 * Game.SCALE);
		}
		//This is for placing the object right on the floor
		//Without this, the objects will float 
		hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
		//This is for placing the object at the center of a block
		hitbox.x += xDrawOffset / 2;
	}

	public void update() {
		//Box and Barrels only update when they are destroyed
		if(doAnimation)
			updateAnimationTick();
	}
	
}
