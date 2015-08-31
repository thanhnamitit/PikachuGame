package com.dev.thanhnam.pikachugame;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;

public class Soundtrack implements OnCompletionListener, OnErrorListener {
	public static boolean CHECK_RUN = true;
	private static final String LOG_TAG = "Soundtrack";

	private MediaPlayer player;

	public Soundtrack(Context context, Handler handler, float volume) {
		ControllerActivity.CREATE_SOUNDTRACK = true;
		player = MediaPlayer.create(context, R.raw.soundtrack1);
		player.setOnErrorListener(this);

		player.setOnCompletionListener(this);
		// player.setVolume(volume, volume);
		player.start();
	}

	public void continueSound() {
		player.start();
	}

	public void stopSound() {
		player.stop();
		ControllerActivity.CREATE_SOUNDTRACK = false;

	}

	public void pauseSound() {
		if (player.isPlaying()) {
			player.pause();
		}
	}

	public void setVolume(float volume) {
		player.setVolume(volume, volume);
	}

	public void seekSound(int m) {
		player.seekTo(m);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.i(LOG_TAG, "Xong");
		// player.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.i(LOG_TAG, "onError(MediaPlayer mp, i");
		return false;
	}

}
