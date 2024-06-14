package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.PauseButtons.*;
import static utils.Constants.UI.URMButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

	private Playing playing;
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgWidth, bgHeight;
	private UrmButton menuB,replayB,unpauseB;
	
	private AudioOptions audioOptions;
	
	
	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		audioOptions = playing.getGame().getAudioOptions();
		createUrmButtons();
		
	}
	
	

	private void createUrmButtons() {
		int menuX = (int)(297 * Game.SCALE) - 2;
		int replayX = (int)(371 * Game.SCALE) - 2;
		int unpauseX = (int)(446 * Game.SCALE) - 2;
		int bY = 665;
		
		menuB = new UrmButton(menuX,bY,URM_SIZE, URM_SIZE, 2);
		replayB = new UrmButton(replayX,bY,URM_SIZE, URM_SIZE, 1);
		unpauseB = new UrmButton(unpauseX,bY,URM_SIZE, URM_SIZE, 0);
	}

	

	private void loadBackground() {
		backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		bgX = (int) (Game.GAME_WIDTH / 2 - bgWidth / 1.75);
		bgY = (int)(30 * Game.SCALE);
 	}

	public void update() {
		menuB.update();
		replayB.update();
		unpauseB.update();
		audioOptions.update();
	}
	
	public void draw(Graphics g) {
		//Background
		g.drawImage(backgroundImg,bgX,bgY,bgWidth,bgHeight,null);
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);
		audioOptions.draw(g);		
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(e,menuB))
			menuB.setMousePressed(true);
		else if(isIn(e,replayB))
			replayB.setMousePressed(true);
		else if(isIn(e,unpauseB))
			unpauseB.setMousePressed(true);
		else
			audioOptions.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if(isIn(e,menuB)) {
			if(menuB.isMousePressed()) {
				GameState.state = GameState.MENU;
				playing.unpauseGame();
			}
		}
		else if(isIn(e,replayB)) {
			if(replayB.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}	
		}
		else if(isIn(e,unpauseB)) {
			if(unpauseB.isMousePressed()) 
				playing.unpauseGame();
		}
		else
			audioOptions.mouseReleased(e);
		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		
		if(isIn(e,menuB))
			menuB.setMouseOver(true);
		else if(isIn(e,	replayB))
			replayB.setMouseOver(true);
		else if(isIn(e,unpauseB))
			unpauseB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
