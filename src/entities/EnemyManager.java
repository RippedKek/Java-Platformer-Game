package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import audio.AudioPlayer;
import gameStates.Playing;
import levels.Levels;
import objects.Fireball;
import objects.ObjectManager;
import utils.LoadSave;
import objects.Explosion;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.playerConstants.PLAYER_DAMAGE;
import static utils.Constants.EnemyConstants.DEAD;
import static utils.Constants.EnemyConstants.HIT;
import static utils.Constants.EnemyConstants.SHARK_DRAWOFFSET_X;
import static utils.Constants.EnemyConstants.SHARK_DRAWOFFSET_Y;
import static utils.Constants.EnemyConstants.SHARK_HEIGHT;
import static utils.Constants.EnemyConstants.SHARK_HEIGHT_DEFAULT;
import static utils.Constants.EnemyConstants.SHARK_WIDTH;
import static utils.Constants.EnemyConstants.SHARK_WIDTH_DEFAULT;
import static utils.Constants.playerConstants.DAMAGE_MODIFIER;

import static objects.ObjectManager.fireballs;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] heavyNinjaArr;
	private BufferedImage[][] alphaImg;
	private BufferedImage[][] ninjaArr;
	private BufferedImage[][] windNinjaArr;
	private BufferedImage[][] sharkArr;
	private BufferedImage[][] necroArr;
	private BufferedImage[][] captainArr;
	private BufferedImage[] lightningArr;
	private ArrayList<HeavyNinja> heavyNinjas = new ArrayList<>();
	private ArrayList<Ninja> ninjas = new ArrayList<>();
	private ArrayList<WindNinja> windNinjas = new ArrayList<>();
	private ArrayList<Alpha> alpha = new ArrayList<>();
	private ArrayList<Shark> sharks = new ArrayList<>();
	private ArrayList<Necromancer> necros = new ArrayList<>();
	private ArrayList<NinjaCaptain> captain = new ArrayList<>();
	private ArrayList<Explosion> explosions = new ArrayList<>();
	
	private boolean firstLightning = true;
	private int lightningIndex = 0;
	private int lightningTick = 0;
	private int lightningAniSpeed = 60;
	
	private int healthBarWidth = 25;
	private int healthHeight = 3;
	
	private int alphaSoundLimit = 1500;
	private int alphaSoundCount = 1500;
	private int captainSoundLimit = 1500;
	private int captainSoundCount = 1500;
	private boolean spawnNinjas = false;
	private Random rand;
	
	private boolean playLightningSound = true;
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		rand = new Random();
		loadEnemyImgs();
	}

	public void loadEnemies(Levels level) {
		heavyNinjas = level.getHeavyNinjas();
		alpha = level.getAlpha();
		ninjas = level.getNinjas();
		windNinjas = level.getWindNinjas();
		sharks = level.getShark();
		necros = level.getNecromancers();
		captain = level.getNinjaCaptain();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for(HeavyNinja c : heavyNinjas)
			if(c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
				if(fireballs.size() != 0) {
					for(Fireball f : fireballs) {
						if(c.getCurrentHealth() > 0 && c.getEnemyState() != HIT)
							if(c.getHitbox().intersects(f.getHitbox())) {
								c.hurt(200);
								explosions.add(new Explosion((int)c.getHitbox().x, (int)(c.getHitbox().y - 38)));
								playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLOSION);
								f.setActive(false);
							}
					}
				}
			}
		for(WindNinja wn : windNinjas)
			if(wn.isActive()) {
				wn.update(lvlData, player);
				isAnyActive = true;
				if(fireballs.size() != 0) {
					for(Fireball f : fireballs) {
						if(wn.getCurrentHealth() > 0 && wn.getEnemyState() != HIT)
							if(wn.getHitbox().intersects(f.getHitbox())) {
								wn.hurt(200);
								f.setActive(false);
								explosions.add(new Explosion((int)wn.getHitbox().x, (int)(wn.getHitbox().y - 38)));
								playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLOSION);
							}
					}
				}
			}
		for (Shark s : sharks)
			if (s.isActive()) {
				s.update(lvlData, playing);
				isAnyActive = true;
				if(fireballs.size() != 0) {
					for(Fireball f : fireballs) {
						if(s.getCurrentHealth() > 0 && s.getEnemyState() != HIT)
							if(s.getHitbox().intersects(f.getHitbox())) {
								s.hurt(200);
								f.setActive(false);
								explosions.add(new Explosion((int)s.getHitbox().x, (int)(s.getHitbox().y - 38)));
								playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLOSION);
							}
					}
				}
			}
		for (Necromancer n : necros)
			if (n.isActive()) {
				n.update(lvlData, player);
				isAnyActive = true;
				if(fireballs.size() != 0) {
					for(Fireball f : fireballs) {
						if(n.getCurrentHealth() > 0 && n.getEnemyState() != HIT)
							if(n.getHitbox().intersects(f.getHitbox())) {
								n.hurt(200);
								f.setActive(false);
								explosions.add(new Explosion((int)n.getHitbox().x, (int)(n.getHitbox().y - 38)));
								playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLOSION);
							}
					}
				}
			}
		for(Alpha a : alpha) {
			if(a.isActive()) {
				a.update(lvlData, player);
				isAnyActive = true;
				if(a.canSeePlayer(lvlData, player)) {
					alphaSoundCount++;
					if(alphaSoundCount >= alphaSoundLimit) {
						int effect = 8;
						effect += rand.nextInt(3);
						playing.getGame().getAudioPlayer().playEffect(effect);
						alphaSoundCount = 0;
					}
				}
				if(Alpha.Alpha_Attack) {
					playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ALPHA_HEAVY_ATTACK);
					Alpha.Alpha_Attack = false;
				}
				if(a.getHealth() <= (a.maxHealth / 2))
					spawnNinjas = true;
			}
		}
		for(NinjaCaptain nc : captain) {
			if(nc.isActive()) {
				nc.update(lvlData, player);
				isAnyActive = true;
				if(nc.canSeePlayer(lvlData, player)) {
					captainSoundCount++;
					if(captainSoundCount >= captainSoundLimit) {
						int effect = 17;
						effect += rand.nextInt(3);
						playing.getGame().getAudioPlayer().playEffect(effect);
						captainSoundCount = 0;
					}
				}
			}
		}
		if(Alpha.TotalAttackCounter >= 15 || spawnNinjas)
			for(Ninja n : ninjas)
				if(n.isActive()) {
					n.update(lvlData, player);
					isAnyActive = true;
					if(fireballs.size() != 0) {
						for(Fireball f : fireballs) {
							if(n.getCurrentHealth() > 0 && n.getEnemyState() != HIT)
								if(n.getHitbox().intersects(f.getHitbox())) {
									n.hurt(200);
									explosions.add(new Explosion((int)n.getHitbox().x, (int)(n.getHitbox().y - 38)));
									playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLOSION);
									f.setActive(false);
								}
						}
					}
				}
		for(Explosion e : explosions)
			if(e.getExplode())
				e.update();
		if(!isAnyActive)
			playing.setLevelCompleted(true);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawHeavyNinjas(g, xLvlOffset);
		drawWindNinjas(g, xLvlOffset);
		drawAlpha(g, xLvlOffset);
		drawSharks(g, xLvlOffset);
		drawNecros(g, xLvlOffset);
		drawCaptain(g, xLvlOffset);
		if(Alpha.TotalAttackCounter >= 15 || spawnNinjas) {
			if(playLightningSound) {
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LIGHTNING);
				playLightningSound = false;
			}
			drawNinja(g, xLvlOffset);
		}
		for(Explosion e : explosions)
			if(e.getExplode())
				e.draw(g, xLvlOffset);
	}
	
	private void drawCaptain(Graphics g, int xLvlOffset) {
		for(NinjaCaptain nc : captain) 
			if(nc.isActive()) {
				drawEnemyHealth(GetMaxHealth(NINJA_CAPTAIN), nc.currentHealth, (int)(nc.getHitbox().x - xLvlOffset), (int)(nc.getHitbox().y), g);
				g.drawImage(captainArr[nc.getEnemyState()][nc.getAniIndex()], 
						(int) nc.getHitbox().x - xLvlOffset - CAPTAIN_DRAWOFFSET_X + nc.flipX() + 66,
						(int) nc.getHitbox().y - CAPTAIN_DRAWOFFSET_Y + 45, 
						(int)(CAPTAIN_WIDTH_DEFAULT * 1.5) * nc.flipW(), 
						(int)(CAPTAIN_HEIGHT_DEFAULT * 1.5), 
						null);
				//nc.drawHitbox(g, xLvlOffset);
				//nc.drawAttackBox(g, xLvlOffset);
			}
	}

	private void drawNecros(Graphics g, int xLvlOffset) {
		for(Necromancer n : necros) 
			if(n.isActive()) {
				drawEnemyHealth(GetMaxHealth(NECROMANCER), n.currentHealth, (int)(n.getHitbox().x - xLvlOffset), (int)(n.getHitbox().y), g);
				g.drawImage(necroArr[n.getEnemyState()][n.getAniIndex()], 
						(int) n.getHitbox().x - xLvlOffset - NECRO_DRAWOFFSET_X + n.flipX() + 60,
						(int) n.getHitbox().y - NECRO_DRAWOFFSET_Y + 50, 
						NECRO_WIDTH_DEFAULT * n.flipW(), 
						NECRO_HEIGHT_DEFAULT, 
						null);
				//n.drawHitbox(g, xLvlOffset);
				//n.drawAttackBox(g, xLvlOffset);
			}
	}

	private void drawSharks(Graphics g, int xLvlOffset) {
		for (Shark s : sharks)
			if (s.isActive()) {
				drawEnemyHealth(GetMaxHealth(SHARK), s.currentHealth, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y), g);
				g.drawImage(sharkArr[s.getEnemyState()][s.getAniIndex()], (int) s.getHitbox().x - xLvlOffset - SHARK_DRAWOFFSET_X + s.flipX() + 70,
						(int) s.getHitbox().y - SHARK_DRAWOFFSET_Y, SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
				//s.drawHitbox(g, xLvlOffset);
				//s.drawAttackBox(g, xLvlOffset);
			}
	}

	private void drawWindNinjas(Graphics g, int xLvlOffset) {
		for(WindNinja wn : windNinjas) {
			if(wn.isActive()) {
				drawEnemyHealth(GetMaxHealth(WIND_NINJA), wn.currentHealth, (int)(wn.getHitbox().x - xLvlOffset), (int)(wn.getHitbox().y), g);
				g.drawImage(windNinjaArr[wn.getEnemyState()][wn.getAniIndex()], (int)(wn.getHitbox().x - WIND_NINJA_DRAWOFFSET_X - xLvlOffset + wn.flipX()) - 68, (int)(wn.getHitbox().y - WIND_NINJA_DRAWOFFSET_Y) - 45, (int)(WIND_NINJA_WIDTH_DEFAULT * 1.5 * wn.flipW()), (int)(WIND_NINJA_HEIGHT_DEFAULT * 1.5), null);
				//wn.drawHitbox(g, xLvlOffset);
				//wn.drawAttackBox(g, xLvlOffset);
			}
		}
	}

	private void drawNinja(Graphics g, int xLvlOffset) {
		for(Ninja n : ninjas)
			if(n.isActive()) {
				if(firstLightning) {
					g.drawImage(lightningArr[lightningIndex], (int)(n.getHitbox().x - NINJA_DRAWOFFSET_X - xLvlOffset), (int)(n.getHitbox().y - NINJA_DRAWOFFSET_Y), LIGHTNING_WIDTH_DEFAULT, LIGHTNING_HEIGHT_DEFAULT, null);
					lightningTick++;
					if(lightningTick >= lightningAniSpeed) {
						lightningIndex++;
						if(lightningIndex >= 9)
							firstLightning = false;
					}
				}
				drawEnemyHealth(GetMaxHealth(NINJA), n.currentHealth, (int)(n.getHitbox().x - xLvlOffset), (int)(n.getHitbox().y), g);
				g.drawImage(ninjaArr[n.getEnemyState()][n.getAniIndex()], (int)(n.getHitbox().x - NINJA_DRAWOFFSET_X - xLvlOffset + n.flipX()), (int)(n.getHitbox().y - NINJA_DRAWOFFSET_Y), NINJA_WIDTH_DEFAULT * n.flipW(), NINJA_HEIGHT_DEFAULT, null);
				//n.drawHitbox(g, xLvlOffset);
				//n.drawAttackBox(g, xLvlOffset);
			}
	}

	private void drawAlpha(Graphics g, int xLvlOffset) {
		for(Alpha a : alpha) {
			if(a.isActive()) {
				g.drawImage(alphaImg[a.getEnemyState()][a.getAniIndex()], (int)(a.getHitbox().x - ALPHA_DRAWOFFSET_X - xLvlOffset + a.flipX()), (int)(a.getHitbox().y - ALPHA_DRAWOFFSET_Y), (int)(ALPHA_WIDTH_DEFAULT) * a.flipW(), (int)(ALPHA_HEIGHT_DEFAULT), null);
				a.drawUI(g);
				//a.drawHitbox(g, xLvlOffset);
				//a.drawAttackBox(g, xLvlOffset);
			}
		}
	}

	private void drawHeavyNinjas(Graphics g, int xLvlOffset) {
		for(HeavyNinja c : heavyNinjas) {
			if(c.isActive()) {
				drawEnemyHealth(GetMaxHealth(HEAVY_NINJA), c.currentHealth, (int)(c.getHitbox().x - xLvlOffset), (int)(c.getHitbox().y), g);
				g.drawImage(heavyNinjaArr[c.getEnemyState()][c.getAniIndex()], (int)(c.getHitbox().x - HEAVY_NINJA_DRAWOFFSET_X - xLvlOffset + c.flipX()), (int)(c.getHitbox().y - HEAVY_NINJA_DRAWOFFSET_Y), HEAVY_NINJA_WIDTH_DEFAULT * c.flipW(), HEAVY_NINJA_HEIGHT_DEFAULT, null);
				//c.drawHitbox(g, xLvlOffset);
				//c.drawAttackBox(g, xLvlOffset);
			}
		}
	}
	
	private void drawEnemyHealth(int maxHealth, int currentHealth, int x, int y, Graphics g) {
		int healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
		g.setColor(Color.RED);
		g.fillRect(x - 5, y - 15, healthWidth, healthHeight);
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for(HeavyNinja c : heavyNinjas) {
			if(c.isActive()) {
				if(c.getCurrentHealth() > 0 && c.getEnemyState() != HIT)
					if(attackBox.intersects(c.getHitbox())) {
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_BASIC_ATTACK);
						c.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						if(c.getCurrentHealth() <= 0)
							playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HEAVY_NINJA_DEATH);
						return;
					}
			}
		}
		
		for(Necromancer n : necros) {
			if(n.isActive()) {
				if(n.getCurrentHealth() > 0 && n.getEnemyState() != HIT)
					if(attackBox.intersects(n.getHitbox())) {
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_BASIC_ATTACK);
						n.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						if(n.getCurrentHealth() <= 0)
							playing.getGame().getAudioPlayer().playEffect(AudioPlayer.NECRO_DEATH);
						return;
					}
			}
		}
		
		for(NinjaCaptain nc : captain) {
			if(nc.isActive()) {
				if(nc.getCurrentHealth() > 0 && nc.getEnemyState() != HIT)
					if(attackBox.intersects(nc.getHitbox())) {
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_BASIC_ATTACK);
						nc.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						if(nc.getCurrentHealth() <= 0)
							playing.getGame().getAudioPlayer().playEffect(AudioPlayer.CAPTAIN_DEATH);
						return;
					}
			}
		}
		
		for(Alpha a : alpha) {
			if(a.isActive()) {
				if(a.getCurrentHealth() > 0 && a.getEnemyState() != HIT)
					if(attackBox.intersects(a.getHitbox())) {
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_BASIC_ATTACK);
						a.hurt(PLAYER_DAMAGE - (PLAYER_DAMAGE / 10) + DAMAGE_MODIFIER - (DAMAGE_MODIFIER / 10));
						return;
					}
			}
		}
		
		for(WindNinja wn : windNinjas) {
			if(wn.isActive()) {
				if(wn.getCurrentHealth() > 0 && wn.getEnemyState() != HIT)
					if(attackBox.intersects(wn.getHitbox())) {
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_BASIC_ATTACK);
						wn.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						if(wn.getCurrentHealth() <= 0)
							playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WIND_NINJA_DEATH);
						return;
					}
			}
		}
		
		for(Ninja n : ninjas) {
			if(n.isActive()) {
				if(n.getCurrentHealth() > 0 && n.getEnemyState() != HIT)
					if(attackBox.intersects(n.getHitbox())) {
						n.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						if(n.getCurrentHealth() <= 0)
							playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HEAVY_NINJA_DEATH);
						return;
					}
			}
		}
		
		for (Shark s : sharks)
			if (s.isActive()) {
				if (s.getEnemyState() != DEAD && s.getEnemyState() != HIT) {
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(PLAYER_DAMAGE + DAMAGE_MODIFIER);
						return;
					}
				}			
			}
	}

	private void loadEnemyImgs() {
		BufferedImage idle = LoadSave.getSpriteAtlas(LoadSave.ENEMY_IDLE);
		BufferedImage running = LoadSave.getSpriteAtlas(LoadSave.ENEMY_RUN);
		BufferedImage attack = LoadSave.getSpriteAtlas(LoadSave.ENEMY_ATTACK);
		BufferedImage hit = LoadSave.getSpriteAtlas(LoadSave.ENEMY_HIT);
		BufferedImage dead = LoadSave.getSpriteAtlas(LoadSave.ENEMY_DEAD);
		BufferedImage heavy_attack = LoadSave.getSpriteAtlas(LoadSave.ALPHA_HEAVY_ATTACK);
		BufferedImage fall = LoadSave.getSpriteAtlas(LoadSave.NINJA_FALL);
		BufferedImage lightning = LoadSave.getSpriteAtlas(LoadSave.LIGHTNING);
		heavyNinjaArr = new BufferedImage[5][8];
		
		for(int i=0;i<2;i++)
			heavyNinjaArr[0][i] = idle.getSubimage(i * HEAVY_NINJA_WIDTH_DEFAULT, 0, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=2;i<4;i++)
			heavyNinjaArr[0][i] = idle.getSubimage((i - 2) * HEAVY_NINJA_WIDTH_DEFAULT, 64, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=4;i<6;i++)
			heavyNinjaArr[0][i] = idle.getSubimage((i - 4) * HEAVY_NINJA_WIDTH_DEFAULT, 128, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=6;i<8;i++)
			heavyNinjaArr[0][i] = idle.getSubimage((i - 6) * HEAVY_NINJA_WIDTH_DEFAULT, 192, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		
		for(int i=0;i<2;i++)
			heavyNinjaArr[1][i] = running.getSubimage(i * HEAVY_NINJA_WIDTH_DEFAULT, 0, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=2;i<4;i++)
			heavyNinjaArr[1][i] = running.getSubimage((i - 2) * HEAVY_NINJA_WIDTH_DEFAULT, 64, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=4;i<6;i++)
			heavyNinjaArr[1][i] = running.getSubimage((i - 4) * HEAVY_NINJA_WIDTH_DEFAULT, 128, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=6;i<8;i++)
			heavyNinjaArr[1][i] = running.getSubimage((i - 6) * HEAVY_NINJA_WIDTH_DEFAULT, 192, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		
		for(int i=0;i<8;i++)
			heavyNinjaArr[2][i] = attack.getSubimage(i * HEAVY_NINJA_WIDTH_DEFAULT, 0, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		
		for(int i=0;i<2;i++)
			heavyNinjaArr[3][i] = hit.getSubimage(i * HEAVY_NINJA_WIDTH_DEFAULT, 0, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=2;i<3;i++)
			heavyNinjaArr[3][i] = hit.getSubimage((i - 2) * HEAVY_NINJA_WIDTH_DEFAULT, 64, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		
		for(int i=0;i<2;i++)
			heavyNinjaArr[4][i] = dead.getSubimage(i * HEAVY_NINJA_WIDTH_DEFAULT, 0, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		for(int i=2;i<4;i++)
			heavyNinjaArr[4][i] = dead.getSubimage((i - 2) * HEAVY_NINJA_WIDTH_DEFAULT, 64, HEAVY_NINJA_WIDTH_DEFAULT, HEAVY_NINJA_HEIGHT_DEFAULT);
		
		//ALPHA
		idle = LoadSave.getSpriteAtlas(LoadSave.ALPHA_IDLE);
		running = LoadSave.getSpriteAtlas(LoadSave.ALPHA_RUN);
		attack = LoadSave.getSpriteAtlas(LoadSave.ALPHA_ATTACK);
		hit = LoadSave.getSpriteAtlas(LoadSave.ALPHA_HIT);
		dead = LoadSave.getSpriteAtlas(LoadSave.ALPHA_DEAD);
		alphaImg = new BufferedImage[6][8];
		
		for(int i=0;i<GetSpriteAmount(ALPHA, IDLE);i++) 
			alphaImg[0][i] = idle.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(ALPHA, RUNNING);i++) 
			alphaImg[1][i] = running.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(ALPHA, ATTACK);i++) 
			alphaImg[2][i] = attack.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(ALPHA, HIT);i++) 
			alphaImg[3][i] = hit.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(ALPHA, DEAD);i++) 
			alphaImg[4][i] = dead.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(ALPHA, HEAVY_ATTACK);i++) 
			alphaImg[5][i] = heavy_attack.getSubimage(i * ALPHA_WIDTH_DEFAULT, 0, ALPHA_WIDTH_DEFAULT, ALPHA_HEIGHT_DEFAULT);
		
		//NINJA
		idle = LoadSave.getSpriteAtlas(LoadSave.NINJA_IDLE);
		running = LoadSave.getSpriteAtlas(LoadSave.NINJA_RUN);
		attack = LoadSave.getSpriteAtlas(LoadSave.NINJA_ATTACK);
		hit = LoadSave.getSpriteAtlas(LoadSave.NINJA_HIT);
		dead = LoadSave.getSpriteAtlas(LoadSave.NINJA_DEATH);
		ninjaArr = new BufferedImage[7][8];
		
		for(int i=0;i<GetSpriteAmount(NINJA, IDLE);i++) 
			ninjaArr[0][i] = idle.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA, RUNNING);i++) 
			ninjaArr[1][i] = running.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA, ATTACK);i++) 
			ninjaArr[2][i] = attack.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA, HIT);i++) 
			ninjaArr[3][i] = hit.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA, DEAD);i++) 
			ninjaArr[4][i] = dead.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA, FALL);i++) 
			ninjaArr[6][i] = fall.getSubimage(i * NINJA_WIDTH_DEFAULT, 0, NINJA_WIDTH_DEFAULT, NINJA_HEIGHT_DEFAULT);
		
		//WIND NINJA
		BufferedImage windNinjaImg = LoadSave.getSpriteAtlas(LoadSave.WIND_NINJA_SPRITE);
		windNinjaArr = new BufferedImage[6][19];
		//IDLE
		for(int i=0;i<GetSpriteAmount(WIND_NINJA, IDLE);i++)
			windNinjaArr[0][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 0 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		//RUNNING
		for(int i=0;i<GetSpriteAmount(WIND_NINJA, RUNNING);i++)
			windNinjaArr[1][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 1 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		//HEAVY ATTACK 
			for(int i=0;i<GetSpriteAmount(WIND_NINJA, HEAVY_ATTACK);i++)
				windNinjaArr[5][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 4 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		//ATTACK
		for(int i=0;i<GetSpriteAmount(WIND_NINJA, ATTACK);i++)
			windNinjaArr[2][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 6 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		//HIT
		for(int i=0;i<GetSpriteAmount(WIND_NINJA, HIT);i++)
			windNinjaArr[3][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 11 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		//DEAD
		for(int i=0;i<GetSpriteAmount(WIND_NINJA, DEAD);i++)
			windNinjaArr[4][i] = windNinjaImg.getSubimage(i * WIND_NINJA_WIDTH_DEFAULT, 12 * 128, WIND_NINJA_WIDTH_DEFAULT,  WIND_NINJA_HEIGHT_DEFAULT);
		
		//LIGHTNING
		lightningArr = new BufferedImage[9];
		
		for(int i=0;i<3;i++)
			lightningArr[i] = lightning.getSubimage(i * LIGHTNING_WIDTH_DEFAULT, 0, LIGHTNING_WIDTH_DEFAULT, LIGHTNING_HEIGHT_DEFAULT);
		for(int i=3;i<6;i++)
			lightningArr[i] = lightning.getSubimage((i - 3) * LIGHTNING_WIDTH_DEFAULT, 128, LIGHTNING_WIDTH_DEFAULT, LIGHTNING_HEIGHT_DEFAULT);
		for(int i=6;i<9;i++)
			lightningArr[i] = lightning.getSubimage((i - 6) * LIGHTNING_WIDTH_DEFAULT, 256, LIGHTNING_WIDTH_DEFAULT, LIGHTNING_HEIGHT_DEFAULT);
		
		sharkArr = getImgArr(LoadSave.getSpriteAtlas(LoadSave.SHARK_SPRITE), 8, 5, SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);
		
		//NECROMANCER
		BufferedImage necroTempImg = LoadSave.getSpriteAtlas(LoadSave.NECOMANCER_SHEET);
		necroArr = new BufferedImage[5][17];
		//IDLE
		for(int i=0;i<GetSpriteAmount(NECROMANCER, IDLE);i++)
			necroArr[0][i] = necroTempImg.getSubimage(i * NECRO_WIDTH_DEFAULT, 4 * 128, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT);
		//RUNNING
		for(int i=0;i<GetSpriteAmount(NECROMANCER, RUNNING);i++)
			necroArr[1][i] = necroTempImg.getSubimage(i * NECRO_WIDTH_DEFAULT, 1 * 128, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT);
		//ATTACK
		for(int i=0;i<GetSpriteAmount(NECROMANCER, ATTACK);i++)
			necroArr[2][i] = necroTempImg.getSubimage(i * NECRO_WIDTH_DEFAULT, 2 * 128, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT);
		//HIT
		for(int i=0;i<GetSpriteAmount(NECROMANCER, HIT);i++)
			necroArr[3][i] = necroTempImg.getSubimage(i * NECRO_WIDTH_DEFAULT, 5 * 128, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT);
		//DEAD
		for(int i=0;i<GetSpriteAmount(NECROMANCER, DEAD);i++)
			necroArr[4][i] = necroTempImg.getSubimage(i * NECRO_WIDTH_DEFAULT, 6 * 128, NECRO_WIDTH_DEFAULT, NECRO_HEIGHT_DEFAULT);
		
		//CAPTAIN
		captainArr = new BufferedImage[8][15];
		BufferedImage captainTemp = LoadSave.getSpriteAtlas(LoadSave.CAPTAIN_SHEET);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, IDLE);i++)
			captainArr[IDLE][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 0 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, RUNNING);i++)
			captainArr[RUNNING][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 1 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, ATTACK);i++)
			captainArr[ATTACK][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 5 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, HEAVY_ATTACK);i++)
			captainArr[HEAVY_ATTACK][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 7 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, HEAVY_ATTACK2);i++)
			captainArr[HEAVY_ATTACK2][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 10 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, HIT);i++)
			captainArr[HIT][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 12 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
		for(int i=0;i<GetSpriteAmount(NINJA_CAPTAIN, DEAD);i++)
			captainArr[DEAD][i] = captainTemp.getSubimage(i * CAPTAIN_WIDTH_DEFAULT, 13 * 128, CAPTAIN_WIDTH_DEFAULT, CAPTAIN_HEIGHT_DEFAULT);
	}
	
	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++)
			for (int i = 0; i < tempArr[j].length; i++)
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
		return tempArr;
	}
	
	public void resetAllEnemies() {
		for(HeavyNinja c : heavyNinjas)
			c.resetEnemy();
		for(Alpha a : alpha)
			a.resetEnemy();
		for(Ninja n : ninjas)
			n.resetEnemy();
		for(WindNinja wn : windNinjas)
			wn.resetEnemy();
		for (Shark s : sharks)
			s.resetEnemy();
		for (Necromancer n : necros)
			n.resetEnemy();
		for(NinjaCaptain nc : captain)
			nc.resetEnemy();
		explosions.clear();
		Alpha.TotalAttackCounter = 0;
		Alpha.attackCounter = 0;
		Alpha.Alpha_Attack = false;
		alphaSoundCount = alphaSoundLimit;
		NinjaCaptain.attackCounter = 0;
		NinjaCaptain.heavyAttackCounter = 0;
		captainSoundCount = captainSoundLimit;
		playLightningSound = true;
		spawnNinjas = false;
	}
	
	public BufferedImage[][] getEnemyImgs(){
		BufferedImage[][] enemies = new BufferedImage[7][17];
		enemies[HEAVY_NINJA] = heavyNinjaArr[IDLE];
		enemies[WIND_NINJA] = windNinjaArr[IDLE];
		enemies[NINJA] = ninjaArr[IDLE];
		enemies[SHARK] = sharkArr[IDLE];
		enemies[NECROMANCER] = necroArr[IDLE];
		enemies[NINJA_CAPTAIN] = captainArr[IDLE];
		enemies[ALPHA - 4] = alphaImg[IDLE];
		return enemies;
	}
	
}
