package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.ATTACK;
import static utils.Constants.EnemyConstants.HEAVY_ATTACK;
import static utils.Constants.EnemyConstants.HIT;
import static utils.Constants.EnemyConstants.IDLE;
import static utils.Constants.EnemyConstants.RUNNING;
import static utils.Constants.EnemyConstants.*;

import java.awt.geom.Rectangle2D;

import main.Game;

public class NinjaCaptain extends Enemy{
	
	private int attackBoxOffsetX;
	public static int attackCounter = 0;
	public static int heavyAttackCounter = 0;

	public NinjaCaptain(float x, float y) {
		super(x, y, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT, NINJA_CAPTAIN);
		initHitbox(43, 57);
		initAttackBox();
		walkSpeed = 1.5f;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,51,57);
		attackBoxOffsetX = 10;
	}
	
	public void update(int[][] lvlData,  Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateAttackBox() {
		if(walkDir == RIGHT) {
			attackBox.x = hitbox.x + hitbox.width;
		}else if(walkDir == LEFT) {
			attackBox.x = hitbox.x - hitbox.width - 10 - attackBoxOffsetX;
		}
		attackBox.y = hitbox.y - 10;
	}

	private void updateBehaviour(int[][] lvlData, Player player) {
		//Enemy falls if spawns in air
		if(firstUpdate) 
			firstUpdateCheck(lvlData);
		if(inAir)
			updateInAir(lvlData);
		else {
			//Enemy patrols
			switch(state) {
			case IDLE:
				if(canSeePlayer(lvlData,player)) {
					turnTowardsPlayer(player);
					newState(RUNNING);
				}
				break;
			case RUNNING:
				if(canSeePlayer(lvlData,player)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player, enemyType))
						newState(ATTACK);
				}
				else
					newState(IDLE);
				move(lvlData);
				break;
			case ATTACK:
				if(attackCounter >= 2) {
					newState(HEAVY_ATTACK);
					attackCounter = 0;
				}
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 4 && !attackChecked)
					checkEnemyHit(attackBox,state,player);
				break;
			case HEAVY_ATTACK:
				if(heavyAttackCounter >= 2) {
					newState(HEAVY_ATTACK2);
					heavyAttackCounter = 0;
				}
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 3 && !attackChecked)
					checkEnemyHit(attackBox,state,player);
				break;
			case HEAVY_ATTACK2:
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 8 && !attackChecked)
					checkEnemyHit(attackBox,state,player);
				break;
			case HIT:
				break;
			}
		}
	}

	public int flipX() {
		if(walkDir == RIGHT)
			return 0;
		else
			return width + 150;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return 1;
		else
			return -1;
	}

}
