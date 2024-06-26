package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import audio.AudioPlayer;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import static utils.Constants.Environment.*;
import static utils.Constants.Achievements.*;

public class Playing extends State implements Statemethods{

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;
	
	private int xLvlOffset;
	private int leftBorder = (int)(0.4 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.6 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;
	
	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private BufferedImage trophyImg;
	private int[] smallCloudsPos;
	private Random rnd = new Random();
	
	private boolean gameOver;
	private boolean lvlCompleted = false;
	private boolean playerDying = false;
	
	private int treasureCount = 0;
	private boolean allTreasureCollected = false;
	
	public Playing(Game game) {
		super(game);
		initClasses();
		loadImg();
		calcLvlOffset();
		loadStartLevel();
	}
	
	private void loadImg() {
		backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
		trophyImg = LoadSave.getSpriteAtlas(LoadSave.TROPHY_IMG);
		smallCloudsPos = new int[8];
		for(int i=0;i<smallCloudsPos.length;i++)
			smallCloudsPos[i] = (int)(90 * Game.SCALE) + rnd.nextInt((int)(100 * Game.SCALE));
		achievementBG = LoadSave.getSpriteAtlas(LoadSave.ACHIEVEMENT_BG);
		achievements = new BufferedImage[3];
		achievements[COLLECTED_CHEST] = LoadSave.getSpriteAtlas(LoadSave.COLLECTED_CHEST);
		achievements[KILLED_ALPHA] = LoadSave.getSpriteAtlas(LoadSave.KILLED_ALPHA);
		achievements[NODMG_LAST_LEVEL] = LoadSave.getSpriteAtlas(LoadSave.NODMGLVL5);
	}

	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}
	
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		objectManager = new ObjectManager(this);
		enemyManager = new EnemyManager(this);
		player = new Player(200,200,(int)(170),(int)(170),this);
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public void update() {
		if(killedAlpha) {
			achievementTracker[KILLED_ALPHA] = true;
			achievementTimer[KILLED_ALPHA] = System.currentTimeMillis();
			game.getAudioPlayer().playEffect(AudioPlayer.ACHIEVMENT_UNLOCKED);
			killedAlpha = false;
		}
		if(paused) 
			pauseOverlay.update();
		else if(lvlCompleted) {
			levelCompletedOverlay.update();
		}
		else if(gameOver) {
			gameOverOverlay.update();
		}
		else if(playerDying) 
			player.update();
		else{
			levelManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
		}		
	}

	private void checkCloseToBorder() {
		int playerX = (int)player.getHitbox().x;
		int diff = playerX - xLvlOffset;
		
		if(diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if(diff < leftBorder)
			xLvlOffset += diff - leftBorder;
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if(xLvlOffset < 0)
			xLvlOffset = 0;
	}
	
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}
	
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		objectManager.checkObjectHit(attackbox);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		levelManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		for(int i=0;i<treasureCount;i++)
			g.drawImage(trophyImg, i * 52 + 20, 730, 32, 32, null);
		drawAchievements(g);
		if(paused) {
			g.setColor(new Color(0,0,0,150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}else if(gameOver)
			gameOverOverlay.draw(g);
		else if(lvlCompleted)
			levelCompletedOverlay.draw(g);
	}

	private void drawAchievements(Graphics g) {
		long now = System.currentTimeMillis();
		if(allTreasureCollected && (now - achievementTimer[COLLECTED_CHEST]) <= 6000) {
			g.drawImage(achievementBG, 50, 700, achievementBG.getWidth(), achievementBG.getHeight(),null);
			g.drawImage(achievements[COLLECTED_CHEST], 110, 706, MEDAL_WIDTH / 5, MEDAL_HEIGHT / 5, null);
		}
		if(achievementTracker[KILLED_ALPHA] && (now - achievementTimer[KILLED_ALPHA]) <= 6000) {
			g.drawImage(achievementBG, 50, 700, achievementBG.getWidth(), achievementBG.getHeight(),null);
			g.drawImage(achievements[KILLED_ALPHA], 110, 706, MEDAL_WIDTH / 5, MEDAL_HEIGHT / 5, null);
		}
		
	}

	private void drawClouds(Graphics g) {
		for(int i=0;i<3;i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int)(230 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		for(int i=0;i<smallCloudsPos.length;i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}
	
	public void resetAll() {
		//Reset everything
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		if(utils.Constants.GAME_OVER) {
			treasureCount = 0;
			allTreasureCollected = false;
			killedAlpha = false;
			noDmgLastLevel = false;
		}
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}
	
	public void checkSpikesTouched(Player player) {
		objectManager.checkSpikesTouched(player);
	}
	
	public void pickTreasure() {
			treasureCount++;
//			System.out.println("Treasures collected: " + treasureCount);
			if(treasureCount == 6) {
				allTreasureCollected = true;
				achievementTracker[COLLECTED_CHEST] = true;
				achievementTimer[COLLECTED_CHEST] = System.currentTimeMillis();
				game.getAudioPlayer().playEffect(AudioPlayer.ACHIEVMENT_UNLOCKED);
			}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!gameOver) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				player.setAttacking(true);
			}
			else if(e.getButton() == MouseEvent.BUTTON2) {
				player.shootFireball();
			}
			else if(e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mousePressed(e);
		}else 
			gameOverOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		}else
			gameOverOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		}else
			gameOverOverlay.mouseMoved(e);
	}
	
	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_Q:
				player.undyingRage();
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameOver)
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
	}
	
	public void unpauseGame() {
		paused = false;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}
	
	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}
	
	public void setLevelCompleted(boolean lvlCompleted) {
		this.lvlCompleted = lvlCompleted;
		if(lvlCompleted)
			game.getAudioPlayer().lvlCompleted();
	}
	
	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
	
	public boolean getAllTreasureCollected() {
		return allTreasureCollected;
	}
	
	public void setNoDmgLastLevel(boolean value) {
		noDmgLastLevel = value;
		achievementTracker[NODMG_LAST_LEVEL] = noDmgLastLevel;
		achievementTimer[NODMG_LAST_LEVEL] = System.currentTimeMillis();
		game.getAudioPlayer().playEffect(AudioPlayer.ACHIEVMENT_UNLOCKED);
	}
	
}
