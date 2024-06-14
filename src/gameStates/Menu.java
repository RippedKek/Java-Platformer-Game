package gameStates;

import static utils.Constants.Achievements.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.EncyclopediaButton;
import ui.MenuButton;
import utils.LoadSave;

public class Menu extends State implements Statemethods {
	
	private MenuButton[] buttons = new MenuButton[4];
	private EncyclopediaButton encycloB;
	private BufferedImage backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgPink = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * 1.75); //Scaling height by 1.75 for centering
		menuX = (int) (Game.GAME_WIDTH / 2 - menuWidth / 1.75);
		menuY = (int) (45 * Game.SCALE);
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, true, GameState.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, true, GameState.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(290 * Game.SCALE), 2, true, GameState.QUIT);
		buttons[3] = new MenuButton(200, 700, 3, true, GameState.ACHIEVEMENTS);
		encycloB = new EncyclopediaButton(1350, 700, GameState.ENCYCLOPEDIA);
	}

	@Override
	public void update() {
		for(MenuButton mb : buttons)
			mb.update();
		encycloB.update();
		if(utils.Constants.GAME_OVER) {
			if(game.getPlaying().getPlayer().getDmgAmount() > 0)
				game.getPlaying().setNoDmgLastLevel(false);
			else
				game.getPlaying().setNoDmgLastLevel(true);
			utils.Constants.GAME_OVER = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		long now = System.currentTimeMillis();
		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		if(noDmgLastLevel && (now - (achievementTimer[NODMG_LAST_LEVEL])) <= 6000) {
			g.drawImage(achievementBG, 1200, 700, achievementBG.getWidth(), achievementBG.getHeight(),null);
			g.drawImage(achievements[NODMG_LAST_LEVEL], 1260, 706, MEDAL_WIDTH / 5, MEDAL_HEIGHT / 5, null);
		}
		for(MenuButton mb : buttons)
			mb.draw(g);
		encycloB.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}
		if(isIn(e, encycloB)) 
			encycloB.setMousePressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGamestate();
				if(mb.getState() == GameState.PLAYING) {
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
					if(utils.Constants.GAME_OVER)
						utils.Constants.GAME_OVER = false;
				}
				break;
			}
		}
		if(isIn(e, encycloB))
			if(encycloB.isMousePressed())
				encycloB.applyGamestate();
		resetButtons();
	}

	private void resetButtons() {
		for(MenuButton mb : buttons) 
			mb.resetBools();
		encycloB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton mb : buttons) 
			mb.setMouseOver(false);
		encycloB.setMouseOver(false);
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		}
		if(isIn(e, encycloB))
			encycloB.setMouseOver(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			GameState.state = GameState.PLAYING;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	
	
}
