package levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Alpha;
import entities.HeavyNinja;
import entities.Necromancer;
import entities.Ninja;
import entities.NinjaCaptain;
import entities.Shark;
import entities.WindNinja;
import main.Game;
import objects.Cannon;
import objects.Chest;
import objects.GameContainer;
import objects.Grass;
import objects.Potion;
import objects.Spike;
import objects.TutorialBanners;
import utils.HelpMethods;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.EnemyConstants.SHARK;

public class Levels {
	
	private BufferedImage img;
	private int[][] levelData;
	
	private ArrayList<HeavyNinja> heavyNinjas = new ArrayList<>();
	private ArrayList<Ninja> ninjas = new ArrayList<>();
	private ArrayList<WindNinja> windNinjas = new ArrayList<>();
	private ArrayList<Shark> sharks = new ArrayList<>();
	private ArrayList<Necromancer> necros = new ArrayList<>();
	private ArrayList<NinjaCaptain> captain = new ArrayList<>();
	private ArrayList<Alpha> alpha = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Chest> chests = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<Grass> grass = new ArrayList<>();
	private ArrayList<TutorialBanners> banners = new ArrayList<>();
	
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	
	private Point playerSpawn;
	
	public Levels(BufferedImage img) {
		this.img = img;
		levelData = new int[img.getHeight()][img.getWidth()];
		loadLevel();
		calcLvlOffset();
	}
	
	private void loadLevel() {

		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
	}

	private void loadLevelData(int redValue, int x, int y) {
		if (redValue >= 48)
			levelData[y][x] = 0;
		else
			levelData[y][x] = redValue;
		switch (redValue) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> 
		grass.add(new Grass((int) (x * Game.TILES_SIZE), (int) (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
		}
	}

	private int getRndGrassType(int xPos) {
		return xPos % 2;
	}

	private void loadEntities(int greenValue, int x, int y) {
		switch (greenValue) {
		case HEAVY_NINJA -> heavyNinjas.add(new HeavyNinja(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case WIND_NINJA -> windNinjas.add(new WindNinja(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case NINJA -> ninjas.add(new Ninja(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case ALPHA -> alpha.add(new Alpha(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case SHARK -> sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case NECROMANCER -> necros.add(new Necromancer(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case NINJA_CAPTAIN -> captain.add(new NinjaCaptain(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
		}
	}

	private void loadObjects(int blueValue, int x, int y) {
		switch (blueValue) {
		case RED_POTION, BLUE_POTION, SKULL -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case CONTROLS, ATTACKS, ULTIMATE -> banners.add(new TutorialBanners(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
		case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case CHEST -> chests.add(new Chest(x * Game.TILES_SIZE + 7, y * Game.TILES_SIZE + 35, blueValue));
		}
	}

	private void calcLvlOffset() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	public int getSpriteIndex(int x, int y) {
		return levelData[y][x];
	}
	
	public int[][] getLevelData(){
		return levelData;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	
	public ArrayList<HeavyNinja> getHeavyNinjas(){
		return heavyNinjas;
	}
	
	public ArrayList<NinjaCaptain> getNinjaCaptain(){
		return captain;
	}
	
	public ArrayList<Alpha> getAlpha(){
		return alpha;
	}
	
	public ArrayList<Ninja> getNinjas(){
		return ninjas;
	}
	
	public ArrayList<WindNinja> getWindNinjas(){
		return windNinjas;
	}
	
	public ArrayList<Necromancer> getNecromancers(){
		return necros;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Potion> getPotions(){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}
	
	public ArrayList<Chest> getChests(){
		return chests;
	}
	
	public ArrayList<Spike> getSpikes(){
		return spikes;
	}
	
	public ArrayList<Cannon> getCannons(){
		return cannons; 
	}
	
	public ArrayList<Grass> getGrass() {
		return grass;
	}
	
	public ArrayList<Shark> getShark(){
		return sharks;
	}
	
	public ArrayList<TutorialBanners> getBanners(){
		return banners; 
	}
	
}