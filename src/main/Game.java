package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gameStates.Encyclopedia;
import gameStates.EnemyInfo;
import gameStates.GameAchievements;
import gameStates.GameOptions;
import gameStates.GameState;
import gameStates.Menu;
import gameStates.ObjectInfo;
import gameStates.PlayerInfo;
import gameStates.Playing;
import gameStates.Startup;
import ui.AudioOptions;
import utils.LoadSave;

public class Game implements Runnable{
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private Playing playing;
	private Menu menu;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;
	private GameOptions gameOptions;
	private GameAchievements gameAchievements;
	private Encyclopedia encyclopedia;
	private PlayerInfo playerInfo;
	private EnemyInfo enemyInfo;
	private ObjectInfo objectInfo;
	private Startup startup;
	
	private final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2.0f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		//Enables gamePanel to be focusable 
		gamePanel.setFocusable(true);
		//Gives gamePanel the input focus
		gamePanel.requestFocus();
		startGameLoop();
	}
	
	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
		gameAchievements = new GameAchievements(this);
		encyclopedia = new Encyclopedia(this);
		playerInfo = new PlayerInfo(this);
		enemyInfo = new EnemyInfo(this);
		objectInfo = new ObjectInfo(this);
		startup = new Startup(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update() {
		switch(GameState.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			gameOptions.update();
			break;
		case ACHIEVEMENTS:
			gameAchievements.update();
			break;
		case ENCYCLOPEDIA:
			encyclopedia.update();
			break;
		case PLAYER_INFO:
			playerInfo.update();
			break;
		case ENEMY_INFO:
			enemyInfo.update();
			break;
		case OBJECT_INFO:
			objectInfo.update();
			break;
		case STARTUP:
			startup.update();
			break;
		default:
			break;
		
		}
	}
	
	public void render(Graphics g) {
		switch(GameState.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameOptions.draw(g);
			break;
		case ACHIEVEMENTS:
			gameAchievements.draw(g);
			break;
		case ENCYCLOPEDIA:
			encyclopedia.draw(g);
			break;
		case PLAYER_INFO:
			playerInfo.draw(g);
			break;
		case ENEMY_INFO:
			enemyInfo.draw(g);
			break;
		case OBJECT_INFO:
			objectInfo.draw(g);
			break;
		case STARTUP:
			startup.draw(g);
			break;
		case QUIT:
		default:
			System.exit(0);
			break;
		}
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long previousTime = System.nanoTime();
		int frames = 0;
		int updates = 0;
		double deltaU = 0;
		double deltaF = 0;
		long lastCheck = System.currentTimeMillis();
		
		while(true) {
			long currentTime = System.nanoTime();
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			if(deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void windowFocusLost() {
		if(GameState.state == GameState.PLAYING) {
			playing.getPlayer().resetDirBooleans();
		}
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public GameOptions getGameOptions() {
		return gameOptions;
	}
	
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
	public GameAchievements getGameAchievements() {
		return gameAchievements;
	}
	
	public Encyclopedia getEncyclopedia() {
		return encyclopedia;
	}
	
	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}
	
	public EnemyInfo getEnemyInfo() {
		return enemyInfo;
	}
	
	public ObjectInfo getObjInfo() {
		return objectInfo;
	}
	
	public Startup getStartup() {
		return startup;
	}
	
}