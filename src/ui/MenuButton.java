package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.Buttons.*;

import gameStates.GameState;
import utils.LoadSave;

public class MenuButton {
	
	private int xPos, yPos, rowIndex, index;
	private boolean menuFlag;
	private int xOffsetCenter = (int) (B_WIDTH / 1.5) - 10;
	private GameState state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	
	public MenuButton(int xPos, int yPos, int rowIndex, boolean menuFlag, GameState state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.menuFlag = menuFlag;
		this.state = state;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	private void loadImgs() {
		imgs = new BufferedImage[3];
		if(menuFlag) {
			BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MENU_BUTTONS);
			for(int i=0;i<3;i++) 
				imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
		}
		else {
			BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENCYCLOPEDIA_BUTTON);
			for(int i=0;i<3;i++) 
				imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
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
