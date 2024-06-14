package entities;

import static utils.Constants.EnemyConstants.*;

import static utils.Constants.Directions.*;

public class Necromancer extends Enemy {

	public Necromancer(float x, float y) {
		super(x, y, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT, NECROMANCER);
		//19 and 46 actual dimensions of the enemy sprite
		initHitbox(30, 46);
		//initAttackBox();
	}

	public void update(int[][] lvlData,  Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		//updateAttackBox();
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
				if(canSeePlayer(lvlData,player,enemyType)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player, enemyType))
						newState(ATTACK);
				}
				break;
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 7 && !attackChecked) {
					shootSpell(player.getHitbox());
				}
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
