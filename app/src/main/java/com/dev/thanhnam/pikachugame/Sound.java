package com.dev.thanhnam.pikachugame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {
	public static boolean PLAY_SONUD = true;
	private SoundPool sound;
	private Context context;
	private AudioManager audioManager;
	private final static int MAX_SOUNDS = 10;
	private float lengthMusic;

	private float volume;

	private int[] soundID = new int[] { R.raw.sound, R.raw.pikachu_happy,
			R.raw.pikachu_kute, R.raw.game_over, R.raw.game_over_high_score };

	public Sound(Context context, float volumValue) {
		sound = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		lengthMusic = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		lengthMusic = lengthMusic * 1.0F
				/ audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		this.context = context;
		loadSound();
		volume = lengthMusic / 2;
		setVolume(volumValue);
	}

	public void loadSound() {
		for (int i = 0; i < soundID.length; i++) {
			soundID[i] = sound.load(context, soundID[i], i + 1);
		}
	}

	public void setVolume(float volumValue) {
		volume = volumValue * lengthMusic;
	}

	public void hidePokemon() {
		if (PLAY_SONUD) {
			sound.play(soundID[0], volume, volume, 1, 0, 1.0F);
		}
	}

	public void win() {
		if (PLAY_SONUD)
			sound.play(soundID[3], volume, volume, 1, 0, 1.0F);
	}

	public void inGame() {
		if (PLAY_SONUD)
			sound.play(soundID[2], volume, volume, 1, 0, 1.0F);
	}

	public void gameOver() {
		if (PLAY_SONUD)
			sound.play(soundID[4], volume, volume, 1, 0, 1.0F);
	}

	public void highScore() {
		if (PLAY_SONUD)
			sound.play(soundID[1], volume, volume, 1, 0, 1.0F);
	}
}
