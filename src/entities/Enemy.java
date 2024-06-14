package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.DEAD;
import static utils.Constants.EnemyConstants.GetEnemyDmg;
import static utils.Constants.EnemyConstants.HIT;
import static utils.Constants.EnemyConstants.SHARK;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import audio.AudioPlayer;
import gameStates.Playing;

import static utils.Constants.Directions.*;
import static utils.Constants.GRAVITY;
import static utils.Constants.ANI_SPEED;
import static utils.Constants.Achievements.*;

import main.Game;
import objects.ObjectManager;
import objects.Spell;

public abstract class Enemy extends Entity {
	
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	//Melee enemies can attack if player is in 1 tile around them
	protected float attackDistance = Game.TILES_SIZE - 20;
	protected float attackDistanceAlpha = Game.TILES_SIZE;
	//Enemy health
	protected boolean active = true;
	protected boolean attackChecked;
	
	protected int attackBoxOffsetX;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		//Getting enemy health based on enemy type
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = 0.25f * Game.SCALE;
	}
	
	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
		this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
	}
	
	//Checks for enemy position the first time they spawn
	protected void firstUpdateCheck(int[][] lvlData) {
		if(!IsEntityOnFloor(hitbox,lvlData)) 
			inAir = true;
		firstUpdate = false;
	}
	
	protected void inAirChecks(int[][] lvlData, Playing playing) {
		if (state != HIT && state != DEAD) {
			updateInAir(lvlData);
			playing.getObjectManager().checkSpikesTouched(this);
		}
	}
	
	//If in air then calculates necessary adjustments
	protected void updateInAir(int[][] lvlData) {
		if(CanMoveHere(hitbox.x,hitbox.y + airSpeed,hitbox.width,hitbox.height,lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		}else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
			//Tile number in Y direction for enemy will never change
			tileY = (int)(hitbox.y / Game.TILES_SIZE);
		}
	}
	
	protected void updateAttackBoxFlip(int attackBoxOffsetX) {
		if (walkDir == RIGHT)
			attackBox.x = hitbox.x + hitbox.width;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX;

		attackBox.y = hitbox.y;
	}
	
	//Inits movement for enemies based on level data
	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		if(walkDir == LEFT)
			xSpeed -= walkSpeed;
		else
			xSpeed += walkSpeed;
		if(CanMoveHere(hitbox.x + xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData))
			if(IsFloor(hitbox,xSpeed,lvlData)) {
				hitbox.x += xSpeed;
				return;
			}			
		changeWalkDir();	
	}
	
	protected void turnTowardsPlayer(Player player) {
		if(player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else walkDir = LEFT;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		//If enemy and player are on the same height i.e. tiles height
		int playerTileY = (int)player.getHitbox().y / Game.TILES_SIZE; //Dividing by TILE_SIZE to get the tile number
		if(playerTileY == tileY)
			if(isPlayerInRange(player)) 
				if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
					return true; 		
		return false;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player, int enemyType) {
		//For necromancer who does not need sight clear
		int playerTileY = (int)player.getHitbox().y / Game.TILES_SIZE; //Dividing by TILE_SIZE to get the tile number
		if(playerTileY == tileY)
			if(isPlayerInRange(player)) 
				return true; 		
		return false;
	}
	
	//Checks the player's proximity around the enemy's vision
	protected boolean isPlayerInRange(Player player) {
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		switch(enemyType) {
		case ALPHA, NINJA_CAPTAIN:
			return absValue <= attackDistance * 20;
		case WIND_NINJA:
			return absValue <= attackDistance * 10;
		case NECROMANCER:
			return absValue <= attackDistance * 25;
		default:
			return absValue <= attackDistance * 5;
		}
		 
	}
	
	//Checks the player's proximity around the enemy's attacking range 
	protected boolean isPlayerCloseForAttack(Player player, int enemyType) {
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		switch(enemyType) {
		case ALPHA, NINJA_CAPTAIN:
			return absValue <= attackDistanceAlpha;
		case WIND_NINJA:
			return absValue <= attackDistance * 3;
		case SHARK:
			return absValue <= attackDistance * 2;
		case NECROMANCER:
			return absValue <= attackDistance * 20;
		default:
			return absValue <= attackDistance;
		}
	}

	//Changes enemy state and also resets animation tick and index
	//so the animation does not start from midway
	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType,state)) {
				aniIndex = 0;
				//Checking enemy state
				switch(state) {
				case ATTACK, HIT, HEAVY_ATTACK, HEAVY_ATTACK2 -> state = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}
	
	//Damages enemy
	public void hurt(int amount) {
		currentHealth -= amount;
		if(currentHealth <= 0) {
			if(enemyType == ALPHA) {
				killedAlpha = true;
				//System.out.println("KILLED ALPHA");
			}
			newState(DEAD);
			if(enemyType == NINJA)
				ObjectManager.dropPotion(hitbox, enemyType);
			if(enemyType == NINJA_CAPTAIN)
				ObjectManager.dropPotion(hitbox, enemyType);
		}
		else
			newState(HIT);
	}
	
	public void heal(int amount) {
		currentHealth += amount;
		if(currentHealth >= this.maxHealth)
			currentHealth = this.maxHealth;
	}

	protected void checkEnemyHit(Rectangle2D.Float attackBox, int state, Player player) {
		//If enemy attackbox intersects player attackbox then it is a hit
		if(state == ATTACK) {
			if(enemyType == ALPHA) {
				Alpha.attackCounter++;
				Alpha.TotalAttackCounter++;
			}
			if(enemyType == NINJA_CAPTAIN)
				NinjaCaptain.attackCounter++;
			if(enemyType == WIND_NINJA) 
				WindNinja.attackCounter++;
			if(attackBox.intersects(player.hitbox)) {
				player.changeHealth(-GetEnemyDmg(enemyType));
				if(enemyType == ALPHA)
					heal(20);
			}
		}
		else if(state == HEAVY_ATTACK) {
			if(enemyType == ALPHA) {
				Alpha.TotalAttackCounter++;
				if(attackBox.intersects(player.hitbox)) {
					heal(30);
					player.changeHealth(-(GetEnemyDmg(enemyType) + 10));
					if(player.hitbox.x > hitbox.x)
						player.knockback(40);
					else
						player.knockback(-40);
				}
			}
			if(enemyType == WIND_NINJA) {
				if(attackBox.intersects(player.hitbox)) {
					player.changeHealth(-(GetEnemyDmg(enemyType) + 5));
					player.slow(1.3f);
					player.inflictDamageOverTime(5.0f);
				}
			}
			if(enemyType == NINJA_CAPTAIN) {
				NinjaCaptain.heavyAttackCounter++;
				if(attackBox.intersects(player.hitbox)) {
					player.changeHealth(-(GetEnemyDmg(enemyType) + 10));
					player.slow(1.5f);
					if(player.hitbox.x > hitbox.x)
						player.knockback(10);
					else
						player.knockback(-10);
				}
			}
			newState(RUNNING);
		}
		else if(state == HEAVY_ATTACK2) {
			if(enemyType == NINJA_CAPTAIN) {
				if(attackBox.intersects(player.hitbox)) {
					player.changeHealth(-(GetEnemyDmg(enemyType) + 20));
					player.slow(2f);
					if(player.hitbox.x > hitbox.x)
						player.knockback(30);
					else
						player.knockback(-30);
				}
			}
			newState(RUNNING);
		}
		attackChecked = true;
	}

	protected void shootSpell(Rectangle2D.Float playerHitbox) {
		int dir = 1;
		if(flipW() == -1)
			dir = -1;
		if(aniTick == 0 && aniIndex == 7)
			ObjectManager.spells.add(new Spell((int)hitbox.x - 10, (int)hitbox.y - 50, dir, playerHitbox));
		attackChecked = true;
	}
	
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));
		else {
			if (enemyType == SHARK)
				return;
		}
		attackChecked = true;
	}
	
	protected void changeWalkDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public int getHealth() {
		return currentHealth;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return 1;
		else
			return -1;
	}

}
