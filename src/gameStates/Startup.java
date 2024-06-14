package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import effects.Leaves;
import main.Game;
import utils.LoadSave;

public class Startup extends State implements Statemethods{

	private BufferedImage startup, title;
	private long startupTimer, titleTimer;
	private int transparency = 255;
	private boolean showStartup = true, showTitle = false;
	private Leaves leaves;
	
	public Startup(Game game) {
		super(game);
		loadImgs();
		leaves = new Leaves();
		startupTimer = System.currentTimeMillis();
	}

	private void loadImgs() {
		startup = LoadSave.getSpriteAtlas(LoadSave.HAFEZI_CODING);
		title = LoadSave.getSpriteAtlas(LoadSave.MYSTIC_VEIL);
	}

	@Override
	public void update() {
		updateTransparency();
		leaves.update();
		long now = System.currentTimeMillis();
		if(now - startupTimer >= 5000) {
			showStartup = false;
			showTitle = true;
		}
	}

	private void updateTransparency() {
		if(showStartup)
			transparency--;
	}

	@Override
	public void draw(Graphics g) {
		if(showStartup) {
			g.drawImage(startup, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
			g.setColor(new Color(0, 0, 0, transparency));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
		else if(showTitle) {
			g.drawImage(title, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
			leaves.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(showTitle)
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				GameState.state = GameState.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
