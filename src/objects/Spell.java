package objects;

import java.awt.geom.Rectangle2D;

import entities.Player;
import main.Game;

import static utils.Constants.Projectiles.*;

public class Spell {

	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;
	private int updateYTick = 0;
	private int updateYLimit = 0;
	
	private Rectangle2D.Float playerHitbox;
	
	public Spell(int x, int y, int dir, Rectangle2D.Float playerHitbox) {
		//For adjusting cannon ball with the necro
		int xOffset = (int)(-3 * Game.SCALE);
		int yOffset = (int)(5 * Game.SCALE);
		this.playerHitbox = playerHitbox;
		if(dir == 1)
			xOffset = (int)(29 * Game.SCALE);
		hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH / 2, CANNON_BALL_HEIGHT / 2);
		this.dir = dir;
	}
	
	public void updatePos() {
		//if dir is negative, then spellmoving left
		hitbox.x += (dir * (SPEED + 1.5));
		if(hitbox.y <= playerHitbox.y + 15) {
			updateYTick++;
			if(updateYTick >= updateYLimit) {
				hitbox.y += 1.5f;
				updateYTick = 0;
			}
		}
		else if(hitbox.y >= playerHitbox.y + 15) {
			updateYTick++;
			if(updateYTick >= updateYLimit) {
				hitbox.y -= 1.5f;
				updateYTick = 0;
			}
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
	
}
