package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import gameStates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener,MouseMotionListener, MouseWheelListener{
	
	private GamePanel gamePanel;
	
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch(GameState.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseDragged(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseDragged(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseMoved(e);
			break;
		case ACHIEVEMENTS:
			gamePanel.getGame().getGameAchievements().mouseMoved(e);
			break;
		case ENCYCLOPEDIA:
			gamePanel.getGame().getEncyclopedia().mouseMoved(e);
			break;
		case PLAYER_INFO:
			gamePanel.getGame().getPlayerInfo().mouseMoved(e);
			break;
		case ENEMY_INFO:
			gamePanel.getGame().getEnemyInfo().mouseMoved(e);
			break;
		case OBJECT_INFO:
			gamePanel.getGame().getObjInfo().mouseMoved(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(GameState.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseClicked(e);
			break;
		default:
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mousePressed(e);
			break;
		case ACHIEVEMENTS:
			gamePanel.getGame().getGameAchievements().mousePressed(e);
			break;
		case ENCYCLOPEDIA:
			gamePanel.getGame().getEncyclopedia().mousePressed(e);
			break;
		case PLAYER_INFO:
			gamePanel.getGame().getPlayerInfo().mousePressed(e);
			break;
		case ENEMY_INFO:
			gamePanel.getGame().getEnemyInfo().mousePressed(e);
			break;
		case OBJECT_INFO:
			gamePanel.getGame().getObjInfo().mousePressed(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseReleased(e);
			break;
		case ACHIEVEMENTS:
			gamePanel.getGame().getGameAchievements().mouseReleased(e);
			break;
		case ENCYCLOPEDIA:
			gamePanel.getGame().getEncyclopedia().mouseReleased(e);
			break;
		case PLAYER_INFO:
			gamePanel.getGame().getPlayerInfo().mouseReleased(e);
			break;
		case ENEMY_INFO:
			gamePanel.getGame().getEnemyInfo().mouseReleased(e);
			break;
		case OBJECT_INFO:
			gamePanel.getGame().getObjInfo().mouseReleased(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

}
