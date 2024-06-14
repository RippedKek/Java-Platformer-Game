package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	public static int MENU = 0;
	public static int LEVEL = 1;
	public static int FINAL_LEVEL = 2;
	
	public static int PLAYER_ATTACK1 = 0;
	public static int PLAYER_ATTACK2 = 1;
	public static int PLAYER_ATTACK3 = 2;
	public static int PLAYER_BASIC_ATTACK = 3;
	public static int PLAYER_HEAVY_ATTACK = 4;
	public static int PLAYER_ULT = 5;
	public static int PLAYER_DEAD = 6;
	public static int ALPHA_HEAVY_ATTACK = 7;
	public static int ALPHA_1 = 8;
	public static int ALPHA_2 = 9;
	public static int ALPHA_3 = 10;
	public static int LEVEL_COMPLETED = 11;
	public static int LIGHTNING = 12;
	public static int EXPLOSION = 13;
	public static int HEAVY_NINJA_DEATH = 14;
	public static int NECRO_DEATH = 15;
	public static int WIND_NINJA_DEATH = 16;
	public static int CAPTAIN_1 = 17;
	public static int CAPTAIN_2 = 18;
	public static int CAPTAIN_3 = 19;
	public static int CAPTAIN_DEATH = 20;
	public static int POTION = 21;
	public static int ACHIEVMENT_UNLOCKED = 22;
	public static int FIREBALL_PICKUP = 23;
	
	private Clip[] songs, effects;
	private int currentSongId;
	private float volume = 1f;
	private boolean songMute, effectMute;
	private Random rand = new Random();

	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU);
	}
	
	private void loadSongs() {
		String[] names = {"menu","level","alpha_fight"};
		songs = new Clip[names.length];
		for(int i=0;i<songs.length;i++)
			songs[i] = getClip(names[i]);
	}
	
	private void loadEffects() {
		String[] effectNames = {
								"attack1","attack2","attack3","player_basic_attack","player_heavy_attack","undying_rage","player_dead",
								"alpha_heavy","alpha1","alpha2","alpha3",
								"lvlcompleted",
								"lightning","explosion",
								"heavy_ninja_death","necro_death","wind_ninja_death",
								"captain1","captain2","captain3","captain_death",
								"potion","achievement_unlocked","fireball_pickup"
								};
		effects = new Clip[effectNames.length];
		for(int i=0;i<effects.length;i++)
			effects[i] = getClip(effectNames[i]);
		updateEffectsVolume();
	}
	
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;
		
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}
	
	public void stopSong() {
		if(songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}
	
	public void setLevelSong(int lvlIndex) {
		System.out.println(lvlIndex);
		if(lvlIndex == 7)
			playSong(FINAL_LEVEL);
		else
			playSong(LEVEL);
	}
	
	public void lvlCompleted() {
		stopSong();
		playEffect(LEVEL_COMPLETED);
	}
	
	public void playAttackSound() {
		int start = 0;
		start += rand.nextInt(3);
		playEffect(start);
		playEffect(PLAYER_BASIC_ATTACK);
	}
	
	public void playSong(int song) {
		//If any song is playing, stop it
		stopSong();
		currentSongId = song;
		updateSongVolume();
		//Reset new song 
		songs[currentSongId].setMicrosecondPosition(0);
		//Restart once ends
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void playEffect(int effect) {
		//Start audio from start
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}
	
	public void toggleSongMute() {
		this.songMute = !songMute;
		for(Clip c: songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	
	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for(Clip c: effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
//		if(!effectMute)
//			playEffect(PLAYER_ATTACK1);
	}
	
	private void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	private void updateEffectsVolume() {
		for(Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
	
}
