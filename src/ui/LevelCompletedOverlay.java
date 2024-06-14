package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.UI.URMButtons.*;

public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgWidth, bgHeight;
	
	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int)(330 * Game.SCALE);
		int nextX = (int)(445 * Game.SCALE);
		int y = (int)(195 * Game.SCALE);
		next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	private void initImg() {
		img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_COMPLETED);
		bgWidth = (int)(img.getWidth() * Game.SCALE);
		bgHeight = (int)(img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 4 + 190;
		bgY = (int)(75 * Game.SCALE);
	}
	
	public void update() {
		next.update();
		menu.update();
	}
	
	public void draw(Graphics g) {
		g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
		next.draw(g);
		menu.draw(g);
	}
	
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);
		if(isIn(menu,e))
			menu.setMouseOver(true);
		else if(isIn(next,e))
			next.setMouseOver(true);
	}
	
	public void mouseReleased(MouseEvent e) {
		if(isIn(menu,e)) {
			if(menu.isMousePressed()) {
				playing.resetAll();
				GameState.state = GameState.MENU;
				playing.getGame().getAudioPlayer().setLevelSong(AudioPlayer.MENU);
			}
		}
		else if(isIn(next,e)) {
			if(next.isMousePressed()) {
				playing.loadNextLevel();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
			}
		}
		menu.resetBools();
		next.resetBools();
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(menu,e))
			menu.setMousePressed(true);
		else if(isIn(next,e))
			next.setMousePressed(true);
	}
	
}
