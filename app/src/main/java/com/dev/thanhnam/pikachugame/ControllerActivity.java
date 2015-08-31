package com.dev.thanhnam.pikachugame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ControllerActivity extends AppCompatActivity {
    private boolean DEBUG = true;
    private static final String TAG = "ControllerActivity";

    public static final int MENU_FRAGMENT = 1;
    public static final int GAME_PLAY_FRAGMENT = 2;
    public static final int HIGH_SCORE_FRAGMENT = 3;


    public static final int CLASSIC_GAME_CLICK = 0;
    public static final int MODERN_GAME_CLICK = 1;
    public static final int CONTINUE_GAME_CLICK = 2;
    public static final int OPTION_CLICK = 3;
    public static final int HIGH_SCORE_CLICK = 4;
    public static final int BACK_PRESS = 5;
    public static final int UPDATE_NEW_CLASSIC_HIGH_SCORE = 6;
    public static final int UPDATE_NEW_MODERN_HIGH_SCORE = 7;
    public static final int CREATE_NEW_CLASSIC_GAME = 8;
    public static final int CREATE_NEW_MODERN_GAME = 9;
    public static final int CAN_CONTINUE_GAME = 10;
    public static final int CAN_NOT_CONTINUE_GAME = 11;
    public static final int UPDATE_SEEBAR = 12;
    public static final int GAME_OVER = 13;


    public static Sound sound;
    public static int WIDTH_SCREEN;
    public static int HEIGHT_SCREEN;
    public static final String PREFERENCE_SAVE_GAME_STATE = "DataGameState";
    private static final String PREFERENCE_SAVE_GAME_PLAY = "DataGamePlay";


    public static int highscore, classicHighScore, modernHighScore;
    private int soundVolume, musicVolume;
    public static final String PROGESS_SOUND = "ProgessSound";
    public static final String PROGESS_MUSIC = "ProgessMusic";

    public static boolean CREATE_SOUNDTRACK = false;


    private GamePlayFragment gamePlayFragment;
    private HighScoreFragment highScoreFragment;
    private MenuFragment menuFragment;

    private SharedPreferences preferencesSaveStateOfGame;
    private SharedPreferences preferencesSaveGamePlay;
    private DialogWhenCreateNewGame createNewGame;
    public static Soundtrack soundtrack;

    public static int FRAGMENT;


    private Editor editorSaveStateOfGame;
    private Editor editorSaveGamePlay;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i(TAG, msg.what + "");
            switch (msg.what) {
                case CLASSIC_GAME_CLICK:
                    if (preferencesSaveGamePlay.getBoolean(GameData.CONTINUEGAME, false))
                        createNewGame.show(true);
                    else
                        newClassicGame();
                    break;
                case MODERN_GAME_CLICK:
                    if (preferencesSaveGamePlay.getBoolean(GameData.CONTINUEGAME, false))
                        createNewGame.show(false);
                    else
                        newModernGame();
                    break;
                case CONTINUE_GAME_CLICK:
                    continueGame();
                    break;
                case OPTION_CLICK:
                    gamePlayFragment.showOptionDialog();
                    break;
                case HIGH_SCORE_CLICK:
                    showHighScoreFragment();
                    break;
                case CAN_NOT_CONTINUE_GAME:
                    menuFragment.canNotContinue();
                    break;
                case CREATE_NEW_MODERN_GAME:
                    newModernGame();
                    break;
                case CREATE_NEW_CLASSIC_GAME:
                    newClassicGame();
                    break;
                case UPDATE_NEW_CLASSIC_HIGH_SCORE:
                    highScoreFragment.updateNewWinnerClassic(msg.arg1, (String) msg.obj);
                    classicHighScore = msg.arg1;
                    showHighScoreFragment();
                    break;
                case UPDATE_NEW_MODERN_HIGH_SCORE:
                    highScoreFragment.updateNewWinnerModern(msg.arg1, (String) msg.obj);
                    modernHighScore = msg.arg1;
                    showHighScoreFragment();
                    break;
                case UPDATE_SEEBAR:
                    updateNewValueOfSound(msg.arg1, msg.arg2);
                    break;
                case GAME_OVER:
                    editorSaveGamePlay.putBoolean(GameData.CONTINUEGAME, false);
                    showMenuFragment();
                    Log.i(TAG, "handleMessage: GAME_OVER");
                    break;
            }
        }
    };

    private BroadcastReceiver internalBroadCast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gamePlayFragment.saveGameData();
            soundtrack.stopSound();
            if (FRAGMENT != MENU_FRAGMENT) {
                showMenuFragment();
            }

        }
    };

    private void catchBroadCast() {
        IntentFilter filter = new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        IntentFilter filter2 = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        registerReceiver(internalBroadCast, filter);
        registerReceiver(internalBroadCast, filter2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            GamePlay.DEBUG = true;
        }
        super.onCreate(savedInstanceState);
        getWidthHeightScreen();
        setPokemonSize();
        initSharedPreferences();
        intiSound();
        initDialog();
        catchBroadCast();
        initFragment();
        getHighScore();

    }

    private void initFragment() {
        menuFragment = new MenuFragment(this, handler, preferencesSaveGamePlay);
        gamePlayFragment = new GamePlayFragment(this, handler, musicVolume, soundVolume, preferencesSaveGamePlay);
        highScoreFragment = new HighScoreFragment(this, handler, editorSaveStateOfGame, preferencesSaveStateOfGame);
        showMenuFragment();

    }

    public void showGamePlayFragment() {
        FRAGMENT = GAME_PLAY_FRAGMENT;
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_in_left, R.anim.anim_out_right)
                .replace(android.R.id.content, gamePlayFragment).commit();

    }

    public void showMenuFragment() {
        FRAGMENT = MENU_FRAGMENT;
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_in_left, R.anim.anim_out_right)
                .replace(android.R.id.content, menuFragment).commit();
    }

    private void showHighScoreFragment() {
        FRAGMENT = HIGH_SCORE_FRAGMENT;
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_in_left, R.anim.anim_out_right)
                .replace(android.R.id.content, highScoreFragment).commit();
    }


    private void getWidthHeightScreen() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        HEIGHT_SCREEN = displaymetrics.heightPixels;
        WIDTH_SCREEN = displaymetrics.widthPixels;
    }


    public void getHighScore() {
        classicHighScore = preferencesSaveStateOfGame.getInt(HighScoreFragment.CLASSIC_SCORE, 0);
        modernHighScore = preferencesSaveStateOfGame.getInt(HighScoreFragment.MODERN_SCORE, 0);
    }


    private void initSharedPreferences() {
        preferencesSaveStateOfGame = getSharedPreferences(
                PREFERENCE_SAVE_GAME_STATE, Context.MODE_PRIVATE);
        preferencesSaveGamePlay = getSharedPreferences(
                PREFERENCE_SAVE_GAME_PLAY, Context.MODE_PRIVATE);
        editorSaveStateOfGame = preferencesSaveStateOfGame.edit();
        editorSaveGamePlay = preferencesSaveGamePlay.edit();

    }

    private void intiSound() {
        soundVolume = preferencesSaveStateOfGame.getInt(PROGESS_SOUND, 50);
        musicVolume = preferencesSaveStateOfGame.getInt(PROGESS_MUSIC, 50);
        sound = new Sound(this, soundVolume / 100F);
        if (!CREATE_SOUNDTRACK) {
            soundtrack = new Soundtrack(this, handler,
                    (float) musicVolume / 100);
        }

    }

    private void initDialog() {
        createNewGame = new DialogWhenCreateNewGame(this, handler);
    }


    private void updateNewValueOfSound(int soundVolume, int musicVolume) {
        this.musicVolume = musicVolume;
        editorSaveStateOfGame.putInt(PROGESS_SOUND, soundVolume);
        editorSaveStateOfGame.putInt(PROGESS_MUSIC, musicVolume);
        editorSaveStateOfGame.apply();
    }

    long back_press;

    @Override
    public void onBackPressed() {
        GamePlay.TEST = false;
        if (FRAGMENT == MENU_FRAGMENT) {
            if (back_press + 2000 > System.currentTimeMillis()) {
                soundtrack.stopSound();
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(),
                        com.dev.thanhnam.pikachugame.R.string.quit,
                        Toast.LENGTH_SHORT).show();
            }
            back_press = System.currentTimeMillis();
        } else
            showMenuFragment();
    }


    private void continueGame() {
        showGamePlayFragment();
        gamePlayFragment.continueGame();
    }

    private void newClassicGame() {
        highscore = classicHighScore;
        showGamePlayFragment();
        gamePlayFragment.newClassicGame();
    }

    private void newModernGame() {
        highscore = modernHighScore;
        showGamePlayFragment();
        gamePlayFragment.newModernGame();
    }


    public void setPokemonSize() {
        int size = HEIGHT_SCREEN / (GamePlay.NUMBER_POKEMON_VERTICAL + 2);
        if (size * (GamePlay.NUMBER_POKEMON_HORIZOLTAL + 1) > WIDTH_SCREEN) {
            size = WIDTH_SCREEN / (GamePlay.NUMBER_POKEMON_HORIZOLTAL + 1);

            Pokemon.MARGIN_BOTTOM = (int) ((HEIGHT_SCREEN - size * 8.5F) / 2);
            GamePlay.MARGIN_TOP_OF_VIEW = Pokemon.MARGIN_BOTTOM;
        } else {
            Pokemon.MARGIN_BOTTOM = size / 2;
            GamePlay.MARGIN_TOP_OF_VIEW = (int) (size / 4);
        }
        Pokemon.SIZE = size;
        Pokemon.IMAGE_SIZE = size - size / 18;
        GamePlay.MARGIN_LEFT_OF_FIRST_BUTTON = (WIDTH_SCREEN - size
                * GamePlay.NUMBER_POKEMON_HORIZOLTAL) / 2;
    }

    protected void onResume() {
        if (!CREATE_SOUNDTRACK) {
            soundtrack = new Soundtrack(this, handler,
                    (float) musicVolume / 100);

        }
        super.onResume();
    }

}