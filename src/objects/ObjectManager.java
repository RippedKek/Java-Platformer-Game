package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.Enemy;
import entities.Player;
import gameStates.Playing;
import levels.Levels;
import main.Game;
import utils.LoadSave;
import static utils.Constants.ObjectConstants.*;
import static utils.HelpMethods.CanCannonSeePlayer;
import static utils.HelpMethods.IsProjectileHittingLevel;
import static utils.Constants.Projectiles.*;
import static utils.Constants.EnemyConstants.NINJA;
import static utils.Constants.EnemyConstants.NINJA_CAPTAIN;

public class ObjectManager {
	
	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs, grassImgs, chestImgs, shineImgs, fireballImgs;
	private BufferedImage spikeImg, cannonBallImg, spellImg;
	private BufferedImage control, attack, ultimate;
	private static ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Chest> chests;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private ArrayList<TutorialBanners> banners;
	public static ArrayList<Projectile> projectiles = new ArrayList<>();
	public static ArrayList<Spell> spells = new ArrayList<>();
	public static ArrayList<Fireball> fireballs = new ArrayList<>();
	
	private boolean alreadyDead = false;
	
	private int shineIndex = 0;
	private int shineTick = 0;
	
	private Levels currentLevel;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		currentLevel = playing.getLevelManager().getCurrentLevel();
		loadImgs();
	}
	
	//If player touches a spike
	public void checkSpikesTouched(Player player) {
		for(Spike s : spikes)
			if(s.getHitbox().intersects(player.getHitbox()) && !alreadyDead) {
				alreadyDead = true;
				player.kill();
			}
				
	}
	
	public void checkSpikesTouched(Enemy e) {
		for (Spike s : currentLevel.getSpikes())
			if (s.getHitbox().intersects(e.getHitbox()))
				e.hurt(200);
	}
	
	//If player touches an pickable object
	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for(Potion p : potions)
			if(p.isActive()) {
				//If player hitbox intersects potion hitbox
				if(hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}
	
	//Applies effect of potions on player
	public void applyEffectToPlayer(Potion p) {
		//Red potion increases health
		if(p.getObjType() == RED_POTION) {
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.POTION);
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
			//playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HEAL);
		}
		//Blue potion increases attack
		else if(p.getObjType() == BLUE_POTION){
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.POTION);
			playing.getPlayer().changeAttack(BLUE_POTION_VALUE);
			//playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ATT_BUFF);
		}
		else if(p.getObjType() == TREASURE) {
			playing.pickTreasure();
		}
		else if(p.getObjType() == SKULL) {
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIREBALL_PICKUP);
			playing.getPlayer().getFireball();
		}
	}
	
	//Check if player breaks any breakable object
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		for(GameContainer gc : containers)
			//Without doAnimation condition checking, spam attacking
			//resulted in unlimited potion drop
			if(gc.isActive() && !gc.doAnimation) {
				if(gc.getHitbox().intersects(attackbox)) {
					//If object gets hit, animate it
					gc.setAnimation(true);
					int type = 0;
					if(gc.getObjType() == BARREL)
						type = 1;
					//Breaking an object drops potions 
					potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2), 
							(int)(gc.getHitbox().y - gc.getHitbox().height / 2),
							type));
					return;
				}
			}
		
		for(Chest c : chests)
			//Without doAnimation condition checking, spam attacking
			//resulted in unlimited potion drop
			if(c.isActive() && !c.doAnimation) {
				if(c.getHitbox().intersects(attackbox)) {
					//If object gets hit, animate it
					c.setAnimation(true);
					//Breaking an object drops potions 
					potions.add(new Potion((int)(c.getHitbox().x + c.getHitbox().width / 2), 
							(int)(c.getHitbox().y - c.getHitbox().height / 2),
							TREASURE));
					return;
				}
			}
	}
	
	//For enemy ninja
	public static void dropPotion(Rectangle2D.Float hitbox, int enemyType) {
		switch(enemyType) {
		case NINJA:
			potions.add(new Potion((int)(hitbox.x + hitbox.width / 2), 
					(int)(hitbox.y - hitbox.height / 2),
					RED_POTION));
			break;
		case NINJA_CAPTAIN:
			potions.add(new Potion((int)(hitbox.x + hitbox.width / 2), 
					(int)(hitbox.y - hitbox.height / 2),
					SKULL));
			break;
		}
	}
	
	public void loadObjects(Levels newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		chests = new ArrayList<>(newLevel.getChests());
		banners = new ArrayList<>(newLevel.getBanners());
		//Do not need to reset the spikes that's why
		//call getSpikes call is like this
		spikes = newLevel.getSpikes();
		cannons = newLevel.getCannons();
		//Clear list everytime a new level is called 
		projectiles.clear();
		spells.clear();
		fireballs.clear();
	}

	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.getSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[10][8];
		for(int j=0;j<2;j++) {
			for(int i=0;i<7;i++)
				potionImgs[j][i] = potionSprite.getSubimage(i * 12, j * 16, 12, 16);
		}
		//Treasure Image
		BufferedImage treasureTemp = LoadSave.getSpriteAtlas(LoadSave.TREASURE_IMG);
		for(int i=0;i<4;i++)
			potionImgs[TREASURE][i] = treasureTemp.getSubimage(15 * 16, 4 * 16, 16, 16);
		//Skull Image
		for(int i=0;i<8;i++)
			potionImgs[SKULL][i] = LoadSave.getSpriteAtlas(LoadSave.SKULL_IMG).getSubimage(i * 24, 0, 24, 24);
		shineImgs = new BufferedImage[7];
		BufferedImage shineTemp = LoadSave.getSpriteAtlas(LoadSave.SHINE_SHEET);
		for(int i=0;i<7;i++)
			shineImgs[i] = shineTemp.getSubimage(i * 16, 9 * 16, 16, 16);
		
		BufferedImage containerSprite = LoadSave.getSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		for(int j=0;j<containerImgs.length;j++) {
			for(int i=0;i<containerImgs[j].length;i++)
				containerImgs[j][i] = containerSprite.getSubimage(i * 40, j * 30, 40, 30);
		}
		
		spikeImg = LoadSave.getSpriteAtlas(LoadSave.TRAP_ATLAS);
		
		cannonImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);
		for(int i=0;i<cannonImgs.length;i++)
			cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
		
		cannonBallImg = LoadSave.getSpriteAtlas(LoadSave.CANNON_BALL);
		
		spellImg = LoadSave.getSpriteAtlas(LoadSave.SPELL_IMG);
		
		BufferedImage grassTemp = LoadSave.getSpriteAtlas(LoadSave.GRASS_ATLAS);
		
		grassImgs = new BufferedImage[2];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
		
		BufferedImage chestTemp = LoadSave.getSpriteAtlas(LoadSave.CHEST_ATLAS);
		chestImgs = new BufferedImage[5];
		for(int i=0;i<GetSpriteAmount(CHEST);i++)
			chestImgs[i] = chestTemp.getSubimage(i * CHEST_WIDTH_DEFAULT, 0, CHEST_WIDTH_DEFAULT, CHEST_HEIGHT_DEFAULT);
		
		BufferedImage fireballTemp = LoadSave.getSpriteAtlas(LoadSave.FIREBALL);
		fireballImgs = new BufferedImage[5];
		for(int i=0;i<5;i++)
			fireballImgs[i] = fireballTemp.getSubimage(i * 64, 0, 64, 64);
		
		control = LoadSave.getSpriteAtlas(LoadSave.CONTROL_BASIC);
		attack = LoadSave.getSpriteAtlas(LoadSave.ATTACK_BASICS);
		ultimate = LoadSave.getSpriteAtlas(LoadSave.ULTIMATE_BASICS);
	}
	
	public void update(int[][] lvlData, Player player) {
		for(Potion p : potions) 
			if(p.isActive())
				p.update();
		updateShine();
		for(GameContainer gc : containers)
			if(gc.isActive())
				gc.update();
		for(Chest c : chests)
			if(c.isActive())
				c.update();
		updateCannon(lvlData, player);
		updateProjectiles(lvlData, player);
		updateSpells(lvlData, player);
		updateFireballs(lvlData);
	}
	
	private void updateFireballs(int[][] lvlData) {
		for(Fireball f : fireballs)
			if(f.isActive()) {
				f.updatePos();
				f.updateAnimation();
				if(IsProjectileHittingLevel(f, lvlData))
					//if projectile hits level border, then deactivate it 
					f.setActive(false);
			}
	}

	private void updateShine() {
		shineTick++;
		if(shineTick >= 24) {
			shineIndex++;
			shineTick = 0;
			if(shineIndex >= 4)
				shineIndex = 0;
		}
	}

	private void updateSpells(int[][] lvlData, Player player) {
		for(Spell s : spells)
			if(s.isActive()) {
				s.updatePos();
				if(s.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-35);
					//TODO: change knockback
					if(s.getDir() == 1)
						player.knockback(5);
					else
						player.knockback(-5);
					s.setActive(false);
				}else if(IsProjectileHittingLevel(s, lvlData))
					//if projectile hits level border, then deactivate it 
					s.setActive(false);
			}
	}

	private void updateProjectiles(int[][] lvlData, Player player) {
		for(Projectile p : projectiles)
			if(p.isActive()) {
				p.updatePos();
				if(p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					if(p.getDir() == 1)
						player.knockback(20);
					else
						player.knockback(-20);
					p.setActive(false);
				}else if(IsProjectileHittingLevel(p, lvlData))
					//if projectile hits level border, then deactivate it 
					p.setActive(false);
			}
	}

	private void updateCannon(int[][] lvlData, Player player) {
		for(Cannon c : cannons) {
			if(!c.doAnimation) 
				if(c.getTileY() == player.getTileY()) 
					if(isPlayerInRange(c,player))
						if(isPlayerInFrontofCannon(c,player))
							if(CanCannonSeePlayer(lvlData,player.getHitbox(),c.getHitbox(),c.getTileY()))  
								c.setAnimation(true);
			c.update();
			//For giving an illusion that the cannon is first loading then shooting
			if(c.getAniIndex() == 4 && c.getAniTick() == 0)
				shootCannon(c);
		}
		/*
		 * if cannon not animating
		 * tileY is same
		 * if player is in range
		 * is player in front of cannon
		 * shoot the cannon
		 */
	}

	private void shootCannon(Cannon c) {
		//Default direction right
		int dir = 1;
		if(c.getObjType() == CANNON_LEFT)
			dir = -1;
		projectiles.add(new Projectile((int)c.getHitbox().x, (int)c.getHitbox().y, dir));
	}

	private boolean isPlayerInFrontofCannon(Cannon c, Player player) {
		if(c.getObjType() == CANNON_LEFT) {
			if(c.getHitbox().x > player.getHitbox().x)
				return true;
		} else if(c.getHitbox().x < player.getHitbox().x)
			return true;
		return false;
	}

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int)Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 5;
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawChests(g, xLvlOffset);
		drawTraps(g, xLvlOffset);
		drawCannons(g, xLvlOffset);
		drawProjectiles(g, xLvlOffset);
		drawSpell(g, xLvlOffset);
		drawGrass(g, xLvlOffset);
		drawFireball(g, xLvlOffset);
		drawBanners(g, xLvlOffset);
	}
	
    private void drawBanners(Graphics g, int xLvlOffset) {
		for(TutorialBanners tb : banners) {
			if(tb.getObjType() == CONTROLS)
				g.drawImage(control,
						(int)(tb.getHitbox().x - xLvlOffset), 
						(int)(tb.getHitbox().y), 
						(int)(TUTORIAL_WIDTH_DEFAULT * 1.5), 
						(int)(TUTORIAL_HEIGHT_DEFAULT * 1.5), 
						null);
			if(tb.getObjType() == ATTACKS)
				g.drawImage(attack,
						(int)(tb.getHitbox().x - xLvlOffset), 
						(int)(tb.getHitbox().y), 
						(int)(TUTORIAL_WIDTH_DEFAULT * 1.5), 
						(int)(TUTORIAL_HEIGHT_DEFAULT * 1.5), 
						null);
			if(tb.getObjType() == ULTIMATE)
				g.drawImage(ultimate,
						(int)(tb.getHitbox().x - xLvlOffset), 
						(int)(tb.getHitbox().y), 
						(int)(TUTORIAL_WIDTH_DEFAULT * 1.5), 
						(int)(TUTORIAL_HEIGHT_DEFAULT * 1.5), 
						null);
		}
	}

	private void drawFireball(Graphics g, int xLvlOffset) {
		for(Fireball f : fireballs)
			if(f.isActive()) {
				g.drawImage(fireballImgs[f.getFireballIndex()],
						(int)(f.getHitbox().x - xLvlOffset), 
						(int)(f.getHitbox().y), 
						64, 
						64, 
						null);
				//f.drawHitbox(g, xLvlOffset);
			}
	}

	private void drawChests(Graphics g, int xLvlOffset) {
    	for(Chest c : chests)
			if(c.isActive()){
				g.drawImage(chestImgs[c.getAniIndex()],
						(int)(c.getHitbox().x - c.getxDrawOffset() - xLvlOffset), 
						(int)(c.getHitbox().y - c.getyDrawOffset()), 
						CHEST_WIDTH_DEFAULT,
						CHEST_HEIGHT_DEFAULT,
						null);
				//c.drawHitbox(g, xLvlOffset);
			}
	}

	private void drawSpell(Graphics g, int xLvlOffset) {
    	for(Spell s : spells)
			if(s.isActive())
				g.drawImage(spellImg, 
						(int)(s.getHitbox().x - xLvlOffset), 
						(int)(s.getHitbox().y), 
						CANNON_BALL_WIDTH / 2, 
						CANNON_BALL_HEIGHT / 2, 
						null);
	}

	private void drawGrass(Graphics g, int xLvlOffset) {
		for (Grass grass : currentLevel.getGrass())
			g.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY(), (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
	}

	private void drawProjectiles(Graphics g, int xLvlOffset) {
		for(Projectile p : projectiles)
			if(p.isActive())
				g.drawImage(cannonBallImg, 
						(int)(p.getHitbox().x - xLvlOffset), 
						(int)(p.getHitbox().y), 
						CANNON_BALL_WIDTH, 
						CANNON_BALL_HEIGHT, 
						null);
	}

	private void drawCannons(Graphics g, int xLvlOffset) {
		for(Cannon c : cannons) {
			int x = (int)(c.getHitbox().x - xLvlOffset);
			int width = CANNON_WIDTH;
			if(c.getObjType() == CANNON_RIGHT) {
				//Change direction
				x += width;
				width *= -1;
			}
			g.drawImage(cannonImgs[c.getAniIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null);
		}
	}

	private void drawTraps(Graphics g, int xLvlOffset) {
		for(Spike s : spikes) {
			g.drawImage(spikeImg, 
						(int)(s.getHitbox().x - xLvlOffset),
						(int)(s.getHitbox().y - s.getyDrawOffset()), 
						SPIKE_WIDTH, 
						SPIKE_HEIGHT, 
						null);
			//s.drawHitbox(g, xLvlOffset);
		}
	}

	private void drawContainers(Graphics g, int xLvlOffset) {
		for(GameContainer gc : containers)
			if(gc.isActive()){
				int type = 0;
				if(gc.getObjType() == BARREL)
					type = 1;
				g.drawImage(containerImgs[type][gc.getAniIndex()],
						(int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), 
						(int)(gc.getHitbox().y - gc.getyDrawOffset()), 
						CONTAINER_WIDTH,
						CONTAINER_HEIGHT,
						null);
				//gc.drawHitbox(g, xLvlOffset);
			}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {
		for(Potion p : potions) 
			if(p.isActive()) {
				if(p.getObjType() == RED_POTION) {
					g.drawImage(potionImgs[RED_POTION][p.getAniIndex()],
							(int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
							(int)(p.getHitbox().y - p.getyDrawOffset()), 
							POTION_WIDTH,
							POTION_HEIGHT,
							null);
				}
				else if(p.getObjType() == TREASURE) {
					g.drawImage(potionImgs[TREASURE][p.getAniIndex()],
							(int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset - 10), 
							(int)(p.getHitbox().y - p.getyDrawOffset()), 
							TREASURE_WIDTH,
							TREASURE_HEIGHT,
							null);
					g.drawImage(shineImgs[shineIndex], 
							(int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset - 10), 
							(int)(p.getHitbox().y - p.getyDrawOffset()), 
							16, 
							16, 
							null);
				}
				else if(p.getObjType() == SKULL){
					g.drawImage(potionImgs[SKULL][p.getAniIndex()],
							(int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
							(int)(p.getHitbox().y - p.getyDrawOffset()), 
							SKULL_WIDTH,
							SKULL_HEIGHT,
							null);
				}
				else if(p.getObjType() == BLUE_POTION) {
					g.drawImage(potionImgs[BLUE_POTION][p.getAniIndex()],
							(int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
							(int)(p.getHitbox().y - p.getyDrawOffset()), 
							POTION_WIDTH,
							POTION_HEIGHT,
							null);
				}
				//p.drawHitbox(g, xLvlOffset);
			}			
	}
	
	public ArrayList<Fireball> getFireball() {
		return fireballs;
	}
	
	public void resetAllObjects() {
		alreadyDead = false;
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for(Potion p : potions) 
			p.reset();
		for(GameContainer gc : containers)
			gc.reset();
		for(Cannon c : cannons)
			c.reset();
		if(utils.Constants.GAME_OVER) 
			for(Chest c : chests)
				c.reset();
	}
	
	public BufferedImage[][] getObjectImg(){
		BufferedImage[][] imgs = new BufferedImage[5][8];
		imgs[BLUE_POTION] = potionImgs[BLUE_POTION];
		imgs[RED_POTION] = potionImgs[RED_POTION];
		imgs[2] = cannonImgs;
		imgs[3] = potionImgs[TREASURE];
		imgs[4] = potionImgs[SKULL];
		
		return imgs;
	}
	
}
