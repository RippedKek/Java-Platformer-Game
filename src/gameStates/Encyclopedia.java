package gameStates;

import static utils.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

public class Encyclopedia extends State implements Statemethods{

	private BufferedImage background, buttonHolder, encycloText; 
	private MenuButton[] buttons = new MenuButton[3];
	private UrmButton menuB;
	private int menuX, menuY, menuW, menuH;
	private int textW, textH;
	
	public Encyclopedia(Game game) {
		super(game);
		loadImgs();
		loadButtons();
	}

	private void loadImgs() {
		background = LoadSave.getSpriteAtlas(LoadSave.ENCYCLO_BG);
		buttonHolder = LoadSave.getSpriteAtlas(LoadSave.ENCYCLOPEDIA_BG);
		encycloText = LoadSave.getSpriteAtlas(LoadSave.ENCYCLOPEDIA_TEXT);
	}

	private void loadButtons() {
		menuX = (int)(50 * Game.SCALE - 30);
		menuY = (int)(325 * Game.SCALE + 20);
		menuW = (int) (buttonHolder.getWidth() * Game.SCALE);
		menuH = (int) (buttonHolder.getHeight() * 1.75);
		textW = (int) (encycloText.getWidth() * Game.SCALE);
		textH = (int) (encycloText.getHeight() * 1.75);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, false, GameState.PLAYER_INFO);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, false, GameState.ENEMY_INFO);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(290 * Game.SCALE), 2, false, GameState.OBJECT_INFO);
	}

	@Override
	public void update() {
		menuB.update();
		for(MenuButton mb : buttons)
			mb.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(encycloText, (int) (Game.GAME_WIDTH / 2 - textW / 1.75), (int) (30 * Game.SCALE), textW, textH, null);
		g.drawImage(buttonHolder, (int) (Game.GAME_WIDTH / 2 - menuW / 1.75), (int) (100 * Game.SCALE), menuW, menuH, null);
		menuB.draw(g);
		for(MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isIn(e, menuB))
			menuB.setMousePressed(true);
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isIn(e, menuB)) {
			if(menuB.isMousePressed())
				GameState.state = GameState.MENU;
		}
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		}
		menuB.resetBools();
		resetButtons();
	}
	
	private void resetButtons() {
		for(MenuButton mb : buttons) 
			mb.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		for(MenuButton mb : buttons) 
			mb.setMouseOver(false);
		if(isIn(e, menuB)) 
			menuB.setMouseOver(true);
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		}
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

}
