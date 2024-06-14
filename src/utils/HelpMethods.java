package utils;

import static utils.Constants.EnemyConstants.HEAVY_NINJA;
import static utils.Constants.EnemyConstants.ALPHA;
import static utils.Constants.EnemyConstants.NINJA;
import static utils.Constants.EnemyConstants.WIND_NINJA;
import static utils.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Alpha;
import entities.HeavyNinja;
import entities.Ninja;
import entities.WindNinja;
import main.Game;
import objects.Cannon;
import objects.Fireball;
import objects.GameContainer;
import objects.Potion;
import objects.Projectile;
import objects.Spell;
import objects.Spike;

public class HelpMethods {
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData){
		if(!IsSolid(x,y,levelData))
			if(!IsSolid(x+width,y+height,levelData))
				if(!IsSolid(x+width,y,levelData))
					if(!IsSolid(x,y+height,levelData))
						return true;
		return false;
	}
	
	private static boolean IsSolid(float x,float y,int[][] levelData) {
		int maxWidth = levelData[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth)
			return true;
		if(y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		return IsTileSolid((int)xIndex, (int)yIndex, levelData);
	}
	
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];
		
		if(value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
		if(xSpeed > 0) {
			//Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1; //-1 for preventing overlapping
		}else {
			//Left
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
		if(airSpeed > 0) {
			//Falling Touching Floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			//Jumping
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
		if(!IsSolid(hitbox.x,hitbox.y+hitbox.height+1,levelData))
			if(!IsSolid(hitbox.x+hitbox.width,hitbox.y+hitbox.height+1,levelData))
				return false;
		return true;
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}
	
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if(IsAllTilesClear(xStart, xEnd, y, lvlData))
			for(int i=0;i<xEnd - xStart;i++)
				//Checking tiles underneath the one we previously checked
				if(!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
		return true;
	}
	
	//Checks if any obstacle or object is in line of the player and enemy
	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
		int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);
		//first and second tile variable sets boundary between player and enemy tiles
		if(firstXTile > secondXTile) 
			//If first tile is bigger
			return IsAllTilesWalkable(secondXTile, firstXTile, tileY, lvlData);
		else 
			//If second tile is bigger
			return IsAllTilesWalkable(firstXTile, secondXTile, tileY, lvlData);
	}
	
	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
		int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);
		//first and second tile variable sets boundary between player and enemy tiles
		if(firstXTile > secondXTile) 
			return IsAllTilesClear(secondXTile, firstXTile, tileY, lvlData);
		else 
			return IsAllTilesClear(firstXTile, secondXTile, tileY, lvlData);
	}
	
	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for(int i=0;i<xEnd - xStart;i++) 
			if(IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}
	
	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
		//Only hitbox.x will return the top left corner of cannon ball but we want the center of the cannon ball
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}
	
	public static boolean IsProjectileHittingLevel(Spell s, int[][] lvlData) {
		//Only hitbox.x will return the top left corner of cannon ball but we want the center of the cannon ball
		return IsSolid(s.getHitbox().x + s.getHitbox().width / 2, s.getHitbox().y + s.getHitbox().height / 2, lvlData);
	}
	
	public static boolean IsProjectileHittingLevel(Fireball f, int[][] lvlData) {
		//Only hitbox.x will return the top left corner of cannon ball but we want the center of the cannon ball
		return IsSolid(f.getHitbox().x + f.getHitbox().width / 2, f.getHitbox().y + f.getHitbox().height / 2, lvlData);
	}
	
}
