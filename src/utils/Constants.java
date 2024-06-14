package utils;

import java.awt.image.BufferedImage;

import main.Game;

public class Constants {
	
	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final int ANI_SPEED = 25;
	public static boolean GAME_OVER = false;
	
	public static class Projectiles{
		public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
		public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
		public static final int CANNON_BALL_WIDTH = (int)(Game.SCALE * CANNON_BALL_DEFAULT_WIDTH);
		public static final int CANNON_BALL_HEIGHT = (int)(Game.SCALE * CANNON_BALL_DEFAULT_HEIGHT);
		public static final float SPEED = 0.5f * Game.SCALE;
		public static final float FIREBALL_SPEED = 1f * Game.SCALE;
	}
	
	public static class ObjectConstants{
		public static final int RED_POTION = 1;
		public static final int BLUE_POTION = 0;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int SPIKE = 4;
		public static final int CANNON_LEFT = 5;
		public static final int CANNON_RIGHT= 6;
		public static final int CHEST = 7;
		public static final int TREASURE = 8;
		public static final int SKULL = 9;
		
		public static final int CONTROLS = 100;
		public static final int ATTACKS = 101;
		public static final int ULTIMATE = 102;
		
		public static final int TUTORIAL_WIDTH_DEFAULT = 224;
		public static final int TUTORIAL_HEIGHT_DEFAULT = 114;
		public static final int TUTORIAL_WIDTH = 448;
		public static final int TUTORIAL_HEIGHT = 228;
		
		public static final int RED_POTION_VALUE = 30;
		public static final int BLUE_POTION_VALUE = 10;
		
		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT);
		public static final int CONTAINER_HEIGHT = (int)(Game.SCALE * CONTAINER_HEIGHT_DEFAULT);
		
		public static final int CHEST_WIDTH_DEFAULT = 48;
		public static final int CHEST_HEIGHT_DEFAULT = 36; 
		public static final int CHEST_WIDTH = (int)(Game.SCALE * CHEST_WIDTH_DEFAULT);
		public static final int CHEST_HEIGHT = (int)(Game.SCALE * CHEST_HEIGHT_DEFAULT);
		
		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int)(Game.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int)(Game.SCALE * POTION_HEIGHT_DEFAULT);
		
		public static final int TREASURE_WIDTH_DEFAULT = 16;
		public static final int TREASURE_HEIGHT_DEFAULT = 16;
		public static final int TREASURE_WIDTH = 32;
		public static final int TREASURE_HEIGHT = 32;
		
		public static final int SKULL_WIDTH_DEFAULT = 24;
		public static final int SKULL_HEIGHT_DEFAULT = 24;
		public static final int SKULL_WIDTH = 48;
		public static final int SKULL_HEIGHT = 48;
		
		public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
		public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);
		
		public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int)(CANNON_WIDTH_DEFAULT * Game.SCALE);
		public static final int CANNON_HEIGHT = (int)(CANNON_HEIGHT_DEFAULT * Game.SCALE);
		
		public static int GetSpriteAmount(int object_type) {
			switch(object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case SKULL:
				return 8;
			case BARREL, BOX:
				return 8;
			case CHEST:
				return 5;
			case CANNON_LEFT, CANNON_RIGHT:
				return 7;
			}
			return 1;
		}
	}
	
	public static class EnemyConstants{
		public static final int HEAVY_NINJA = 0;
		public static final int WIND_NINJA = 1;
		public static final int NINJA = 2;
		public static final int SHARK = 3;
		public static final int NECROMANCER = 4;
		public static final int NINJA_CAPTAIN = 5;
		public static final int ALPHA = 10;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		public static final int HEAVY_ATTACK = 5;
		public static final int FALL = 6;
		public static final int HEAVY_ATTACK2 = 7;
		
		public static final int HEAVY_NINJA_WIDTH_DEFAULT = 128;
		public static final int HEAVY_NINJA_HEIGHT_DEFAULT = 64;
		
		public static final int HEAVY_NINJA_WIDTH = (int)(HEAVY_NINJA_WIDTH_DEFAULT * Game.SCALE);
		public static final int HEAVY_NINJA_HEIGHT = (int)(HEAVY_NINJA_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int HEAVY_NINJA_DRAWOFFSET_X = 57;
		public static final int HEAVY_NINJA_DRAWOFFSET_Y = 18;
		
		public static final int NINJA_WIDTH_DEFAULT = 200;
		public static final int NINJA_HEIGHT_DEFAULT = 200;
		
		public static final int NINJA_WIDTH = (int)(NINJA_WIDTH_DEFAULT * Game.SCALE);
		public static final int NINJA_HEIGHT = (int)(NINJA_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int NINJA_DRAWOFFSET_X = 94;
		public static final int NINJA_DRAWOFFSET_Y = 72;
		
		public static final int WIND_NINJA_WIDTH_DEFAULT = 288;
		public static final int WIND_NINJA_HEIGHT_DEFAULT = 128;
		
		public static final int WIND_NINJA_WIDTH = (int)(WIND_NINJA_WIDTH_DEFAULT * Game.SCALE);
		public static final int WIND_NINJA_HEIGHT = (int)(WIND_NINJA_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int WIND_NINJA_DRAWOFFSET_X = 132;
		public static final int WIND_NINJA_DRAWOFFSET_Y = 90;
		
		public static final int SHARK_WIDTH_DEFAULT = 34;
		public static final int SHARK_HEIGHT_DEFAULT = 30;
		
		public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * Game.SCALE);
		public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
		public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);
		
		public static final int NECRO_WIDTH_DEFAULT = 160;
		public static final int NECRO_HEIGHT_DEFAULT = 128;
		
		public static final int NECRO_WIDTH = (int) (NECRO_WIDTH_DEFAULT * Game.SCALE);
		public static final int NECRO_HEIGHT = (int) (NECRO_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int NECRO_DRAWOFFSET_X = (int) (62 * Game.SCALE);
		public static final int NECRO_DRAWOFFSET_Y = (int) (63 * Game.SCALE);
		
		public static final int CAPTAIN_WIDTH_DEFAULT = 288;
		public static final int CAPTAIN_HEIGHT_DEFAULT = 128;
		
		public static final int CAPTAIN_WIDTH = (int) (CAPTAIN_WIDTH_DEFAULT * Game.SCALE);
		public static final int CAPTAIN_HEIGHT = (int) (CAPTAIN_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int CAPTAIN_DRAWOFFSET_X = (int) (130 * Game.SCALE);
		public static final int CAPTAIN_DRAWOFFSET_Y = (int) (88 * Game.SCALE);
		
		public static final int ALPHA_WIDTH_DEFAULT = 160;
		public static final int ALPHA_HEIGHT_DEFAULT = 111;
		
		public static final int ALPHA_WIDTH = (int)(ALPHA_WIDTH_DEFAULT * Game.SCALE);
		public static final int ALPHA_HEIGHT = (int)(ALPHA_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int ALPHA_DRAWOFFSET_X = 64;
		public static final int ALPHA_DRAWOFFSET_Y = 51;
		
		public static final int LIGHTNING_WIDTH_DEFAULT = 128;
		public static final int LIGHTNING_HEIGHT_DEFAULT = 128;
		
		public static final int LIGHTNING_WIDTH = (int)(LIGHTNING_WIDTH_DEFAULT * Game.SCALE);
		public static final int LIGHTNING_HEIGHT = (int)(LIGHTNING_HEIGHT_DEFAULT * Game.SCALE);
		
		public static int GetSpriteAmount(int enemyType, int enemyState) {
			switch(enemyType) {
			case HEAVY_NINJA:
				switch(enemyState) {
				case IDLE:
				case RUNNING:
				case ATTACK:
					return 8;
				case HIT:
					return 3;
				case DEAD:
					return 4;
				}
			case ALPHA:
				switch(enemyState) {
				case IDLE:
				case RUNNING:
					return 8;
				case HIT:
				case ATTACK:
				case HEAVY_ATTACK:
					return 4;
				case DEAD:
					return 6;
				}
			case NINJA:
				switch(enemyState) {
				case IDLE:
				case ATTACK:
					return 4;
				case HIT:
					return 3;
				case RUNNING:
					return 8;
				case FALL:
					return 2;
				case DEAD:
					return 7;
				}
			case WIND_NINJA:
				switch(enemyState) {
				case IDLE:
				case RUNNING:
					return 8;
				case HIT:
					return 6;
				case ATTACK:
					return 8;
				case HEAVY_ATTACK:
					return 7;
				case DEAD:
					return 19;
				}
			case SHARK:
				switch(enemyState) {
				case IDLE:
				case ATTACK:
					return 8;
				case RUNNING:
					return 6;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
			case NECROMANCER:
				switch(enemyState) {
				case IDLE:
					return 17;
				case RUNNING:
				case DEAD:
					return 8;
				case ATTACK:
					return 13;
				case HIT:
					return 5;
				}
			case NINJA_CAPTAIN:
				switch(enemyState) {
				case IDLE:
				case RUNNING:
				case ATTACK:
					return 8;
				case HEAVY_ATTACK:
					return 7;
				case HEAVY_ATTACK2:
					return 15;
				case DEAD:
					return 15;
				case HIT:
					return 6;
				}
			}
			
			return 0;
			
		}
		
		public static int GetMaxHealth(int enemyType) {
			switch(enemyType) {
			case HEAVY_NINJA:
				return 20;
			case ALPHA:
				return 350;
			case NINJA:
				return 20;
			case WIND_NINJA:
				return 20;
			case SHARK:
				return 20;
			case NECROMANCER:
				return 20;
			case NINJA_CAPTAIN:
				return 100;
			default:
				return 1;
			}
		}
		
		public static int GetEnemyDmg(int enemyType) {
			switch(enemyType) {
			case HEAVY_NINJA:
				return 15;
			case ALPHA:
				return 40;
			case NINJA:
				return 30;
			case WIND_NINJA:
				return 30;
			case SHARK:
				return 20;
			case NINJA_CAPTAIN:
				return 30;
			default:
				return 0;
			}
		}
		
	}
	
	public static class Environment{
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
		
		public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
	}
	
	public static class UI{
		
		public static class Buttons{
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}
		
		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 40;
			public static final int SOUND_SIZE = (int)(38 * 1.75);
		}
		
		public static class URMButtons{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
		
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * 1.75);
			public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
		
	}
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class playerConstants{
		public static final int ATTACK = 0;
		public static final int DEAD = 1;
		public static final int IDLE = 2;
		public static final int JUMP_UP = 3;
		public static final int JUMP_DOWN = 4;
		public static final int RUN = 5;
		public static final int HIT = 6;
		
		public static final int PLAYER_DAMAGE = 10;
		public static int DAMAGE_MODIFIER;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case ATTACK:
			case DEAD:
				return 6;
			case RUN:
				return 8;
			case IDLE:
				return 8;
			case HIT:
				return 4;
			case JUMP_UP:
			case JUMP_DOWN:
				return 2;
			default:
				return 1;	
			}
		}
	}
	
	public static class Achievements{
		public static final int COLLECTED_CHEST = 0;
		public static final int KILLED_ALPHA = 1;
		public static final int NODMG_LAST_LEVEL = 2;
		
		public static final int MEDAL_WIDTH = 512;
		public static final int MEDAL_HEIGHT = 512;
		
		public static boolean killedAlpha = false;
		public static boolean noDmgLastLevel = false;
		
		public static BufferedImage achievementBG;
		public static BufferedImage[] achievements = new BufferedImage[3];
		public static long[] achievementTimer = new long[3];
		public static boolean[] achievementTracker = new boolean[3];
		
		public static boolean[] getAchievements() {
			return achievementTracker;
		}
	}
}
