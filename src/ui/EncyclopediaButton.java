package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import utils.LoadSave;

public class EncyclopediaButton {

	private int xPos, yPos, index;
	private GameState state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	
	public EncyclopediaButton(int xPos, int yPos, GameState state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.state = state;
		loadImgs();
		initBounds();
	}
	
	private void initBounds() {
		bounds = new Rectangle(xPos, yPos, 56 * 2, 56 * 2);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENCYCLO_BUTTON);
		for(int i=0;i<3;i++) 
			imgs[i] = temp.getSubimage(i * 56, 0, 56, 56);
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos, yPos, 56 * 2, 56 * 2, null);
	}
	
	public void update() {
		index = 0;
		if(mouseOver)
			index = 1;
		if(mousePressed)
			index = 2;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public void applyGamestate() {
		GameState.state = state;
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	public GameState getState() {
		return state;
	}
	
}
