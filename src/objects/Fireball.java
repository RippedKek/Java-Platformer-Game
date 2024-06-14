package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

import main.Game;

import static utils.Constants.Projectiles.*;

public class Fireball {

	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;
	
	private int fireballIndex = 0;
	private int fireballTick = 0;
	
	public Fireball(int x, int y, int dir) {
		//For adjusting cannon ball with the cannon
		int xOffset = (int)(-3 * Game.SCALE);
		int yOffset = (int)(5 * Game.SCALE);
		if(dir == 1)
			xOffset = (int)(29 * Game.SCALE);
		hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, 64, 64);
		this.dir = dir;
	}
	
	public void updatePos() {
		//if dir is negative, then cannon ball moving left
		hitbox.x += (dir * FIREBALL_SPEED);
	}
	
	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int)(hitbox.x - xLvlOffset), (int)(hitbox.y), 64, 64);
	}
	
	public void updateAnimation() {
		fireballTick++;
		if(fireballTick >= 25) {
			fireballIndex++;
			fireballTick = 0;
			if(fireballIndex >= 4) 
				fireballIndex = 0;
		}
	}
	
	public void setPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
	}
	
	public Rectangle2D.Float getHitbox(){
		return hitbox;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public int getDir() {
		return dir;
	}
	
	public int getFireballIndex() {
		return fireballIndex;
	}
	
}
