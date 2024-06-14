package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;

public class Explosion {

	private boolean explode;
	private int explosionTick = 0;
	private int explosionIndex = 0;
	private int expX, expY; 
	
	private BufferedImage[] explosionArr;
	
	public Explosion(int x, int y) {
		this.expX = x;
		this.expY = y;
		explode = true;
		loadExplosionImg();
	}

	private void loadExplosionImg() {
		explosionArr = new BufferedImage[18];
		for(int i=0;i<18;i++)
			explosionArr[i] = LoadSave.getSpriteAtlas(LoadSave.EXPLOSION).getSubimage(i * 48, 0, 48, 48);
	}
	
	public void update() {
		explosionTick++;
		if(explosionTick >= 25) {
			explosionIndex++;
			explosionTick = 0;
			if(explosionIndex >= 18) 
				explode = false;
		}
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		g.drawImage(explosionArr[explosionIndex], expX - xLvlOffset, expY, 96, 96, null);
	}
	
	public void setExplode(boolean explode) {
		this.explode = explode;
	}
	
	public boolean getExplode() {
		return explode;
	}
	
}
