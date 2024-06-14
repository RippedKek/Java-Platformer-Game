package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {
	
	public final static String ATTACK_ATLAS = "Attack1.png";
	public final static String DEAD_ATLAS = "Death.png";
	public final static String IDLE_ATLAS = "Idle.png";
	public final static String JUMP_START_ATLAS = "Jump.png";
	public final static String JUMP_END_ATLAS = "Fall.png";
	public final static String RUN_ATLAS = "Run.png";
	public final static String HIT_ATLAS = "Take_Hit.png";
	public final static String LEVEL_ATLAS = "outside_sprites.png";
	public final static String MENU_BUTTONS = "button_atlas.png";
	public final static String MENU_BACKGROUND = "menu_background.png";
	public final static String PAUSE_BACKGROUND = "pause_menu.png";
	public final static String SOUND_BUTTONS = "sound_button.png";
	public final static String URM_BUTTONS = "urm_buttons.png";
	public final static String VOLUME_BUTTONS = "volume_buttons.png";
	public final static String MENU_BACKGROUND_IMG = "BG_15.png";
	public final static String PLAYING_BG_IMG = "BG_11.png";
	public final static String BIG_CLOUDS = "big_clouds.png";
	public final static String SMALL_CLOUDS = "small_clouds.png";
	public final static String ENEMY_IDLE = "IdleEnemy.png";
	public final static String ENEMY_RUN = "RunEnemy.png";
	public final static String ENEMY_DEAD = "DeathEnemy.png";
	public final static String ENEMY_HIT = "HitEnemy.png";
	public final static String ENEMY_ATTACK = "AttackEnemy.png";
	public final static String STATUS_BAR = "health_power_bar.png";
	public final static String LEVEL_COMPLETED = "completed_sprite.png";
	public final static String CONTAINER_ATLAS = "objects_sprites.png";
	public final static String POTION_ATLAS = "potions_sprites.png";
	public final static String TRAP_ATLAS = "trap_atlas.png";
	public final static String ALPHA_IDLE = "Alpha_idle.png";
	public final static String ALPHA_RUN = "Alpha_run.png";
	public final static String ALPHA_ATTACK = "Alpha_attack.png";
	public final static String ALPHA_HEAVY_ATTACK = "Alpha_attack2.png";
	public final static String ALPHA_HIT = "Alpha_hit.png";
	public final static String ALPHA_DEAD = "Alpha_death.png";
	public final static String ALPHA_NAME = "Alpha_name.png";
	public final static String NINJA_ATTACK = "Ninja_attack.png";
	public final static String NINJA_FALL = "Ninja_fall.png";
	public final static String NINJA_DEATH = "Ninja_death.png";
	public final static String NINJA_HIT = "Ninja_hit.png";
	public final static String NINJA_IDLE = "Ninja_idle.png";
	public final static String NINJA_RUN = "Ninja_run.png";
	public final static String LIGHTNING = "Lightning_1_128-sheet.png";
	public final static String CANNON_ATLAS = "cannon_atlas.png";
	public final static String CANNON_BALL = "ball.png";
	public final static String DEATH_SCREEN = "death_screen.png";
	public final static String OPTIONS_MENU = "options_background.png";
	public final static String WIND_NINJA_SPRITE = "wind_ninja.png";
	public final static String SHARK_SPRITE = "shark_atlas.png";
	public final static String NECOMANCER_SHEET = "necromancer_sheet.png";
	public final static String CAPTAIN_SHEET = "ninja_captain.png";
	public final static String SPELL_IMG = "spell.png";
	public final static String GRASS_ATLAS = "grass_atlas.png";
	public final static String ICONS_SHEET = "icons.png";
	public final static String COLLECTED_CHEST = "18.png";
	public final static String KILLED_ALPHA = "23.png";
	public final static String NODMGLVL5 = "30.png";
	public final static String ACHIEVEMENT_BG = "achievement_bg.png";
	public final static String CHEST_ATLAS = "chest_sheet.png";
	public final static String TREASURE_IMG = "Treasure+.png";
	public final static String SHINE_SHEET = "Shine_Sheet.png";
	public final static String ACHIEVEMENT_BUTTON = "achievement_button.png";
	public final static String ACHIEVEMENT_DISPLAY = "achievement_background2.png";
	public final static String ACHIEVEMENT_LOCKED = "achievement_locked.png";
	public final static String ACHIEVEMENT_DESC = "achievement_desc.png";
	public final static String TROPHY_IMG = "trophy.png";
	public final static String SMOKE_EFFECT = "smoke.png";
	public final static String PLAYER_LIGHTNING = "player_lightning.png";
	public final static String UNDYING_RAGE = "undying_rage.png";
	public final static String FIREBALL = "fireball.png";
	public final static String EXPLOSION = "Explosion 2 SpriteSheet.png";
	public final static String SKULL_IMG = "skull.png";
	public final static String CONTROL_BASIC = "control_basic.png";
	public final static String ATTACK_BASICS = "attack_basic.png";
	public final static String ULTIMATE_BASICS = "ultimate_basic.png";
	public final static String ENCYCLO_BUTTON = "encyclopedia.png";
	public final static String ENCYCLO_BG = "encyclo_bg.png";
	public final static String ENCYCLOPEDIA_BUTTON = "encyclopedia_buttons.png";
	public final static String ENCYCLOPEDIA_BG = "encyclopedia_bg.png";
	public final static String ENCYCLOPEDIA_TEXT = "encyclopedia_text.png";
	public final static String ENCYCLO_INFO_BG = "info_bg.png";
	public final static String PLAYER_INFO = "player_info_bg.png";
	public final static String ENEMY_INFO = "enemy_info.png";
	public final static String OBJECT_INFO = "object_info.png";
	public final static String HAFEZI_CODING = "hafezi_coding.png";
	public final static String MYSTIC_VEIL = "mystic_veil.png";
	public final static String LEAVES = "leaves.png";
	
	public static BufferedImage getSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;
		
		//URL is the location and URI is the actual resource
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		//Resources from URL is listed inside this array
		//All level images are now in this array
		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];
		
		//Sorting levels based on their name i.e. number
		for(int i=0;i<filesSorted.length;i++)
			for(int j=0;j<files.length;j++) {
				if(files[j].getName().equals("" + (i + 1) + ".png"))
					filesSorted[i] = files[j];
			}
		
		//Loading images from the array into BufferedImage
		BufferedImage[] imgs = new BufferedImage[filesSorted.length];
		for(int i=0;i<imgs.length;i++) {
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imgs;
	}

}
