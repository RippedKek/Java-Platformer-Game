package gameStates;

import static utils.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

import static utils.Constants.Achievements.*;

public class GameAchievements extends State implements Statemethods{

	private BufferedImage backgroundImg, achievementBackgroundImg, achievementLocked, achievementDesc;
	private BufferedImage[] achievementDescImg = new BufferedImage[3];
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuB;
	private boolean help1 = false, help2 = false, help3 = false;
	private int posX, posY;
	private Rectangle bound1, bound2, bound3;
	
	public GameAchievements(Game game) {
		super(game);
		loadImgs();
		loadButton();
		loadBounds();
	}
	
	private void loadBounds() {
		bound1 = new Rectangle(576, 68, 448, 222);
		bound2 = new Rectangle(576, 292, 448, 222);
		bound3 = new Rectangle(576, 516, 448, 222);
	}

	private void loadButton() {
		int menuX = (int)(50 * Game.SCALE - 30);
		int menuY = (int)(325 * Game.SCALE + 20);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}
	
	private void loadImgs() {
		backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		achievementBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.ACHIEVEMENT_DISPLAY);
		achievementLocked = LoadSave.getSpriteAtlas(LoadSave.ACHIEVEMENT_LOCKED);
		achievementDesc = LoadSave.getSpriteAtlas(LoadSave.ACHIEVEMENT_DESC);
		bgW = (int)(achievementBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int)(achievementBackgroundImg.getHeight() * Game.SCALE);
		bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 1.75);
		bgY = (int)(33 * Game.SCALE);
		for(int i=0;i<3;i++)
			achievementDescImg[i] = achievementDesc.getSubimage(i * 182, 0, 182, 60);
	}

	@Override
	public void update() {
		menuB.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(achievementBackgroundImg, bgX, bgY, bgW, bgH, null);
		menuB.draw(g);
		if(!achievementTracker[COLLECTED_CHEST])
			g.drawImage(achievementLocked, bgX + 48, bgY + 50, 130, 130, null);
		if(!achievementTracker[KILLED_ALPHA])
			g.drawImage(achievementLocked, bgX + 48, bgY + 276, 130, 130, null);
		if(!achievementTracker[NODMG_LAST_LEVEL])
			g.drawImage(achievementLocked, bgX + 48, bgY + 497, 130, 130, null);
		if(help1)
			g.drawImage(achievementDescImg[0], posX, posY, null);
		if(help2)
			g.drawImage(achievementDescImg[1], posX, posY, null);
		if(help3)
			g.drawImage(achievementDescImg[2], posX, posY, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
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
				GameState.state = GameState.MENU;
		}
		menuB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		if(isIn(e, menuB)) 
			menuB.setMouseOver(true);
		if(isInAchievement(e, bound1))
			help1 = true;
		if(isInAchievement(e, bound2))
			help2 = true;
		if(isInAchievement(e, bound3))
			help3 = true;
		if(!isInAchievement(e, bound1))
			help1 = false;
		if(!isInAchievement(e, bound2))
			help2 = false;
		if(!isInAchievement(e, bound3))
			help3 = false;
		posX = e.getX();
		posY = e.getY();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			GameState.state = GameState.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	private boolean isInAchievement(MouseEvent e, Rectangle bound) {
		return bound.getBounds().contains(e.getX(),e.getY());
	}

}
