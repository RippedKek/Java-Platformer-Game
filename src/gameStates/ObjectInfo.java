package gameStates;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.playerConstants.IDLE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

public class ObjectInfo extends State implements Statemethods{

	private BufferedImage[][] objectImgs;
	private BufferedImage[] objInfo;
	private BufferedImage bg, treasureImg;
	private UrmButton menuB;
	private int menuX, menuY, textW, textH;
	private int aniTick = 0, aniIndex = 0; 
	private int objType = 0;
	private int[] arrIndex = {0, 1, 5, 8, 9};
	
	public ObjectInfo(Game game) {
		super(game);
		loadImgs();
		loadButtons();
	}
	
	private void loadButtons() {
		menuX = (int)(50 * Game.SCALE - 30);
		menuY = (int)(325 * Game.SCALE + 20);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
		textW = (int) (objInfo[0].getWidth() * 1.5);
		textH = (int) (objInfo[0].getHeight() * 1.5);
	}
	
	private void loadImgs() {
		objectImgs = game.getPlaying().getObjectManager().getObjectImg();
		treasureImg = objectImgs[3][0];
		bg = LoadSave.getSpriteAtlas(LoadSave.ENCYCLO_BG);
		BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.OBJECT_INFO);
		objInfo = new BufferedImage[5];
		for(int i = 0; i < 5; i++)
			objInfo[i] = temp.getSubimage(i * 186, 0, 186, 322);
	}

	@Override
	public void update() {
		updateAnimation();
		menuB.update();
	}
	
	private void updateAnimation() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniIndex++;
			aniTick = 0;
			if(aniIndex >= GetSpriteAmount(arrIndex[objType])) 
				aniIndex = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(bg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		for(int i=0;i<objectImgs.length;i++)
			g.drawImage(objInfo[i], 22 + i * 300, (int) (50 * Game.SCALE), textW, textH, null);
		g.drawImage(objectImgs[BLUE_POTION][aniIndex], 130, 240, 48, 64, null);
		g.drawImage(objectImgs[RED_POTION][aniIndex], 430, 240, 48, 64, null);
		g.drawImage(objectImgs[2][aniIndex], 700, 240, 120, 78, null);
		g.drawImage(treasureImg, 1040, 260, 48, 48, null);
		g.drawImage(objectImgs[4][aniIndex], 1330, 240, 72, 72, null);
		menuB.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isIn(e, menuB))
			menuB.setMousePressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isIn(e, menuB)) {
			if(menuB.isMousePressed())
				GameState.state = GameState.ENCYCLOPEDIA;
		}
		menuB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		if(isIn(e, menuB)) 
			menuB.setMouseOver(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			GameState.state = GameState.ENCYCLOPEDIA;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
