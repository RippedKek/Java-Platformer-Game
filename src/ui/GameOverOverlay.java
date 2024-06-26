package ui;

import static utils.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.LoadSave;

public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	
	private UrmButton menu, play;
	
	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}
	
	private void createButtons() {
		int menuX = (int)(320 * Game.SCALE);
		int playX = (int)(425 * Game.SCALE);
		int y = (int)(195 * Game.SCALE);
		play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	private void createImg() {
		img = LoadSave.getSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int)(img.getWidth() * Game.SCALE);
		imgH = (int)(img.getHeight() * Game.SCALE);
		imgX = (int)(Game.GAME_WIDTH / 2 - imgW / 1.75);
		imgY = (int)(100 * Game.SCALE);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0,0,0,200)); //3rd argument transparency
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		g.drawImage(img, imgX, imgY, imgW, imgH, null);
		
		menu.draw(g);
		play.draw(g);
	}
	
	public void update() {
		menu.update();
		play.update();
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			GameState.state = GameState.MENU;
		}
	}
	
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	public void mouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);
		if(isIn(menu,e))
			menu.setMouseOver(true);
		else if(isIn(play,e))
			play.setMouseOver(true);
	}
	
	public void mouseReleased(MouseEvent e) {
		if(isIn(menu,e)) {
			if(menu.isMousePressed()) {
				playing.resetAll();
				GameState.state = GameState.MENU;
			}
		}
		else if(isIn(play,e)) {
			if(play.isMousePressed()) 
				playing.resetAll();
		}
		menu.resetBools();
		play.resetBools();
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(menu,e))
			menu.setMousePressed(true);
		else if(isIn(play,e))
			play.setMousePressed(true);
	}
	
}
