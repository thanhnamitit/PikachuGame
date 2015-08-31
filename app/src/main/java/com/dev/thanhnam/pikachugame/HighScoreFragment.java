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
import android.widget.TextView;

/**
 * Created by thanh_000 on 25/08/2015.
 */
public class HighScoreFragment extends Fragment {
    private static final String TAG = "HighScoreFragment";

    public static final String CLASSIC_NAME = "ClassicName";
    public static final String CLASSIC_SCORE = "ClassicScore";
    public static final String MODERN_NAME = "ModernName";
    public static final String MODERN_SCORE = "ModernScore";

    private View view;
    private Handler handler;
    private TextView classicScore, classicName, modernScore, modernName;
    private SharedPreferences preferencesSaveStateOfGame;
    private SharedPreferences.Editor editorSaveStateOfGame;
    LayoutInflater layoutInflate;

    private int classicHighScore, modernHighScore;

    public HighScoreFragment(Context context, Handler handler, SharedPreferences.Editor editorSaveStateOfGame, SharedPreferences preferencesSaveStateOfGame) {
        this.handler = handler;
        this.editorSaveStateOfGame = editorSaveStateOfGame;
        this.preferencesSaveStateOfGame = preferencesSaveStateOfGame;
        layoutInflate = layoutInflate.from(context);
        view = layoutInflate.inflate(R.layout.highscore_layout, null);
        initFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    private void initFragment() {
        classicScore = (TextView) view.findViewById(R.id.classicScore);
        classicName = (TextView) view.findViewById(R.id.classicName);
        modernScore = (TextView) view.findViewById(R.id.modernScore);
        modernName = (TextView) view.findViewById(R.id.modernName);
        classicHighScore = preferencesSaveStateOfGame.getInt(CLASSIC_SCORE, 0);
        classicScore.setText(classicHighScore + "");
        classicName.setText(preferencesSaveStateOfGame.getString(CLASSIC_NAME,
                "Player"));
        modernHighScore = preferencesSaveStateOfGame.getInt(MODERN_SCORE, 0);
        modernScore.setText(modernHighScore + "");
        modernName.setText(preferencesSaveStateOfGame.getString(MODERN_NAME,
                "Player"));
        classicName.setSelected(true);
        modernName.setSelected(true);
    }

    public void updateNewWinnerClassic(int score, String name) {
        classicHighScore = score;
        editorSaveStateOfGame.putString(CLASSIC_NAME, name);
        editorSaveStateOfGame.putInt(CLASSIC_SCORE, score);
        editorSaveStateOfGame.apply();
        classicName.setText(name);
        classicScore.setText(score + "");
    }

    public void updateNewWinnerModern(int score, String name) {
        modernHighScore = score;
        editorSaveStateOfGame.putString(MODERN_NAME, name);
        editorSaveStateOfGame.putInt(MODERN_SCORE, score);
        editorSaveStateOfGame.apply();
        modernName.setText(name);
        modernScore.setText(score + "");
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");

        super.onPause();
    }
}
