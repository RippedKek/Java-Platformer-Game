package gameStates;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.playerConstants.GetSpriteAmount;
import static utils.Constants.playerConstants.IDLE;
import static utils.Constants.EnemyConstants.GetSpriteAmount;
import static utils.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

public class EnemyInfo extends State implements Statemethods{

	private BufferedImage[][] enemyImg;
	private BufferedImage[] enemyInfo;
	private BufferedImage bg;
	private UrmButton menuB;
	private int menuX, menuY;
	private int textW, textH;
	private int aniTick = 0, aniIndex = 0; 
	private int enemyType = 0;
	private int[] arrIndex = {0, 3, 1, 4, 5, 2, 6};
	private int[] enemyWidth = {HEAVY_NINJA_WIDTH, SHARK_WIDTH, WIND_NINJA_WIDTH, NECRO_WIDTH, CAPTAIN_WIDTH, NINJA_WIDTH, ALPHA_WIDTH};
	private int[] enemyHeight = {HEAVY_NINJA_HEIGHT, SHARK_HEIGHT, WIND_NINJA_HEIGHT, NECRO_HEIGHT, CAPTAIN_HEIGHT, NINJA_HEIGHT, ALPHA_HEIGHT};
	private int[] multiplier = {2, 3, 2, 2, 2, 2, 2};
	private int[] xOffset = {160, 300, -170, 90, -170, 10, 90};
	private int[] yOffset = {262, 350, 10, 0, 10, 6, 100};
	
	public EnemyInfo(Game game) {
		super(game);
		loadImgs();
		loadButtons();
	}
	
	private void loadButtons() {
		menuX = (int)(50 * Game.SCALE - 30);
		menuY = (int)(325 * Game.SCALE + 20);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
		textW = (int) (enemyInfo[0].getWidth() * 1.5);
		textH = (int) (enemyInfo[0].getHeight() * 1.5);
	}
	
	private void loadImgs() {
		enemyImg = game.getPlaying().getEnemyManager().getEnemyImgs();
		bg = LoadSave.getSpriteAtlas(LoadSave.ENCYCLO_BG);
		BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENEMY_INFO);
		enemyInfo = new BufferedImage[7];
		for(int i = 0; i < 7; i++)
			enemyInfo[i] = temp.getSubimage(i * 512, 0, 512, 512);
	}

	@Override
	public void update() {
		updateAnimation();
		menuB.update();
	}
	
	private void updateAnimation() {
		int enemy;
		if(enemyType == 6)
			enemy = 10;
		else
			enemy = arrIndex[enemyType];
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniIndex++;
			aniTick = 0;
			if(aniIndex >= GetSpriteAmount(enemy, IDLE)) 
				aniIndex = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(bg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(enemyInfo[enemyType], (int) (Game.GAME_WIDTH / 2 - textW / 1.75), (int) (20 * Game.SCALE), textW, textH, null);
		g.drawImage(enemyImg[arrIndex[enemyType]][aniIndex],
				160 + xOffset[enemyType],
				-40 + yOffset[enemyType], 
				enemyWidth[enemyType] * multiplier[enemyType], 
				enemyHeight[enemyType] * multiplier[enemyType],
				null);
		menuB.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isIn(e, menuB))
			menuB.setMousePressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isIn(e, menuB)) {
			if(menuB.isMousePressed())
				GameState.state = GameState.ENCYCLOPEDIA;
		}
		menuB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		if(isIn(e, menuB)) 
			menuB.setMouseOver(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			GameState.state = GameState.ENCYCLOPEDIA;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			enemyType++;
			if(enemyType > 6)
				enemyType = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			enemyType--;
			if(enemyType < 0)
				enemyType = 6;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
