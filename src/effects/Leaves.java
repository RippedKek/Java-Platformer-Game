package effects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import utils.LoadSave;

import static utils.Constants.ANI_SPEED;

public class Leaves {

	private Point2D.Float[] leaves;
	private Random rand;
	private float leafSpeed = 0.75f, xLeafSpeed = 0.5f;
	private BufferedImage[] leaveParticle = new BufferedImage[5];
	private int tick = 0, index = 0;
	
	public Leaves() {
		rand = new Random();
		leaves = new Point2D.Float[100];
		BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.LEAVES);
		for(int i=0;i<5;i++)
			leaveParticle[i] = temp.getSubimage(i * 16, 0, 16, 16);
		initLeaves();
	}

	private void initLeaves() {
		for (int i = 0; i < leaves.length; i++)
			leaves[i] = getRndPos();
	}

	private Point2D.Float getRndPos() {
		return new Point2D.Float((int) getNewX(0), rand.nextInt(Game.GAME_HEIGHT));
	}
	
	private float getNewX(int xLvlOffset) {
		float value = (-Game.GAME_WIDTH) + rand.nextInt((int) (Game.GAME_WIDTH * 3f)) + xLvlOffset;
		return value;
	}
	
	public void update() {
		updateAnimation();
		for (Point2D.Float l : leaves) {
			l.y += leafSpeed;
			l.x -= xLeafSpeed;
			if (l.y >= Game.GAME_HEIGHT) {
				l.y = -20;
				l.x = getNewX(0);
			}
		}
	}
	
	private void updateAnimation() {
		tick++;
		if(tick >= ANI_SPEED) {
			index++;
			tick = 0;
			if(index >= 5)
				index = 0;
		}
	}

	public void draw(Graphics g) {
		for (Point2D.Float l : leaves)
			g.drawImage(leaveParticle[index], (int) l.getX(), (int) l.getY(), 32, 32, null);
	}
	
}
