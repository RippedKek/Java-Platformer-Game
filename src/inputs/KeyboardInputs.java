package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameStates.GameState;
import main.GamePanel;

import static utils.Constants.Directions.*;

public class KeyboardInputs implements KeyListener{
	
	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().keyPressed(e);
			break;
		case ACHIEVEMENTS:
			gamePanel.getGame().getGameAchievements().keyPressed(e);
			break;
		case ENCYCLOPEDIA:
			gamePanel.getGame().getEncyclopedia().keyPressed(e);
			break;
		case PLAYER_INFO:
			gamePanel.getGame().getPlayerInfo().keyPressed(e);
			break;
		case ENEMY_INFO:
			gamePanel.getGame().getEnemyInfo().keyPressed(e);
			break;
		case OBJECT_INFO:
			gamePanel.getGame().getObjInfo().keyPressed(e);
			break;
		case STARTUP:
			gamePanel.getGame().getStartup().keyPressed(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
		default:
			break;
		}
	}

}
