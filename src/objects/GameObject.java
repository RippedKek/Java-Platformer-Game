package objects;

import static utils.Constants.ObjectConstants.*;
import static utils.Constants.ANI_SPEED;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	//For animating potions alltime
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;
	
	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}
	
	protected void initHitbox(float width, float height) {
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}
	
	public void drawHitbox(Graphics g, int xLvlOffset) {
		//For debugging purposes
		g.setColor(Color.RED);
		g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;			
				if(objType == BARREL || objType == BOX || objType == CHEST) {
					doAnimation = false;
					active = false;
				}else if(objType == CANNON_LEFT || objType == CANNON_RIGHT) 
					doAnimation = false;
			}
		}
	}
	
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;
		doAnimation = true;
		if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT || objType == CHEST)
			doAnimation = false;
		else
			doAnimation = true;
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public boolean isActive() {
		return active;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getAniTick() {
		return aniTick;
	}
	
}
