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
import android.widget.ImageButton;

/**
 * Created by thanh_000 on 25/08/2015.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MenuFragment";

    private Handler handler;
    private Context context;
    private SharedPreferences preferencesSaveGamePlay;


    private View view;
    private ImageButton[] button;
    private static final int ID_IMAGE_BUTTON[] = new int[]{R.id.classic,
            R.id.mordern, R.id.continueGame, R.id.options, R.id.highScore};

    public MenuFragment(Context context, Handler handler, SharedPreferences preferencesSaveGamePlay) {
        this.context = context;
        this.handler = handler;
        this.preferencesSaveGamePlay = preferencesSaveGamePlay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_layout, null);
        initFragment();
        return view;
    }

    private void initFragment() {
        button = new ImageButton[ID_IMAGE_BUTTON.length];
        for (int i = 0; i < ID_IMAGE_BUTTON.length; i++) {
            button[i] = (ImageButton) view.findViewById(ID_IMAGE_BUTTON[i]);
            button[i].setOnClickListener(this);
        }
        checkContinue();
    }

    public void checkContinue() {
        boolean canContinue = preferencesSaveGamePlay.getBoolean(GameData.CONTINUEGAME, false);
        Log.i(TAG, "cancontinue: " + canContinue);
        if (!canContinue) {
            canNotContinue();
        } else {
            canContinue();
        }
    }

    private void canContinue() {
        button[2].setImageResource(R.drawable.continuegame);
        button[2].setOnClickListener(this);
    }

    public void canNotContinue() {
        button[2].setImageResource(R.drawable.nocontinue);
        button[2].setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classic:
                handler.sendEmptyMessage(ControllerActivity.CLASSIC_GAME_CLICK);
                break;
            case R.id.mordern:
                handler.sendEmptyMessage(ControllerActivity.MODERN_GAME_CLICK);
                break;
            case R.id.continueGame:
                handler.sendEmptyMessage(ControllerActivity.CONTINUE_GAME_CLICK);
                break;
            case R.id.highScore:
                handler.sendEmptyMessage(ControllerActivity.HIGH_SCORE_CLICK);
                break;
            case R.id.options:
                handler.sendEmptyMessage(ControllerActivity.OPTION_CLICK);
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        checkContinue();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");

        super.onPause();
    }
}
