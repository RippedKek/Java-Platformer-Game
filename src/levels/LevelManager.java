package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import effects.Leaves;
import gameStates.GameState;
import main.Game;
import utils.LoadSave;
import static utils.Constants.GAME_OVER;

public class LevelManager {
	
	private Game game;
	private BufferedImage[] levelSprite;
	private ArrayList<Levels> levels;
	private int lvlIndex = 0;
//	private Leaves leave;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<>();
//		leave = new Leaves();
		buildAllLevels();
	}
	
	public void loadNextLevel() {
		lvlIndex++;
		if(lvlIndex >= levels.size()) {
			lvlIndex = 0;
			System.out.println("No more Levels!");
			GameState.state = GameState.MENU;
			game.getAudioPlayer().setLevelSong(AudioPlayer.MENU);
			GAME_OVER = true;
			game.getPlaying().resetAll();
			game.getPlaying().getObjectManager().resetAllObjects();
		}
		Levels newLevel = levels.get(lvlIndex);
		game.getPlaying().getPlayer().resetAll();
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
		game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	private void buildAllLevels() {
		//Get all level image data from LoadSave
		BufferedImage[] allLevels = LoadSave.GetAllLevels();
		//Append all level information to array list
		for(BufferedImage img : allLevels)
			levels.add(new Levels(img));
	}

	private void importOutsideSprites() {
		levelSprite = new BufferedImage[48];
		BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
		for(int j=0;j<4;j++) {
			for(int i=0;i<12;i++) {
				int index = j*12 + i;
				levelSprite[index] = img.getSubimage(i*32,j*32, 32, 32);
			}
		}
	}

	public void draw(Graphics g,int lvlOffset) {
		for(int j=0;j<Game.TILES_IN_HEIGHT;j++) {
			for(int i=0;i<levels.get(lvlIndex).getLevelData()[0].length;i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i,j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		}
	}
	
	public void update() {
		
	}
	
	public Levels getCurrentLevel() {
		return levels.get(lvlIndex);
	}
	
	public int getAmountOfLevels() {
		return levels.size();
	}
	
	public int getLvlIndex() {
		return lvlIndex;
	}
	
}
