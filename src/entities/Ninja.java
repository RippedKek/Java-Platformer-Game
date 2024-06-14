package entities;

import static utils.Constants.EnemyConstants.*;

import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;

public class Ninja extends Enemy {
	
	private int attackBoxOffsetX;

	public Ninja(float x, float y) {
		super(x, y, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT, NINJA);
		//19 and 46 actual dimensions of the enemy sprite
		initHitbox(16, 56);
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,35,30);
		attackBoxOffsetX = 25;
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
			attackBox.x = hitbox.x - hitbox.width - 10;
		}
		attackBox.y = hitbox.y - 3;
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
				state = RUNNING;
				break;
			case RUNNING:
				if(canSeePlayer(lvlData,player)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player, enemyType))
						newState(ATTACK);
				}
				move(lvlData);
				break;
			case FALL:
				if(!inAir)
					newState(IDLE);
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 3 && !attackChecked)
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
			return width;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return 1;
		else
			return -1;
	}

}
