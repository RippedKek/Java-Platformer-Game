package gameStates;

import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.playerConstants.*;
import static utils.Constants.ANI_SPEED;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

public class PlayerInfo extends State implements Statemethods{

	private BufferedImage[] playerImg;
	private BufferedImage bg, textContainer;
	private UrmButton menuB;
	private int menuX, menuY;
	private int textW, textH;
	private int aniTick = 0, aniIndex = 0; 
	
	public PlayerInfo(Game game) {
		super(game);
		loadImgs();
		loadButtons();
	}

	private void loadButtons() {
		menuX = (int)(50 * Game.SCALE - 30);
		menuY = (int)(325 * Game.SCALE + 20);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
		textW = (int) (textContainer.getWidth() * 1.5);
		textH = (int) (textContainer.getHeight() * 1.5);
	}

	private void loadImgs() {
		playerImg = game.getPlaying().getPlayer().getPlayerAnimation();
		bg = LoadSave.getSpriteAtlas(LoadSave.ENCYCLO_BG);
		textContainer = LoadSave.getSpriteAtlas(LoadSave.PLAYER_INFO);
	}

	@Override
	public void update() {
		updateAnimation();
		menuB.update();
	}

	private void updateAnimation() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniIndex++;
			aniTick = 0;
			if(aniIndex >= GetSpriteAmount(IDLE)) 
				aniIndex = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(bg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(textContainer, (int) (Game.GAME_WIDTH / 2 - textW / 1.75), (int) (20 * Game.SCALE), textW, textH, null);
		g.drawImage(playerImg[aniIndex], 160, -40, 170 * 5, 170 * 5, null);
		menuB.draw(g);
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
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			GameState.state = GameState.ENCYCLOPEDIA;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
