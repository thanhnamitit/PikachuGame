package com.dev.thanhnam.pikachugame;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by thanh_000 on 25/08/2015.
 */
public class GamePlayFragment extends Fragment {

    private static final String TAG = "GamePlayFragment";

    private View view;
    private Handler handler;
    private GamePlay gamePlay;
    private Context context;
    private FrameLayout layoutGamePlay;
    private int soundVolume, musicVolume;
    private SharedPreferences preferencesSaveGamePlay;
    private LayoutInflater layoutInflate;

    public GamePlayFragment(Context context, Handler handler, int musicVolume, int soundVolume, SharedPreferences preferencesSaveGamePlay) {
        this.context = context;
        this.handler = handler;
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;
        this.preferencesSaveGamePlay = preferencesSaveGamePlay;
        layoutInflate = layoutInflate.from(context);
        view = layoutInflate.inflate(R.layout.gameplay_layout, null);
        initFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }


    private void initFragment() {
        layoutGamePlay = (FrameLayout) view.findViewById(R.id.myFrame);
        gamePlay = new GamePlay(context, layoutGamePlay, handler, soundVolume,
                musicVolume, preferencesSaveGamePlay);
        layoutGamePlay.addView(gamePlay);
        loadGameData();
    }


    public void continueGame() {
        gamePlay.continueGame();
    }

    public void newClassicGame() {
        gamePlay.createNewGame(true);
    }

    public void newModernGame() {
        gamePlay.createNewGame(false);
    }

    public void pauseGame() {
        gamePlay.pauseThread();
        gamePlay.saveGameData();
        gamePlay.hideAllPokemonTemporary();
    }

    public void loadGameData() {
        try {
            gamePlay.loadGameData();
        } catch (ArrayIndexOutOfBoundsException e) {
            handler.sendEmptyMessage(ControllerActivity.CAN_NOT_CONTINUE_GAME);
        }
    }

    public void saveGameData() {
        gamePlay.saveGameData();
    }

    public void showOptionDialog() {
        gamePlay.showOptionsDialog();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        pauseGame();
        super.onPause();
    }
}
