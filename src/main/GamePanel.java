package main;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utils.Constants.playerConstants.*;
import static utils.Constants.Directions.*;

import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;


public class GamePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game) {
		setPanelSize();
		mouseInputs = new MouseInputs(this);
		this.game = game;
		super.addKeyListener(new KeyboardInputs(this));
		super.addMouseListener(mouseInputs);
		super.addMouseMotionListener(mouseInputs);
	}
	
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
		//setMinimumSize(size);
		setPreferredSize(size);
		//setMaximumSize(size);
	}
	
	public Game getGame() {
		return game;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
}
