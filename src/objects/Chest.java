package objects;

import main.Game;

public class Chest extends GameObject{

	public Chest(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}
	
	private void createHitbox() {
		initHitbox(48,30);
		xDrawOffset = 2;
		yDrawOffset = 6;
		//This is for placing the object right on the floor
		//Without this, the objects will float 
		//hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
		//This is for placing the object at the center of a block
		hitbox.x += xDrawOffset / 2;
	}

	public void update() {
		//Chests only update when they are opened
		if(doAnimation)
			updateAnimationTick();
	}

}
