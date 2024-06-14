package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.IsFloor;

import java.awt.geom.Rectangle2D;

import gameStates.Playing;
import objects.ObjectManager;
import objects.Projectile;

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT, SHARK);
		initHitbox(36, 44);
		initAttackBox(50,20,50);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip(attackBoxOffsetX);
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, lvlData))
					newState(RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					shootProjectile();
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer(),SHARK))
						newState(ATTACK);
				}

				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(lvlData, playing);
				}

				break;
	 		case HIT:
				break;
			}
		}
	}
	
	private void shootProjectile() {
		int dir = -1;
		if(flipW() == -1)
			dir = 1;
		if((aniTick == 0 && aniIndex == 0) || (aniTick == 25 && aniIndex == 4))
			ObjectManager.projectiles.add(new Projectile((int)hitbox.x, (int)hitbox.y - 5, dir));
	}

	protected void attackMove(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
				hitbox.x += xSpeed * 4;
				return;
			}
		newState(IDLE);
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return 0;
		else
			return -70;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
	
}
