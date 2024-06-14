package entities;

import static utils.Constants.playerConstants.GetSpriteAmount;
import static utils.Constants.playerConstants.*;
import static utils.Constants.GRAVITY;
import static utils.Constants.ANI_SPEED;
import static utils.Constants.playerConstants.DAMAGE_MODIFIER;
import static utils.HelpMethods.*;
import static utils.Constants.playerConstants.HIT;
import static objects.ObjectManager.fireballs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import gameStates.Playing;
import main.Game;
import objects.Fireball;
import utils.LoadSave;

public class Player extends Entity{
	
	private BufferedImage[][] animation;
	private BufferedImage[] lightning, smokeImg;
	private BufferedImage undyingRageImg, fireballCountImg;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int aniSpeed = 25;
	private int[][] levelData;
	private float xDrawOffset = 20;
	private float yDrawOffset = 16;
	
	//Jumping
	private float jumpSpeed = -2.5f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	
	//Status and Health Bar
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int)(192 * Game.SCALE);
	private int statusBarHeight = (int)(58 * Game.SCALE);
	private int statusBarX = (int)(10 * Game.SCALE);
	private int statusBarY = (int)(10 * Game.SCALE);
	
	private int healthBarWidth = (int)(150 * Game.SCALE);
	private int healthBarHeight = (int)(4 * Game.SCALE);
	private int healthBarXStart = (int)(34 * Game.SCALE);
	private int healthBarYStart = (int)(14 * Game.SCALE);
	
	//Power Bar
	private int powerBarWidth = (int)(104 * Game.SCALE);
	private int powerBarHeight = (int)(2 * Game.SCALE);
	private int powerBarXStart = (int)(44 * Game.SCALE);
	private int powerBarYStart = (int)(34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
	private int powerIncreaseValue = 1;
	
	//Effect Icons
	private BufferedImage slowImg, poisonImg, dmgImg;
	private ArrayList<BufferedImage> icons = new ArrayList<>();
	private int iconsX = 20;
	private int iconsYStart = 200;
	private int iconsYOffset = 80;
	private int slowX = 20, slowY = 200;
	private int poisonX = 20, poisonY = 280;
	private int dmgX = 20, dmgY = 360;
	
	//Player health
	private int healthWidth = healthBarWidth;
	
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked;
	private boolean attacked = false;
	private Playing playing;
	
	private int tileY = 0;
	
	private boolean isKnockback = false;
	private int knockBackDist, knockBackDir;
	
	private boolean inAirOnSpike = false;
	
	private long currentTime;
	private long damageTime;
	private boolean isSlow = false;
	private float normalWalkSpeed;
	
	private boolean damageOverTime = false;
	private float damageOTAmount;
	private int damageOTCounter = 0;
	private boolean damaging = false;
	
	private boolean dmgMod = false;
	
	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 10;
	private int powerGrowTick;
	
	private int damageAmount = 0;
	private float defense = 0;
	
	private boolean ultimate = false;
	private long ultTimer;
	private int lightningTick = 0;
	private int lightningIndex = 0;
	private int lightningLimit = 9;
	
	private int smokeTick = 0;
	private int smokeIndex = 0;
	private int smokeLimit = 8;
	private int smokeFlipX;
	
	private int fireballAmount = 0;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x,y,width,height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 2000;
		this.currentHealth = maxHealth;
		this.walkSpeed = 1f * Game.SCALE;
		this.normalWalkSpeed = walkSpeed;
		loadAnimations();
		//20 and 47 are respectively the width and height of the character
		initHitbox((int) (26), (int) (52));
		initAttackBox();
	}
	
	//Sets player spawn point
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, 35, 30);
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();
		if(currentHealth <= 0) {
			if(inAir && !inAirOnSpike)
				updatePosition();
			else if(state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
			}else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) { 
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_DEAD);
			}
			else
				updateAnimationTick();
			return;
		}
		
		updateAttackBox();
		updatePosition();
		
		if(moving) {
			checkPotionTouched();
			if(!inAirOnSpike)
				checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if(powerAttackActive) {
				powerAttackTick++;
				if(powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}
		
		if(attacking || powerAttackActive)
			checkAttack();
		if(isKnockback)
			updateKnockback();
		if(isSlow)
			updateSlow();
		if(damageOverTime)
			updateDamageOverTime();
		if(dmgMod)
			updateDamageModifier();
		if(ultimate)
			updateUltimate();
		updateAnimationTick();
		setAnimation();
	}
	
	private void updateUltimate() {
		long now = System.currentTimeMillis();
		if(now - ultTimer >= 7000) {
			ultimate = false;
			defense = 0;
			walkSpeed = normalWalkSpeed;
			DAMAGE_MODIFIER = 0;
			powerIncreaseValue = 1;
		}
	}

	private void updatePowerBar() {
		powerWidth = (int)((powerValue / (float) powerMaxValue) * powerBarWidth);
		powerGrowTick++;
		if(powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(powerIncreaseValue);
		}
	}

	private void updateDamageModifier() {
		long now = System.currentTimeMillis();
		if(now - damageTime >= 5000) {
			DAMAGE_MODIFIER = 0;
			dmgMod = false;
			icons.remove(icons.indexOf(dmgImg));
		}
	}

	private void updateDamageOverTime() {
		damageOTCounter++;
		if(damaging) {
			currentHealth -= damageOTAmount;
			if(currentHealth <= 0) {
				currentHealth = 1;
				resetDamageOverTime();
			}
			damaging = false;
		}
		if(damageOTCounter % 120 == 0) {
			damaging = true;
		}
		if(damageOTCounter >= 1000) {
			resetDamageOverTime();
		}
	}
	
	private void resetDamageOverTime() {
		damageOTCounter = 0;
		damageOverTime = false;
		damaging = false;
		icons.remove(icons.indexOf(poisonImg));
	}

	private void updateSlow() {
		long now = System.currentTimeMillis();
		if(now - currentTime >= 3000) {
			isSlow = false;
			walkSpeed = normalWalkSpeed;
			icons.remove(icons.indexOf(slowImg));
		}
	}

	private void updateKnockback() {
		if(knockBackDist <= 0)
			isKnockback = false;
		knockBackDist -= (walkSpeed);
		if(knockBackDir < 0) 
			updateXPos(-knockBackDist);
		else 
			updateXPos(knockBackDist);
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		if(powerAttackActive)
			attackChecked = false;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if(right || (powerAttackActive && flipW == 1)) {
			attackBox.x = hitbox.x + hitbox.width + 6;
		}else if(left || (powerAttackActive && flipW == -1)) {
			attackBox.x = hitbox.x - hitbox.width - 6;
		}
		attackBox.y = hitbox.y;
	}

	private void updateHealthBar() {
		//Calculating the size of the health indicator according to the player health
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
	}
	
	public void kill() {
		inAirOnSpike = true;
		inAir = true;
		currentHealth = 0;
	}
	
	public void shootFireball() {
		if(fireballAmount != 0) {
			fireballs.add(new Fireball((int)hitbox.x - 30, (int)hitbox.y - 20, flipW));
			fireballAmount--;
		}
	}
	
	public void getFireball() {
		fireballAmount++;
	}

	public void render(Graphics g,int lvlOffset) {
		g.drawImage(animation[state][aniIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX - 51, (int)(hitbox.y - yDrawOffset) - 35, width * flipW, height, null); //6th argument used for monitoring the state of the image before it is drawn
		//drawHitbox(g, lvlOffset);
		//drawAttackBox(g,lvlOffset);
		drawUI(g);
//		if(isSlow)
//			g.drawImage(slowImg, slowX, slowY, (int)(32 * 1.5), (int)(32 * 1.5), null);
//		if(damageOverTime)
//			g.drawImage(poisonImg, poisonX, poisonY, (int)(32 * 1.5), (int)(32 * 1.5), null);
//		if(dmgMod)
//			g.drawImage(dmgImg, dmgX, dmgY, (int)(32 * 1.5), (int)(32 * 1.5), null);
		for(int i=0;i<icons.size();i++)
			g.drawImage(icons.get(i), iconsX, iconsYStart + iconsYOffset * i, (int)(32 * 1.5), (int)(32 * 1.5), null);
		if(ultimate) {
			g.drawImage(lightning[lightningIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset, (int)(hitbox.y - yDrawOffset) + 20, 64, 64, null);
			g.drawImage(undyingRageImg, (int)(Game.GAME_WIDTH / 2.25), 20, 64, 64, null);
		}
		if(state == RUN)
			g.drawImage(smokeImg[smokeIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + smokeFlipX - 20, (int)(hitbox.y - yDrawOffset) + 50, 40 * flipW, 16, null);
		for(int i=0;i<fireballAmount;i++)
			g.drawImage(fireballCountImg, 1450, 730, 48, 48, null);
	}

	private void drawUI(Graphics g) {
		//BG UI
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		//Health Indicator
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		//Power Indicator
		g.setColor(Color.YELLOW);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				attacked = false;
			}
		}
		if(ultimate) {
			lightningTick++;
			if(lightningTick >= 25) {
				lightningTick = 0;
				lightningIndex++;
				if(lightningIndex >= lightningLimit) 
					lightningIndex = 0;
			}
		}
		if(state == RUN) {
			smokeTick++;
			if(smokeTick >= 25) {
				smokeTick = 0;
				smokeIndex++;
				if(smokeIndex >= smokeLimit)
					smokeIndex = 0;
			}
		}
	}
	
	private void setAnimation() {
		
		int startAni = state;
		
		if(attacked) {
			state = HIT;
			aniSpeed = 20;
		}	
		else if(moving) {
			state = RUN;
			aniSpeed = 120 / GetSpriteAmount(state);
		}
		else {
			state = IDLE;
			aniSpeed = 120 / GetSpriteAmount(state);
		}
		
		if(inAir) {
			if(airSpeed < 0) {
				state = JUMP_UP;
				aniSpeed = 120 / GetSpriteAmount(state);
			}
			else {
				state = JUMP_DOWN;
				aniSpeed = 120 / GetSpriteAmount(state);
			}
		}
		
		if(powerAttackActive) {
			state = ATTACK;
			aniIndex = 5;
			aniTick = 0;
			return;
		}
		
		if(attacking) {
			state = ATTACK;
			//aniSpeed = 120 / GetSpriteAmount(state);
			aniSpeed = 15;
			if(startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		
		if(startAni != state)
			resetAniTick();
	}
	
	public void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}
	
	private void updatePosition() {
		moving = false;
		
		if(jump)
			jump();
		
		if (!inAir) 
			if(!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;
		
		float xSpeed = 0;
		
		if(left) {
			xSpeed -= walkSpeed;
			flipX = width;
			smokeFlipX = 110;
			flipW = -1;
		}
		if(right) {
			xSpeed += walkSpeed;
			flipX = 0;
			smokeFlipX = 0;
			flipW = 1;
		}
		
		if(powerAttackActive) {
			//if power attack but not pressing anything
			if(!left && !right) {
				if(flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}
			xSpeed *= 3;
		}
		
		if(!inAir)
			if(!IsEntityOnFloor(hitbox,levelData))
				inAir = true;
		
		if(inAir && !powerAttackActive) {
			if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,levelData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			}else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		}else 
			updateXPos(xSpeed);
		moving = true;
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += xSpeed;
		}else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if(powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}
	
	//Changing player health based on enemy attack or power up
	public void changeHealth(int value) {
		if(value < 0) {
			attacked = true;
			if(playing.getLevelManager().getLvlIndex() == 5)
				damageAmount += Math.abs(value);
		}
		if(ultimate)
			defense = (float) (value * 0.5);
		currentHealth += (value - defense);
		if(currentHealth <= 0) {
			//Stop health indicator from underflowing
			currentHealth = 0;
			aniSpeed = 25;
			//gameOver();
		}else if(currentHealth >= maxHealth){
			//Stop overflowing
			currentHealth = maxHealth;
		}
	}
	
	public void powerAttack() {
		if(powerAttackActive)
			return;
		if(powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}
	}
	
	public void undyingRage() {
		if(ultimate)
			return;
		if(powerValue >= 200) {
			ultTimer = System.currentTimeMillis();
			ultimate = true;
			walkSpeed = 2.5f;
			DAMAGE_MODIFIER = 10;
			powerIncreaseValue = 0;
			changePower(-200);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_ULT);
		}
	}
	
	public void knockback(int xSpeed) {
		isKnockback = true;
		knockBackDist = Math.abs(xSpeed);
		if(xSpeed < 0)
			knockBackDir = -1;
		else
			knockBackDir = 1;
	}
	
	public void slow(float slowAmount) {
		if(!isSlow) {
			currentTime = System.currentTimeMillis();
			isSlow = true;
			walkSpeed -= slowAmount;
			icons.add(slowImg);
		}
	}
	
	public void inflictDamageOverTime(float value) {
		if(!damageOverTime) {
			currentTime = System.currentTimeMillis();
			damageOverTime = true;
			damageOTAmount = value;
			icons.add(poisonImg);
		}
	}
	
	public void changePower(int value) {
		powerValue += value;
		if(powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if(powerValue <= 0)
			powerValue = 0;
	}
	
	public void changeAttack(int value) {
		if(!dmgMod) {
			damageTime = System.currentTimeMillis();
			DAMAGE_MODIFIER = value;
			dmgMod = true;
			icons.add(dmgImg);
		}
	}

	private void loadAnimations() {
		BufferedImage attack = LoadSave.getSpriteAtlas(LoadSave.ATTACK_ATLAS);
		BufferedImage dead = LoadSave.getSpriteAtlas(LoadSave.DEAD_ATLAS);
		BufferedImage idle = LoadSave.getSpriteAtlas(LoadSave.IDLE_ATLAS);
		BufferedImage jump_start = LoadSave.getSpriteAtlas(LoadSave.JUMP_START_ATLAS);
		BufferedImage jump_end = LoadSave.getSpriteAtlas(LoadSave.JUMP_END_ATLAS);
		BufferedImage run = LoadSave.getSpriteAtlas(LoadSave.RUN_ATLAS);
		BufferedImage hit = LoadSave.getSpriteAtlas(LoadSave.HIT_ATLAS);
		animation = new BufferedImage[7][15];
		for(int i=0;i<GetSpriteAmount(ATTACK);i++)
			animation[0][i] = attack.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(DEAD);i++)
			animation[1][i] = dead.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(IDLE);i++)
			animation[2][i] = idle.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(JUMP_UP);i++)
			animation[3][i] = jump_start.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(JUMP_DOWN);i++)
			animation[4][i] = jump_end.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(RUN);i++)
			animation[5][i] = run.getSubimage(i * 200, 0, 200, 200);
		for(int i=0;i<GetSpriteAmount(HIT);i++)
			animation[6][i] = hit.getSubimage(i * 200, 0, 200, 200);
		
		statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
		
		BufferedImage icons = LoadSave.getSpriteAtlas(LoadSave.ICONS_SHEET);
		poisonImg = icons.getSubimage(24 * 32, 3 * 32, 32, 32);
		slowImg = icons.getSubimage(4 * 32, 6 * 32, 32, 32);
		dmgImg = icons.getSubimage(4 * 32, 15 * 32, 32, 32);
		
		BufferedImage lightningTemp = LoadSave.getSpriteAtlas(LoadSave.PLAYER_LIGHTNING);
		lightning = new BufferedImage[9];
		for(int i=0;i<3;i++)
			lightning[i] = lightningTemp.getSubimage(i * 128, 0 * 128, 128, 128);
		for(int i=3;i<6;i++)
			lightning[i] = lightningTemp.getSubimage((i - 3) * 128, 1 * 128, 128, 128);
		for(int i=6;i<9;i++)
			lightning[i] = lightningTemp.getSubimage((i - 6) * 128, 2 * 128, 128, 128);
		
		undyingRageImg = LoadSave.getSpriteAtlas(LoadSave.UNDYING_RAGE);
		
		smokeImg = new BufferedImage[8];
		BufferedImage smokeTemp = LoadSave.getSpriteAtlas(LoadSave.SMOKE_EFFECT);
		for(int i=0;i<8;i++)
			smokeImg[i] = smokeTemp.getSubimage(i * 40, 0, 40, 16);
		
		fireballCountImg = LoadSave.getSpriteAtlas(LoadSave.SKULL_IMG).getSubimage(0, 0, 24, 24);
	}
	
	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if(!IsEntityOnFloor(hitbox,levelData))
			inAir = true;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public int getFLipW() {
		return flipW;
	}
	
	public int getTileY() {
		return tileY;
	}
	
	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		inAirOnSpike = false;
		isSlow = false;
		walkSpeed = normalWalkSpeed;
		damageOverTime = false;
		damaging = false;
		damageOTCounter = 0;
		attacking = false;
		moving = false;
		attacked = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		DAMAGE_MODIFIER = 0;
		dmgMod = false;
		ultimate = false;
		fireballAmount = 0;
		powerIncreaseValue = 1;
		powerValue = powerMaxValue;
		if(!IsEntityOnFloor(hitbox,levelData))
			inAir = true;
	}
	
	public int getDmgAmount() {
		return damageAmount;
	}
	
	public BufferedImage[] getPlayerAnimation() {
		return animation[IDLE];
	}

}