package ui;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gameStates.GameState;
import main.Game;

public class AudioOptions {
	
	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;
	private Game game;

	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}
	
	private void createVolumeButton() {
		int vX = (int)(309 * Game.SCALE - 37);
		int vY = (int)(285 * Game.SCALE);
		volumeButton = new VolumeButton(vX,vY,SLIDER_WIDTH,VOLUME_HEIGHT);
	}
	
	private void createSoundButtons() {
		int soundX = (int) (450 * Game.SCALE);
		int musicY = (int) (160 * Game.SCALE - 20);
		int sfxY = (int) (206 * Game.SCALE - 20);
		musicButton = new SoundButton(soundX,musicY,SOUND_SIZE,SOUND_SIZE);
		sfxButton = new SoundButton(soundX,sfxY,SOUND_SIZE,SOUND_SIZE);
	}
	
	public void update() {
		musicButton.update();
		sfxButton.update();
		volumeButton.update();
	}
	
	public void draw(Graphics g) {
		//Sound Buttons
		musicButton.draw(g);
		sfxButton.draw(g);
		//Volume Slider
		volumeButton.draw(g);
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(e,musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e,sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e,volumeButton))
			volumeButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if(isIn(e,musicButton)) {
			if(musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}
		}
		else if(isIn(e,sfxButton)) {
			if(sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggleEffectMute();
			}
		}
		musicButton.resetBools();
		sfxButton.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		
		if(isIn(e,musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e,sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e,volumeButton))
			volumeButton.setMouseOver(true);
	}

	public void mouseDragged(MouseEvent e) {
		if(volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			float valueAfter = volumeButton.getFloatValue();
			if(valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
