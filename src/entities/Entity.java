package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.Directions.UP;
import static utils.HelpMethods.CanMoveHere;
import static utils.Constants.Directions.DOWN;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
	
	protected float x,y;
	protected int width,height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed = 1.0f * Game.SCALE;
	
	public Entity(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		//For debugging purposes
		g.setColor(Color.RED);
		g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
	}
	
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y + 10, (int)attackBox.width, (int)attackBox.height);
	}
	
	protected void initHitbox(float width, float height) {
		hitbox = new Rectangle2D.Float(x,y,width,height);
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getEnemyState() {
		return state;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	
}