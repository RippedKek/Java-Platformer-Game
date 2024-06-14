package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.ATTACK;
import static utils.Constants.EnemyConstants.HIT;
import static utils.Constants.EnemyConstants.IDLE;
import static utils.Constants.EnemyConstants.RUNNING;
import static utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

public class Alpha extends Enemy {
	
	private int attackBoxOffsetX;
	public static int attackCounter = 0;
	public static int TotalAttackCounter = 0;
	public static boolean Alpha_Attack = false;
	
	private BufferedImage statusBarImg;
	private BufferedImage alphaName;
	
	private int statusBarWidth = (int)(192 * Game.SCALE);
	private int statusBarHeight = (int)(58 * Game.SCALE);
	private int statusBarX = 1500;
	private int statusBarY = 20;
	
	private int healthBarWidth = (int)(150 * Game.SCALE);
	private int healthBarHeight = (int)(4 * Game.SCALE);
	private int healthBarXStart = 1130;
	private int healthBarYStart = (int)(24 * Game.SCALE);
	
	private int healthWidth = healthBarWidth;

	public Alpha(float x, float y) {
		super(x, y, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT, ALPHA);
		initHitbox(21, 54);
		initAttackBox();
		walkSpeed = (int)(0.5 * Game.SCALE);
		statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
		alphaName = LoadSave.getSpriteAtlas(LoadSave.ALPHA_NAME);
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,50,54);
		attackBoxOffsetX = 25;
	}
	
	public void update(int[][] lvlData,  Player player) {
		updateHealthBar();
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateHealthBar() {
		//Calculating the size of the health indicator according to the Alpha health
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
	}
	
	public void drawUI(Graphics g) {
		g.drawImage(alphaName, statusBarX - 180, statusBarY - 26 , 128, 50, null);
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth * (-1), statusBarHeight, null);
		//Health Indicator
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart, healthBarYStart, healthWidth , healthBarHeight);
	}
	
	private void updateAttackBox() {
		if(walkDir == RIGHT) {
			attackBox.x = hitbox.x + hitbox.width;
		}else if(walkDir == LEFT) {
			attackBox.x = hitbox.x - hitbox.width - 15;
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
			case ATTACK:
				if(attackCounter >= 4) {
					newState(HEAVY_ATTACK);
					Alpha_Attack = true;
					attackCounter = 0;
				}
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 3 && !attackChecked)
					checkEnemyHit(attackBox,state,player);
				break;
			case HEAVY_ATTACK:
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
