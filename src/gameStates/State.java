package gameStates;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.EncyclopediaButton;
import ui.MenuButton;

public class State {

	protected Game game;
	
	public State(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
	public boolean isIn(MouseEvent e, EncyclopediaButton eb) {
		return eb.getBounds().contains(e.getX(), e.getY());
	}
	
	public void setGameState(GameState state) {
		switch(state) {
		case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
		}
		GameState.state = state;
	}
	
}
