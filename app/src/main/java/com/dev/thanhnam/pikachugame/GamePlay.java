package com.dev.thanhnam.pikachugame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GamePlay extends View implements OnClickListener {
    private Context context;
    public static String TAG = "GamePlay";
    private Pokemon[][] pokemon;
    private FrameLayout layoutPlayGame, myFrame;
    public static final int NUMBER_POKEMON_HORIZOLTAL = 14,
            NUMBER_POKEMON_VERTICAL = 7;
    private ArrayList<XY> listXYPokemon;
    private Random rd;
    private int numberPokemonOfLevel; // số image dùng trong level
    private int numberPokemon; // Ban đầu = width * height
    private int gameLevel = 1;
    private OpionsDialog dialogOptions;
    private Paint paint;
    private WayGoing wayHorizontal, wayVertical, way;
    private ArrayList<WayGoing> wayPaint;
    private ListXY[] samePokemon;
    private XY xyHelp1, xyHelp2;
    private HelpImage help1, help2;
    private Button btnHelp, btnOptions;
    private ProgressBar time;
    private static final int SHOW_DIALOG_GAME_OVER = 6323123;
    private static final int SHOW_DIALOG_HIGH_SCORE = 525214;
    public static final int CONTINUE_GAME = 23752;
    public static final int REPLAY_THIS_LEVEL = 5342919;
    protected static final int SHOW_POKEMON = 0;
    protected static final int UPDATE_FOR_NEWGAME = 1;
    protected static final int SET_TIME = 2;
    protected static final int HIDE_POKEMON = 3;
    protected static final int COUNT_DOWN_DONE = 7;
    private SharedPreferences preferencesSaveGamePlay;
    private int score;
    public static int MARGIN_TOP_OF_VIEW;
    public static int MARGIN_LEFT_OF_FIRST_BUTTON;
    private int live;
    public static boolean DEBUG = false;
    public static boolean TEST = false;
    public static boolean CLASSIC_MODE;
    private int timeSendMessage = 100;
    private DrawTimeCountDown drawTimeCountDown;

    private AssetManager ass;

    private boolean canContinueGame;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HIDE_POKEMON:
                    hidePokemon(new XY(msg.arg1), new XY(msg.arg2));
                    break;
                case SET_TIME:
                    setTime();
                    break;
                case CREATE_NEW_GAME:
                    showPokemon = true;
                    createNextLevel();
                    break;
                case SHOW_DIALOG_GAME_OVER:
                    canContinueGame = false;
                    ControllerActivity.sound.gameOver();
                    setBackGroundWhenEndGame();

                    if (replay) {
                        dialogReplay.show();
                        replay = false;
                    } else {
                        dialogGameOver.show();
                    }
                    break;
                case SHOW_DIALOG_HIGH_SCORE:
                    canContinueGame = false;
                    ControllerActivity.sound.highScore();
                    setBackGroundWhenEndGame();
                    dialogHighScore.show(score, CLASSIC_MODE);
                    break;
                case CONTINUE_GAME:
                    showPokemonToContinueGame();
                    break;
                case REPLAY_THIS_LEVEL:
                    replayGameLevel();
                    break;
                case SHOW_POKEMON:
                    showPokemon(msg.arg1, msg.arg2);
                    break;
                case UPDATE_FOR_NEWGAME:
                    updateForNewGame();
                    break;
                case COUNT_DOWN_DONE:
                    runThreadShowPokemon();
                default:
                    break;
            }
        }

        ;
    };
    private boolean runSetTime = true;

    private int timeOfLevel;
    private int timeRemaining;
    private ReplayDialog dialogReplay;
    private HighScoreDialog dialogHighScore;
    private boolean setTimeStop = true;

    private Runnable runableSetTime = new Runnable() {

        @Override
        public void run() {
            while (runSetTime && live != 0) {
                handler.sendEmptyMessage(SET_TIME);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            setTimeStop = true;

            if ((timeRemaining <= 0 && numberPokemon > 0) || live == 0) {
                pauseThread();
                editor.putBoolean(GameData.CONTINUEGAME, false);
                editor.apply();
                Log.i(TAG, "SCORE: " + score + "    "
                        + ControllerActivity.highscore);
                Message message = new Message();
                if (score > ControllerActivity.highscore)
                    message.what = SHOW_DIALOG_HIGH_SCORE;

                else
                    message.what = SHOW_DIALOG_GAME_OVER;

                handler.sendMessageDelayed(message, 1000);
            }
        }
    };
    private Runnable runableTimeCountDown = new Runnable() {

        @Override
        public void run() {
            drawTimeCountDown.setScale();
            timeCountDown = DrawTimeCountDown.ID_NUMBER.length * 750;
            while (--timeCountDown > 0) {

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handlerDraw.sendEmptyMessage(1);
            }
            handler.sendEmptyMessage(COUNT_DOWN_DONE);

        }
    };
    private boolean showPokemon = true;
    private Runnable runableShowPokemon = new Runnable() {

        @Override
        public void run() {
            for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
                for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                    listXYPokemon.add(new XY(i, j));
                }
            }
            int integerRandom;
            Log.i(TAG, showPokemon + "");
            while (listXYPokemon.size() != 0 && showPokemon) {

                integerRandom = rd.nextInt(listXYPokemon.size());
                Message message = new Message();
                message.what = SHOW_POKEMON;
                message.arg1 = listXYPokemon.get(integerRandom).x;
                message.arg2 = listXYPokemon.get(integerRandom).y;
                listXYPokemon.remove(integerRandom);
                handler.sendMessage(message);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if (showPokemon)
                handler.sendEmptyMessage(UPDATE_FOR_NEWGAME);

        }
    };

    private Runnable runableDrawHelp = new Runnable() {

        @Override
        public void run() {
            while (drawHelp && numberPokemon > 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handlerDraw.sendEmptyMessage(1);
            }
        }
    };

    private void runThreadSetTime() {
        if (!setTimeStop) {
            runSetTime = true;
            return;
        }
        // if (timeRemaining > 0)
        runSetTime = true;
        setTimeStop = false;
        new Thread(runableSetTime).start();
    }

    private void runThreadDrawTimeCountDown() {
        new Thread(runableTimeCountDown).start();

    }

    private void runThreadShowPokemon() {
        new Thread(runableShowPokemon).start();
    }

    private void runThreadDrawHelp() {
        new Thread(runableDrawHelp).start();
    }

    private void showPokemon(int x, int y) {
        pokemon[x][y].showToContinue();
    }

    private void updateForNewGame() {
        updateLevelAndLive();
        myFrame.setVisibility(View.VISIBLE);
        runThreadSetTime();
        showPokemon = false;
    }

    private int timeCountDown = 0;

    private void setBackGroundWhenEndGame() {
        pokemon[saveID / 10][saveID % 10].setBackground1();
        pokemon[xyHelp1.x][xyHelp1.y].setBackground2();
        pokemon[xyHelp2.x][xyHelp2.y].setBackground2();
    }

    private Editor editor;

    public void setTime() {
        if (numberPokemon > 0) {
            int progess = ((int) (float) (-1 + timeRemaining--) * 100 / timeOfLevel);
            if (progess == 0 && timeRemaining >= 1)
                progess = 1;

            time.setProgress(progess);
        }

        if (timeRemaining <= 0) {
            runSetTime = false;
        }
    }

    private Handler handlerOfLayoutPlayGame;
    private GameOverDialog dialogGameOver;

    public GamePlay(Context context, FrameLayout frame, Handler handler,
                    int soundVoulme, int musicVolume,
                    SharedPreferences sharedPreferences) {
        super(context);
        if (DEBUG)
            live = 100;
        ass = context.getAssets();
        handlerOfLayoutPlayGame = handler;
        this.preferencesSaveGamePlay = sharedPreferences;
        editor = preferencesSaveGamePlay.edit();
        this.context = context;
        this.layoutPlayGame = frame;
        rd = new Random();
        checkClick = false;
        xyHelp1 = new XY();
        xyHelp2 = new XY();
        initPaint();
        initDialog(soundVoulme, musicVolume);
        listXYPokemon = new ArrayList<XY>();

        addButton();
        addPokemonImage();
        setWillNotDraw(false);

    }

    private TextView textViewScore, textViewLevel;

    public void addButton() {
        myFrame = new FrameLayout(context);
        int marginTop, marginLeft;
        btnHelp = new Button(context);
        btnHelp.setBackgroundResource(R.drawable.background_help);
        LayoutParams lpHelp = new LayoutParams(Pokemon.IMAGE_SIZE,
                Pokemon.IMAGE_SIZE);
        marginTop = MARGIN_TOP_OF_VIEW;
        marginLeft = MARGIN_LEFT_OF_FIRST_BUTTON;
        lpHelp.setMargins(marginLeft, marginTop, 0, 0);
        myFrame.addView(btnHelp);
        btnHelp.setLayoutParams(lpHelp);
        btnHelp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (numberPokemon == 0)
                    return;
                helpClick();
            }
        });

        btnOptions = new Button(context);
        btnOptions.setBackgroundResource(R.drawable.background_options);
        LayoutParams lpOptions = new LayoutParams(Pokemon.IMAGE_SIZE,
                Pokemon.IMAGE_SIZE);
        marginLeft += Pokemon.SIZE;
        lpOptions.setMargins(marginLeft, marginTop, 0, 0);
        myFrame.addView(btnOptions);
        btnOptions.setLayoutParams(lpOptions);
        btnOptions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });
        textViewScore = new TextView(context);
        textViewScore.setSingleLine();
        textViewScore.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        textViewScore.setTextSize(15);
        textViewScore.setBackgroundResource(R.drawable.score);
        textViewScore.setText("Score: 0");
        textViewScore.setTextColor(Color.RED);
        textViewScore.setGravity(Gravity.CENTER);
        LayoutParams lpTextView = new LayoutParams(Pokemon.SIZE * 2
                + Pokemon.IMAGE_SIZE, Pokemon.IMAGE_SIZE);
        marginLeft += Pokemon.SIZE * 2;

        lpTextView.setMargins(marginLeft, marginTop, 0, 0);
        myFrame.addView(textViewScore);
        textViewScore.setLayoutParams(lpTextView);
        textViewScore.setFocusable(true);

        time = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        time.setMax(100);
        time.setProgress(50);
        LayoutParams lpTime = new LayoutParams(6 * Pokemon.SIZE
                + Pokemon.IMAGE_SIZE, Pokemon.IMAGE_SIZE);
        marginTop = MARGIN_TOP_OF_VIEW;
        marginLeft += 4 * Pokemon.SIZE;
        lpTime.setMargins(marginLeft, marginTop, 0, 0);
        myFrame.addView(time);
        time.setLayoutParams(lpTime);

        textViewLevel = new TextView(context);
        textViewLevel.setSingleLine();
        textViewLevel.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        textViewLevel.setTextSize(16);
        textViewLevel.setTextColor(Color.parseColor("#1E88E5"));
        textViewLevel.setGravity(Gravity.CENTER);
        LayoutParams lpTextViewLevel = new LayoutParams(6 * Pokemon.SIZE
                + Pokemon.IMAGE_SIZE, Pokemon.IMAGE_SIZE);

        lpTextViewLevel.setMargins(marginLeft, marginTop, 0, 0);
        myFrame.addView(textViewLevel);
        textViewLevel.setLayoutParams(lpTextViewLevel);
        textViewLevel.setFocusable(true);

        if (DEBUG) {
            timeSendMessage = 20;
            Button change = new Button(context);
            change.setBackgroundResource(R.drawable.background_options);
            LayoutParams lpChange = new LayoutParams(Pokemon.IMAGE_SIZE,
                    Pokemon.IMAGE_SIZE);
            marginLeft += Pokemon.SIZE * 2;
            lpChange.setMargins(marginLeft, marginTop, 0, 0);
            myFrame.addView(change);
            change.setLayoutParams(lpChange);
            change.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!TEST)
                        TEST = true;
                    else
                        TEST = false;
                }
            });

            Button setTime = new Button(context);
            change.setBackgroundResource(R.drawable.background_options);
            LayoutParams lpSetTime = new LayoutParams(Pokemon.IMAGE_SIZE,
                    Pokemon.IMAGE_SIZE);
            marginLeft -= Pokemon.SIZE;
            lpSetTime.setMargins(marginLeft, marginTop, 0, 0);
            myFrame.addView(setTime);
            setTime.setLayoutParams(lpSetTime);
            setTime.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    timeRemaining = 1;
                }
            });
        }

        layoutPlayGame.addView(myFrame);
        myFrame.setLayoutParams(new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    private void updateLevelAndLive() {
        textViewLevel.setText("Level: " + gameLevel + " - Live: " + live);
    }

    private void initDialog(int soundVolume, int soundMusic) {
        dialogReplay = new ReplayDialog(context, handler,
                handlerOfLayoutPlayGame);
        dialogGameOver = new GameOverDialog(context, handlerOfLayoutPlayGame);
        dialogOptions = new OpionsDialog(context, handlerOfLayoutPlayGame, handler,
                soundVolume, soundMusic);
        dialogHighScore = new HighScoreDialog(context, handlerOfLayoutPlayGame);
    }

    private int helpRemaning;
    private boolean checkClickHelp = false;
    private XY xyDrawHelp1, xyDrawHelp2;

    public void helpClick() {
        if (helpRemaning == 0)
            return;
        pokemon[saveID / 10][saveID % 10].setBackground1();
        checkClick = false;
        saveID = 0;// nếu không gán thì sẽ bị trường hợp không
        // setBackground 2 nhưng vẫn ăn!!!
        if (!drawHelp) {
            xyDrawHelp1 = pokemon[xyHelp1.x][xyHelp1.y].getXandY().clone();
            xyDrawHelp2 = pokemon[xyHelp2.x][xyHelp2.y].getXandY().clone();
            drawHelp = true;
            runThreadDrawHelp();
        }
        if (!checkClickHelp) {
            --helpRemaning;
            checkClickHelp = true;
        }

        if (helpRemaning == 0) {
            btnHelp.setBackgroundResource(R.drawable.help12);
        }

    }

    private Handler handlerDraw = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
    public static boolean drawHelp = false;

    public void addPokemonImage() {

        dataGame = new GameData(NUMBER_POKEMON_HORIZOLTAL + 2,
                NUMBER_POKEMON_VERTICAL + 2);
        pokemon = new Pokemon[NUMBER_POKEMON_HORIZOLTAL + 2][NUMBER_POKEMON_VERTICAL + 2];
        for (int i = 0; i <= NUMBER_POKEMON_HORIZOLTAL + 1; i++) {
            pokemon[i][NUMBER_POKEMON_VERTICAL + 1] = new Pokemon(context, i,
                    NUMBER_POKEMON_VERTICAL + 1, NUMBER_POKEMON_HORIZOLTAL,
                    NUMBER_POKEMON_VERTICAL);
            pokemon[i][NUMBER_POKEMON_VERTICAL + 1].setYPathBot();
            pokemon[i][0] = new Pokemon(context, i, 0,
                    NUMBER_POKEMON_HORIZOLTAL, NUMBER_POKEMON_VERTICAL);
            pokemon[i][0].setYPathTop();
        }
        for (int i = 0; i <= NUMBER_POKEMON_VERTICAL + 1; i++) {
            pokemon[0][i] = new Pokemon(context, 0, i,
                    NUMBER_POKEMON_HORIZOLTAL, NUMBER_POKEMON_VERTICAL);
            pokemon[0][i].setXPathLeft();
            pokemon[NUMBER_POKEMON_HORIZOLTAL + 1][i] = new Pokemon(context,
                    NUMBER_POKEMON_HORIZOLTAL + 1, i,
                    NUMBER_POKEMON_HORIZOLTAL, NUMBER_POKEMON_VERTICAL);
            pokemon[NUMBER_POKEMON_HORIZOLTAL + 1][i].setXPathRigth();
        }
        for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
            for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                pokemon[i][j] = new Pokemon(context, 0,
                        NUMBER_POKEMON_HORIZOLTAL, NUMBER_POKEMON_VERTICAL);
                pokemon[i][j].setOnClickListener(this);
                layoutPlayGame.addView(pokemon[i][j]);
                pokemon[i][j].setXY(i, j);
            }
        }
    }

    private void setNumberPokemonOfLevel() {
        if (!CLASSIC_MODE)
            numberPokemonOfLevel = (gameLevel + 4) * 3;
        else
            numberPokemonOfLevel = (gameLevel + 4) * 4;
        if (numberPokemonOfLevel > GameResources.pokemonImageID.length)
            numberPokemonOfLevel = GameResources.pokemonImageID.length;
    }

    public void setPokemonImage() {
        listXYPokemon.clear();
        myFrame.setVisibility(View.GONE);
        resetView();
        createNewBackGround();

        saveID = 0;
        setNumberPokemonOfLevel();
        samePokemon = new ListXY[numberPokemonOfLevel];
        for (int i = 0; i < samePokemon.length; i++) {
            samePokemon[i] = new ListXY();
        }

        if (CLASSIC_MODE)
            readFile();
        else {
            numberPokemon = NUMBER_POKEMON_HORIZOLTAL * NUMBER_POKEMON_VERTICAL;

            for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
                for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                    listXYPokemon.add(new XY(i, j));
                }
            }
        }
        int integerRandom, imageIDRandom, i;

        while (listXYPokemon.size() != 0) {

            imageIDRandom = rd.nextInt(numberPokemonOfLevel);
            for (i = 0; i < 2; i++) {
                integerRandom = rd.nextInt(listXYPokemon.size());
                pokemon[listXYPokemon.get(integerRandom).x][listXYPokemon
                        .get(integerRandom).y].setNewImage(imageIDRandom);
                samePokemon[imageIDRandom].list.add(listXYPokemon.get(
                        integerRandom).clone());
                listXYPokemon.remove(integerRandom);
            }
        }
        checkStillTheWay(false);

        // runThreadShowPokemon();

    }

    private void readFile() {
        numberPokemon = 0;
        if (gameLevel % 11 == 1) {
            numberPokemon = NUMBER_POKEMON_HORIZOLTAL * NUMBER_POKEMON_VERTICAL;

            for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
                for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                    listXYPokemon.add(new XY(i, j));
                }
            }
            return;
        }

        BufferedReader reader = null;

        String fileName = "aloxo" + gameLevel % 11;

        int value1, value2;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    ass.open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine = "Nam Dep Trai";
            while (mLine != null) {
                mLine = reader.readLine();
                value1 = Integer
                        .parseInt(mLine.substring(0, mLine.indexOf(" ")));
                value2 = Integer.parseInt(mLine.substring(
                        mLine.lastIndexOf(" ") + 1, mLine.length()));
                listXYPokemon.add(new XY(value1, value2));
                numberPokemon++;
            }
        } catch (IOException e) {
            // log the exception
        } catch (NullPointerException e) {
            // TODO: handle exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // log the exception
                }
            }
        }

    }

    private int indexBackGroundPokemon = 02, indexBackGround = 1995;

    public void createNewBackGround() {
        int index;

        index = indexBackGroundPokemon;
        while (index == indexBackGroundPokemon) {
            indexBackGroundPokemon = rd
                    .nextInt(GameResources.backGroundPokemonID1.length);
        }

        index = indexBackGround;
        while (index == indexBackGround) {
            indexBackGround = rd.nextInt(GameResources.backGroundGameID.length);
        }
        setNewBackGround();
    }

    private void setNewBackGround() {
        Pokemon.backgroundID1 = GameResources.backGroundPokemonID1[indexBackGroundPokemon];
        Pokemon.backgroundID2 = GameResources.backGroundPokemonID2[indexBackGroundPokemon];
        layoutPlayGame
                .setBackgroundResource(GameResources.backGroundGameID[indexBackGround]);
    }

    public void resetView() {
        helpRemaning = 3;
        if (DEBUG) {
            helpRemaning = 100;
        }
        btnHelp.setBackgroundResource(R.drawable.background_help);
        setTimeOfLevel();

        timeRemaining = timeOfLevel;
        runSetTime = true;
        time.setProgress(100);
    }

    private int idValue, saveID = 0;
    private boolean checkClick;

    @Override
    public void onClick(View v) {
        drawHelp = false;
        if (showPokemon)
            return;
        if (pokemon[v.getId() / 10][v.getId() % 10].getCheckOn() == false)
            return;
        idValue = v.getId();
        if (saveID == v.getId() && checkClick) {
            pokemon[idValue / 10][idValue % 10].setBackground1();
            checkClick = false;
            saveID = 0;
        } else if (checkClick == false) {
            saveID = idValue;
            checkClick = true;
            pokemon[idValue / 10][idValue % 10].setBackground2();
        } else {
            checkImage();
        }
    }

    public void checkImage() {
        if (pokemon[idValue / 10][idValue % 10].getKind() == pokemon[saveID / 10][saveID % 10]
                .getKind()
                && checkWayGoing(idValue, saveID)
                && pokemon[idValue / 10][idValue % 10].getCheckOn()) {
            pokemon[idValue / 10][idValue % 10].setBackground2();
            pokemon[idValue / 10][idValue % 10].setCheckOn();
            pokemon[saveID / 10][saveID % 10].setCheckOn();
            callHandler();
            checkClick = false;
        } else {
            pokemon[idValue / 10][idValue % 10].setBackground2();
            pokemon[saveID / 10][saveID % 10].setBackground1();
            saveID = idValue;
        }
    }

    private int checkX1, checkY1, checkX2, checkY2;

    private void callHandler() {
        wayPaint.add(new WayGoing(way));
        invalidate();
        Message message = new Message();
        message.what = HIDE_POKEMON;
        message.arg1 = saveID;
        message.arg2 = idValue;
        handler.sendMessageDelayed(message, timeSendMessage);

    }

    private void updateScore(int score) {
        this.score += score;
        textViewScore.setText("Score: " + this.score);
    }

    public void hidePokemon(XY xy1, XY xy2) {
        updateScore(1);
        checkClickHelp = false;
        wayPaint.remove(0);

        invalidate();

        ControllerActivity.sound.hidePokemon();

        movePokemon(xy1, xy2);
        numberPokemon -= 2;

        if (numberPokemon == 0) {
            ControllerActivity.sound.win();
            win();
            return;
        }
        checkStillTheWay(true);
    }

    public void removeSamePokemonInList(XY xy) {
        int kind = pokemon[xy.x][xy.y].getKind();
        for (int i = 0; i < samePokemon[kind].list.size(); i++) {
            if (samePokemon[kind].list.get(i).equals(xy)) {
                samePokemon[kind].list.remove(i);
                i--;
                break;
            }
        }
    }

    private static final int CREATE_NEW_GAME = 19995;

    private void win() {
        live++;
        updateScore(timeRemaining);
        if (!CLASSIC_MODE)
            updateScore(helpRemaning * 5);
        pauseThread();
        handler.sendEmptyMessageDelayed(CREATE_NEW_GAME, 1000);

    }

    public void createNewGame(boolean CLASSIC_GAME) {

        canContinueGame = true;

        showPokemon = true;
        gameLevel = 1;
        setPokemonImage();

        runThreadDrawTimeCountDown();
        replay = false;
        CLASSIC_MODE = CLASSIC_GAME;
        if (CLASSIC_MODE) {
            btnHelp.setVisibility(Button.INVISIBLE);
            live = 3;

        } else {
            btnHelp.setVisibility(Button.VISIBLE);

            live = 10;

        }
        textViewScore.setText("Score: 0");
        score = 0;

    }

    public void createNextLevel() {
        myFrame.setVisibility(View.GONE);
        replay = true;
        runSetTime = false;
        gameLevel++;
        setPokemonImage();
        if (ControllerActivity.FRAGMENT == ControllerActivity.GAME_PLAY_FRAGMENT)
            runThreadShowPokemon();
    }

    private void checkStillTheWay(boolean subLive) {

        int samePokemonLength = samePokemon.length;
        int i = rd.nextInt(samePokemonLength);
        int theLoopNumber = -1;
        while (++theLoopNumber < samePokemonLength) {
            for (int j = 0; j < samePokemon[i].list.size() - 1; j++) {
                for (int k = j + 1; k < samePokemon[i].list.size(); k++) {
                    if (TEST) {
                        saveID = samePokemon[i].list.get(j).x * 10
                                + samePokemon[i].list.get(j).y;
                        idValue = samePokemon[i].list.get(k).x * 10
                                + samePokemon[i].list.get(k).y;
                        if (checkWayGoing(saveID, idValue) == true) {
                            xyHelp1 = samePokemon[i].list.get(j).clone();
                            xyHelp2 = samePokemon[i].list.get(k).clone();
                            callHandler();
                            return;

                        }
                    } else if (checkStillWay(samePokemon[i].list.get(j).x * 10
                                    + samePokemon[i].list.get(j).y,
                            samePokemon[i].list.get(k).x * 10
                                    + samePokemon[i].list.get(k).y) == true) {
                        xyHelp1 = samePokemon[i].list.get(j).clone();
                        xyHelp2 = samePokemon[i].list.get(k).clone();
                        return;
                    }
                }
            }
            if (++i == samePokemonLength)
                i = 0;
        }
        if (subLive) {
            live--;
            updateLevelAndLive();
        }
        changeLocation();
    }

    public void changeLocation() {
        ArrayList<XY> listPokemonShow = new ArrayList<XY>();
        ArrayList<Integer> listKindOfPokemon = new ArrayList<Integer>();
        for (int i = 0; i < samePokemon.length; i++) {
            for (int j = 0; j < samePokemon[i].list.size(); j++) {
                listPokemonShow.add(samePokemon[i].list.get(j).clone());
                listKindOfPokemon
                        .add(pokemon[samePokemon[i].list.get(j).x][samePokemon[i].list
                                .get(j).y].getKind());
            }
        }
        for (int i = 0; i < samePokemon.length; i++) {
            samePokemon[i].list.clear();
        }
        int rdIndex;
        int rdPokemon;
        while (listKindOfPokemon.size() != 0) {
            rdIndex = rd.nextInt(listKindOfPokemon.size());
            rdPokemon = rd.nextInt(listPokemonShow.size());
            pokemon[listPokemonShow.get(rdPokemon).x][listPokemonShow
                    .get(rdPokemon).y].setKind(listKindOfPokemon.get(rdIndex));
            samePokemon[listKindOfPokemon.get(rdIndex)].list
                    .add(listPokemonShow.get(rdPokemon).clone());
            listKindOfPokemon.remove(rdIndex);
            listPokemonShow.remove(rdPokemon);
        }
        checkStillTheWay(false);
    }

    private void initPaint() {
        help1 = new HelpImage(context);
        help2 = new HelpImage(context);
        drawTimeCountDown = new DrawTimeCountDown(context);
        wayHorizontal = new WayGoing();
        wayVertical = new WayGoing();
        way = new WayGoing();

        wayPaint = new ArrayList<WayGoing>();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(ControllerActivity.WIDTH_SCREEN / 150);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (timeCountDown > 0) {
            drawTimeCountDown.drawNumber(canvas, paint, timeCountDown / (750));
        }
        if (drawHelp) {
            help1.drawImage(canvas, paint, xyDrawHelp1);
            help2.drawImage(canvas, paint, xyDrawHelp2);
        }

        paint.setAlpha(255);
        for (int i = 0; i < wayPaint.size(); i++) {
            wayPaint.get(i).drawWay(canvas, paint);
        }
    }

    private void hidePokemon(XY xy) {
        pokemon[xy.x][xy.y].hide();
    }

    private void movePokemon(XY xy1, XY xy2) {
        if (CLASSIC_MODE) {
            doNotMovePokemon(xy1);
            doNotMovePokemon(xy2);
            return;
        }
        switch (gameLevel) {
            case 1:
                doNotMovePokemon(xy1);
                doNotMovePokemon(xy2);
                break;
            case 2:
                moveUp(xy1, xy2);
                break;
            case 3:
                moveDown(xy1, xy2);
                break;
            case 4:
                moveCenterVertical(xy1, xy2);
                break;
            case 5:
                moveLeft(xy1, xy2);
                break;
            case 6:
                moveRight(xy1, xy2);
                break;
            case 7:
                moveCenterHorizontal(xy1, xy2);
                break;
            case 8:
                moveUpAndLeft(xy1, xy2);
                break;
            case 9:
                moveUpAndRight(xy1, xy2);
                break;
            case 10:
                moveDownAndRight(xy1, xy2);
                break;
            case 11:
                moveDownAndLeft(xy1, xy2);
                break;
            case 12:
                moveLeftAndUp(xy1, xy2);
                break;
            case 13:
                moveRightAndUp(xy1, xy2);
                break;
            case 14:
                moveRightAndDown(xy1, xy2);
                break;
            case 15:
                moveLeftAndDown(xy1, xy2);
                break;
            default:
                moveRandom(xy1, xy2);
                break;
        }

    }

    private void moveLeft(XY xy1, XY xy2) {
        if (xy1.x > xy2.x) {
            hidePokemon(movePokemonLeft(xy1));
            hidePokemon(movePokemonLeft(xy2));
        } else {
            hidePokemon(movePokemonLeft(xy2));
            hidePokemon(movePokemonLeft(xy1));
        }
    }

    private void moveRight(XY xy1, XY xy2) {
        if (xy1.x < xy2.x) {
            hidePokemon(movePokemonRigth(xy1));
            hidePokemon(movePokemonRigth(xy2));
        } else {
            hidePokemon(movePokemonRigth(xy2));
            hidePokemon(movePokemonRigth(xy1));
        }
    }

    private void moveUp(XY xy1, XY xy2) {
        if (xy1.y > xy2.y) {
            hidePokemon(movePokemonUp(xy1));
            hidePokemon(movePokemonUp(xy2));

        } else {
            hidePokemon(movePokemonUp(xy2));
            hidePokemon(movePokemonUp(xy1));
        }

    }

    private void moveDown(XY xy1, XY xy2) {
        if (xy1.y < xy2.y) {
            hidePokemon(movePokemonDown(xy1));
            hidePokemon(movePokemonDown(xy2));
        } else {
            hidePokemon(movePokemonDown(xy2));
            hidePokemon(movePokemonDown(xy1));
        }
    }

    private void moveCenterVertical(XY xy1, XY xy2) {
        if (xy1.y <= NUMBER_POKEMON_VERTICAL / 2
                && xy2.y <= NUMBER_POKEMON_VERTICAL / 2) {
            moveDown(xy1, xy2);
        } else if (xy1.y > NUMBER_POKEMON_VERTICAL / 2
                && xy2.y > NUMBER_POKEMON_VERTICAL / 2) {
            moveUp(xy1, xy2);
        } else if (xy1.y <= NUMBER_POKEMON_VERTICAL / 2
                && xy2.y > NUMBER_POKEMON_VERTICAL / 2) {
            hidePokemon(movePokemonUp(xy2));
            hidePokemon(movePokemonDown(xy1));
        } else {
            hidePokemon(movePokemonUp(xy1));
            hidePokemon(movePokemonDown(xy2));
        }
    }

    private void moveCenterHorizontal(XY xy1, XY xy2) {
        if (xy1.x <= NUMBER_POKEMON_HORIZOLTAL / 2
                && xy2.x <= NUMBER_POKEMON_HORIZOLTAL / 2) {
            moveRight(xy1, xy2);
        } else if (xy1.x > NUMBER_POKEMON_HORIZOLTAL / 2
                && xy2.x > NUMBER_POKEMON_HORIZOLTAL / 2) {
            moveLeft(xy1, xy2);
        } else if (xy1.x > NUMBER_POKEMON_HORIZOLTAL / 2
                && xy2.x <= NUMBER_POKEMON_HORIZOLTAL / 2) {
            hidePokemon(movePokemonRigth(xy2));
            hidePokemon(movePokemonLeft(xy1));
        } else {
            hidePokemon(movePokemonRigth(xy1));
            hidePokemon(movePokemonLeft(xy2));
        }
    }

    private void moveUpAndLeft(XY xy1, XY xy2) {
        if (xy1.y > xy2.y) {
            xy1 = movePokemonUp(xy1);
            hidePokemon(xy1);
            xy2 = movePokemonUp(xy2);

        } else {
            xy2 = movePokemonUp(xy2);
            hidePokemon(xy2);
            xy1 = movePokemonUp(xy1);
        }
        moveLeft(xy1, xy2);
    }

    private void moveUpAndRight(XY xy1, XY xy2) {
        if (xy1.y > xy2.y) {
            xy1 = movePokemonUp(xy1);
            hidePokemon(xy1);
            xy2 = movePokemonUp(xy2);

        } else {
            xy2 = movePokemonUp(xy2);
            hidePokemon(xy2);
            xy1 = movePokemonUp(xy1);
        }
        moveRight(xy1, xy2);
    }

    private void moveDownAndRight(XY xy1, XY xy2) {
        if (xy1.y < xy2.y) {
            hidePokemon(xy1 = movePokemonDown(xy1));
            xy2 = movePokemonDown(xy2);
        } else {
            hidePokemon(xy2 = movePokemonDown(xy2));
            xy1 = movePokemonDown(xy1);
        }
        moveRight(xy1, xy2);
    }

    private void moveDownAndLeft(XY xy1, XY xy2) {
        if (xy1.y < xy2.y) {
            hidePokemon(xy1 = movePokemonDown(xy1));
            xy2 = movePokemonDown(xy2);
        } else {
            hidePokemon(xy2 = movePokemonDown(xy2));
            xy1 = movePokemonDown(xy1);
        }
        moveLeft(xy1, xy2);
    }

    private void moveLeftAndUp(XY xy1, XY xy2) {
        if (xy1.x > xy2.x) {
            hidePokemon(xy1 = movePokemonLeft(xy1));
            xy2 = movePokemonLeft(xy2);
        } else {
            hidePokemon(xy2 = movePokemonLeft(xy2));
            xy1 = movePokemonLeft(xy1);
        }

        moveUp(xy1, xy2);
    }

    private void moveRightAndUp(XY xy1, XY xy2) {
        if (xy1.x < xy2.x) {
            hidePokemon(xy1 = movePokemonRigth(xy1));
            xy2 = movePokemonRigth(xy2);
        } else {
            hidePokemon(xy2 = movePokemonRigth(xy2));
            xy1 = movePokemonRigth(xy1);
        }

        moveUp(xy1, xy2);
    }

    private void moveRightAndDown(XY xy1, XY xy2) {
        if (xy1.x < xy2.x) {
            hidePokemon(xy1 = movePokemonRigth(xy1));
            xy2 = movePokemonRigth(xy2);
        } else {
            hidePokemon(xy2 = movePokemonRigth(xy2));
            xy1 = movePokemonRigth(xy1);
        }

        moveDown(xy1, xy2);
    }

    private void moveLeftAndDown(XY xy1, XY xy2) {
        if (xy1.x > xy2.x) {
            hidePokemon(xy1 = movePokemonLeft(xy1));
            xy2 = movePokemonLeft(xy2);
        } else {
            hidePokemon(xy2 = movePokemonLeft(xy2));
            xy1 = movePokemonLeft(xy1);
        }

        moveDown(xy1, xy2);
    }

    private void moveRandom(XY xy1, XY xy2) {
        switch (rd.nextInt(6)) {
            case 0:
                moveLeft(xy1, xy2);
                break;
            case 1:
                moveCenterHorizontal(xy1, xy2);
                break;
            case 2:
                moveCenterVertical(xy1, xy2);
                break;
            case 3:
                moveDown(xy1, xy2);
                break;
            case 4:
                moveUp(xy1, xy2);
                break;
            case 5:
                moveRight(xy1, xy2);
                break;

            default:
                break;
        }
    }

    private void doNotMovePokemon(XY xy) {
        pokemon[xy.x][xy.y].hide();
        removeSamePokemonInList(xy);

    }

    private XY movePokemonUp(XY xy) {
        int i = xy.y;
        for (; true; i++) {
            if (!pokemon[xy.x][i + 1].getCheckOn())
                break;
            removeSamePokemonInList(new XY(xy.x, i));
            pokemon[xy.x][i].setKind(pokemon[xy.x][i + 1].getKind());
            samePokemon[pokemon[xy.x][i].getKind()].list.add(pokemon[xy.x][i]
                    .getXY());
        }
        pokemon[xy.x][xy.y].show();
        // pokemon[xy.x][i].hide();
        removeSamePokemonInList(new XY(xy.x, i));
        return new XY(xy.x, i);
    }

    private XY movePokemonDown(XY xy) {
        int i = xy.y;
        for (; true; i--) {
            if (!pokemon[xy.x][i - 1].getCheckOn())
                break;
            removeSamePokemonInList(new XY(xy.x, i));
            pokemon[xy.x][i].setKind(pokemon[xy.x][i - 1].getKind());
            samePokemon[pokemon[xy.x][i].getKind()].list.add(pokemon[xy.x][i]
                    .getXY());
        }
        pokemon[xy.x][xy.y].show();
        removeSamePokemonInList(new XY(xy.x, i));
        return new XY(xy.x, i);
    }

    private XY movePokemonLeft(XY xy) {
        int i = xy.x;
        for (; true; i++) {
            if (!pokemon[i + 1][xy.y].getCheckOn())
                break;
            removeSamePokemonInList(new XY(i, xy.y));
            pokemon[i][xy.y].setKind(pokemon[i + 1][xy.y].getKind());
            samePokemon[pokemon[i][xy.y].getKind()].list.add(pokemon[i][xy.y]
                    .getXY());
        }
        pokemon[xy.x][xy.y].show();
        removeSamePokemonInList(new XY(i, xy.y));
        return new XY(i, xy.y);
    }

    private XY movePokemonRigth(XY xy) {
        int i = xy.x;
        for (; true; i--) {
            if (!pokemon[i - 1][xy.y].getCheckOn())
                break;
            removeSamePokemonInList(new XY(i, xy.y));
            pokemon[i][xy.y].setKind(pokemon[i - 1][xy.y].getKind());
            samePokemon[pokemon[i][xy.y].getKind()].list.add(pokemon[i][xy.y]
                    .getXY());
        }
        pokemon[xy.x][xy.y].show();
        removeSamePokemonInList(new XY(i, xy.y));
        return new XY(i, xy.y);
    }

    public boolean checkStopedThread() {
        if (!showPokemon && !runSetTime && !drawHelp && timeCountDown == 00)
            return true;
        return false;
    }

    public void pauseThread() {
        showPokemon = false;
        timeCountDown = 0;
        runSetTime = false;
        drawHelp = false;
    }

    private void continueThreadSetTime() {
        // if (!setTimeStop) {
        // runSetTime = true;
        // return;
        // }
        // if (timeRemaining > 0)
        // Log.i(TAG, "Continue thread");
        runThreadSetTime();
    }

    public void setGameLevel() {
        gameLevel = 1;
    }

    private boolean checkCanReturn;

    public boolean checkWayGoing(int pokemon1ID, int pokemon2ID) {
        checkCanReturn = false;
        if (!pokemon[pokemon1ID / 10][pokemon1ID % 10].getCheckOn()
                || !pokemon[pokemon2ID / 10][pokemon2ID % 10].getCheckOn()) {
            return false; // pokemon mat 1 cach bi an
        }
        wayHorizontal = new WayGoing();
        wayVertical = new WayGoing();
        if (pokemon2ID / 10 >= pokemon1ID / 10) {
            checkX1 = pokemon1ID / 10;
            checkY1 = pokemon1ID % 10;
            checkX2 = pokemon2ID / 10;
            checkY2 = pokemon2ID % 10;
        } else {
            checkX2 = pokemon1ID / 10;
            checkY2 = pokemon1ID % 10;
            checkX1 = pokemon2ID / 10;
            checkY1 = pokemon2ID % 10;
        }
        wayVertical = checkVertical(pokemon1ID, pokemon2ID);
        try {
            wayVertical.setP1(new XY(pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getXPath(), pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getYPath()));
            wayVertical.setP4(new XY(pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getXPath(), pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getYPath()));
        } catch (NullPointerException e) {
        }
        if (checkCanReturn) {
            way = wayVertical;
            return true;
        }
        if (pokemon2ID % 10 >= pokemon1ID % 10) {
            checkX1 = pokemon1ID / 10;
            checkY1 = pokemon1ID % 10;
            checkX2 = pokemon2ID / 10;
            checkY2 = pokemon2ID % 10;
        } else {
            checkX2 = pokemon1ID / 10;
            checkY2 = pokemon1ID % 10;
            checkX1 = pokemon2ID / 10;
            checkY1 = pokemon2ID % 10;
        }
        wayHorizontal = checkHorizontal(pokemon1ID, pokemon2ID);
        try {
            wayHorizontal.setP1(new XY(
                    pokemon[pokemon1ID / 10][pokemon1ID % 10].getXPath(),
                    pokemon[pokemon1ID / 10][pokemon1ID % 10].getYPath()));
            wayHorizontal.setP4(new XY(
                    pokemon[pokemon2ID / 10][pokemon2ID % 10].getXPath(),
                    pokemon[pokemon2ID / 10][pokemon2ID % 10].getYPath()));
        } catch (NullPointerException e) {
        }

        if (wayHorizontal == null && wayVertical == null) {
            return false;
        } else if (wayHorizontal != null && wayVertical == null) {
            way = wayHorizontal;
        } else if (wayHorizontal == null && wayVertical != null) {
            way = wayVertical;
        } else {
            if (wayHorizontal.equals(wayVertical)) {
                way = wayVertical;
            } else
                way = wayHorizontal;
        }
        return true;
    }

    public WayGoing checkHorizontal(int pokemon1ID, int pokemon2ID) {
        boolean checkTrue;
        int z1, z2;
        if (checkX1 > checkX2) {
            z2 = checkX1;
            z1 = checkX2;
        } else {
            z2 = checkX2;
            z1 = checkX1;
        }
        if (checkY1 == checkY2) {
            checkTrue = true;
            for (int i = z1 + 1; i < z2; i++) {
                if (pokemon[i][checkY1].getCheckOn()) {

                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                wayHorizontal.setP2AndP3(new XY(
                                pokemon[idValue / 10][idValue % 10].getXPath(),
                                pokemon[idValue / 10][idValue % 10].getYPath()),
                        new XY(pokemon[saveID / 10][saveID % 10].getXPath(),
                                pokemon[saveID / 10][saveID % 10].getYPath()));
                return wayHorizontal;
            }
        }
        int i, j;
        for (i = checkY1; i < checkY2; ) {
            if (!pokemon[checkX1][i + 1].getCheckOn()) {
                i++;
            } else
                break;

        }
        for (j = checkY2; j > checkY1; ) {
            if (!pokemon[checkX2][j - 1].getCheckOn()) {
                j--;
            } else
                break;
        }
        if (i < j)
            return null;
        for (int k = j; k <= i; k++) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[l][k].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                wayHorizontal.setP2AndP3(
                        new XY(pokemon[z1][k].getXPath(), pokemon[z1][k]
                                .getYPath()),
                        new XY(pokemon[z2][k].getXPath(), pokemon[z2][k]
                                .getYPath()));
                return wayHorizontal;
            }
        }
        WayGoing wayBot = null, wayTop = null;
        if (i == checkY2) {
            wayBot = checkHorizontalBot(z1, z2);
        }
        if (j == checkY1) {
            wayTop = checkHorizontalTop(z1, z2);
        }
        try {
            wayBot.setP1(new XY(pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getXPath(), pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getYPath()));
            wayBot.setP4(new XY(pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getXPath(), pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getYPath()));
        } catch (NullPointerException e) {
        }
        try {
            wayTop.setP1(new XY(pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getXPath(), pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getYPath()));
            wayTop.setP4(new XY(pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getXPath(), pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getYPath()));
        } catch (NullPointerException e) {
        }
        if (wayBot == null && wayTop == null)
            return null;

        else if (wayBot == null && wayTop != null)
            return wayTop;
        else if (wayBot != null && wayTop == null)
            return wayBot;
        else if (wayBot.equals(wayTop))
            return wayTop;
        else
            return wayBot;

    }

    public WayGoing checkHorizontalBot(int z1, int z2) {
        boolean checkTrue = true;
        WayGoing wayHorizontalBot = new WayGoing();
        for (int k = checkY2 + 1; k < NUMBER_POKEMON_VERTICAL + 2; k++) {
            if (pokemon[checkX1][k].getCheckOn()
                    || pokemon[checkX2][k].getCheckOn()) {
                break;
            } else {
                checkTrue = true;
                for (int l = z1 + 1; l < z2; l++) {
                    if (pokemon[l][k].getCheckOn()) {
                        checkTrue = false;
                        break;
                    }
                }
                if (checkTrue) {

                    wayHorizontalBot.setP2AndP3(
                            new XY(pokemon[z1][k].getXPath(), pokemon[z1][k]
                                    .getYPath()),
                            new XY(pokemon[z2][k].getXPath(), pokemon[z2][k]
                                    .getYPath()));
                    return wayHorizontalBot;
                }
            }
        }
        return null;
    }

    public WayGoing checkHorizontalTop(int z1, int z2) {
        boolean checkTrue = true;
        WayGoing wayHorizontalTop = new WayGoing();
        for (int k = checkY1 - 1; k >= 0; k--) {
            if (pokemon[checkX1][k].getCheckOn()
                    || pokemon[checkX2][k].getCheckOn()) {
                break;
            } else {
                checkTrue = true;
                for (int l = z1 + 1; l < z2; l++) {
                    if (pokemon[l][k].getCheckOn()) {
                        checkTrue = false;
                        break;
                    }
                }
                if (checkTrue) {
                    wayHorizontalTop.setP2AndP3(
                            new XY(pokemon[z1][k].getXPath(), pokemon[z1][k]
                                    .getYPath()),
                            new XY(pokemon[z2][k].getXPath(), pokemon[z2][k]
                                    .getYPath()));
                    return wayHorizontalTop;
                }
            }
        }
        return null;
    }

    public WayGoing checkVertical(int pokemon1ID, int pokemon2ID) // dá»�c
    {
        boolean checkTrue;
        int z1, z2;
        if (checkY1 > checkY2) {
            z2 = checkY1;
            z1 = checkY2;
        } else {
            z2 = checkY2;
            z1 = checkY1;
        }
        if (checkX1 == checkX2) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[checkX1][l].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                if (checkTrue) {
                    wayVertical.setP2AndP3(
                            new XY(pokemon[idValue / 10][idValue % 10]
                                    .getXPath(),
                                    pokemon[idValue / 10][idValue % 10]
                                            .getYPath()),
                            new XY(
                                    pokemon[saveID / 10][saveID % 10]
                                            .getXPath(),
                                    pokemon[saveID / 10][saveID % 10]
                                            .getYPath()));
                    checkCanReturn = true;
                    return wayVertical;
                }
            }
        }
        int i;
        for (i = checkX1; i < checkX2; ) {
            if (!pokemon[i + 1][checkY1].getCheckOn()) {
                i++;
            } else
                break;

        }
        int j;
        for (j = checkX2; j > checkX1; ) {
            if (!pokemon[j - 1][checkY2].getCheckOn()) {
                j--;
            } else
                break;

        }

        if (i < j)
            return null;

        for (int k = j; k <= i; k++) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[k][l].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                wayVertical.setP2AndP3(
                        new XY(pokemon[k][z1].getXPath(), pokemon[k][z1]
                                .getYPath()),
                        new XY(pokemon[k][z2].getXPath(), pokemon[k][z2]
                                .getYPath()));
                checkCanReturn = true;
                return wayVertical;
            }

        }

        WayGoing wayRight = null, wayLeft = null;

        if (i == checkX2) {
            wayRight = checkVerticalRight(z1, z2);
        }
        if (j == checkX1) {

            wayLeft = checkVerticalLeft(z1, z2);
        }
        try {
            wayRight.setP1(new XY(pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getXPath(), pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getYPath()));
            wayRight.setP4(new XY(pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getXPath(), pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getYPath()));
        } catch (NullPointerException e) {
        }
        try {
            wayLeft.setP1(new XY(pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getXPath(), pokemon[pokemon1ID / 10][pokemon1ID % 10]
                    .getYPath()));
            wayLeft.setP4(new XY(pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getXPath(), pokemon[pokemon2ID / 10][pokemon2ID % 10]
                    .getYPath()));
        } catch (NullPointerException e) {
        }
        if (wayLeft == null && wayRight == null)
            return null;
        else if (wayLeft == null && wayRight != null)
            return wayRight;
        else if (wayLeft != null && wayRight == null)
            return wayLeft;
        else if (wayRight.equals(wayLeft))
            return wayLeft;
        else
            return wayRight;

    }

    public WayGoing checkVerticalLeft(int z1, int z2) {
        boolean checkTrue;
        WayGoing wayVerticalLeft = new WayGoing();
        for (int l = checkX1 - 1; l >= 0; l--) {
            if (pokemon[l][checkY1].getCheckOn()
                    || pokemon[l][checkY2].getCheckOn())
                break;
            else {
                checkTrue = true;
                for (int m = z1 + 1; m < z2; m++) {
                    if (pokemon[l][m].getCheckOn()) {
                        checkTrue = false;
                        break;
                    }
                }
                if (checkTrue) {
                    wayVerticalLeft.setP2AndP3(
                            new XY(pokemon[l][z1].getXPath(), pokemon[l][z1]
                                    .getYPath()),
                            new XY(pokemon[l][z2].getXPath(), pokemon[l][z2]
                                    .getYPath()));
                    return wayVerticalLeft;
                }
            }
        }

        return null;
    }

    public WayGoing checkVerticalRight(int z1, int z2) {
        boolean checkTrue;
        WayGoing wayVerticalRight = new WayGoing();
        for (int l = checkX2 + 1; l < NUMBER_POKEMON_HORIZOLTAL + 2; l++) {
            if (pokemon[l][checkY1].getCheckOn()
                    || pokemon[l][checkY2].getCheckOn())
                break;
            else {
                checkTrue = true;
                for (int m = z1 + 1; m < z2; m++) {
                    if (pokemon[l][m].getCheckOn()) {
                        checkTrue = false;
                        break;
                    }
                }
                if (checkTrue) {

                    wayVerticalRight.setP2AndP3(
                            new XY(pokemon[l][z1].getXPath(), pokemon[l][z1]
                                    .getYPath()),
                            new XY(pokemon[l][z2].getXPath(), pokemon[l][z2]
                                    .getYPath()));
                    return wayVerticalRight;
                }
            }
        }
        return null;
    }

    public boolean checkStillWay(int pokemon1ID, int pokemon2ID) {
        if (!pokemon[pokemon1ID / 10][pokemon1ID % 10].getCheckOn()
                || !pokemon[pokemon2ID / 10][pokemon2ID % 10].getCheckOn()) {
            return false; // pokemon mat 1 cach bi an
        }
        wayHorizontal = new WayGoing();
        wayVertical = new WayGoing();
        if (pokemon2ID / 10 >= pokemon1ID / 10) {
            checkX1 = pokemon1ID / 10;
            checkY1 = pokemon1ID % 10;
            checkX2 = pokemon2ID / 10;
            checkY2 = pokemon2ID % 10;
        } else {
            checkX2 = pokemon1ID / 10;
            checkY2 = pokemon1ID % 10;
            checkX1 = pokemon2ID / 10;
            checkY1 = pokemon2ID % 10;
        }
        if (checkStillWayVertical())
            return true;
        if (pokemon2ID % 10 >= pokemon1ID % 10) {
            checkX1 = pokemon1ID / 10;
            checkY1 = pokemon1ID % 10;
            checkX2 = pokemon2ID / 10;
            checkY2 = pokemon2ID % 10;
        } else {
            checkX2 = pokemon1ID / 10;
            checkY2 = pokemon1ID % 10;
            checkX1 = pokemon2ID / 10;
            checkY1 = pokemon2ID % 10;
        }
        if (checkStillWayHorizontal())
            return true;
        return false;
    }

    public boolean checkStillWayHorizontal() {
        boolean checkTrue;
        int z1, z2;
        if (checkX1 > checkX2) {
            z2 = checkX1;
            z1 = checkX2;
        } else {
            z2 = checkX2;
            z1 = checkX1;
        }
        if (checkY1 == checkY2) {
            checkTrue = true;
            for (int i = z1 + 1; i < z2; i++) {
                if (pokemon[i][checkY1].getCheckOn()) {

                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                return true;
            }
        }
        int i, j;
        for (i = checkY1; i < checkY2; ) {
            if (!pokemon[checkX1][i + 1].getCheckOn()) {
                i++;
            } else
                break;

        }
        for (j = checkY2; j > checkY1; ) {
            if (!pokemon[checkX2][j - 1].getCheckOn()) {
                j--;
            } else
                break;
        }
        if (i < j)
            return false;
        for (int k = j; k <= i; k++) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[l][k].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                return true;
            }

        }
        if (i == checkY2) {
            for (int k = i + 1; k < NUMBER_POKEMON_VERTICAL + 2; k++) {
                if (pokemon[checkX1][k].getCheckOn()
                        || pokemon[checkX2][k].getCheckOn()) {
                    break;
                } else {
                    checkTrue = true;
                    for (int l = z1 + 1; l < z2; l++) {
                        if (pokemon[l][k].getCheckOn()) {
                            checkTrue = false;
                            break;
                        }
                    }
                    if (checkTrue) {

                        return true;
                    }
                }
            }
        }
        if (j == checkY1) {
            for (int k = j - 1; k >= 0; k--) {
                if (pokemon[checkX1][k].getCheckOn()
                        || pokemon[checkX2][k].getCheckOn()) {
                    break;
                } else {
                    checkTrue = true;
                    for (int l = z1 + 1; l < z2; l++) {
                        if (pokemon[l][k].getCheckOn()) {
                            checkTrue = false;
                            break;
                        }
                    }
                    if (checkTrue) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkStillWayVertical() {
        boolean checkTrue;
        int z1, z2;
        if (checkY1 > checkY2) {
            z2 = checkY1;
            z1 = checkY2;
        } else {
            z2 = checkY2;
            z1 = checkY1;
        }
        if (checkX1 == checkX2) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[checkX1][l].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                return true;
            }
        }
        int i;
        for (i = checkX1; i < checkX2; ) {
            if (!pokemon[i + 1][checkY1].getCheckOn()) {
                i++;
            } else
                break;

        }
        int j;
        for (j = checkX2; j > checkX1; ) {
            if (!pokemon[j - 1][checkY2].getCheckOn()) {
                j--;
            } else
                break;

        }

        if (i < j)
            return false;

        for (int k = j; k <= i; k++) {
            checkTrue = true;
            for (int l = z1 + 1; l < z2; l++) {
                if (pokemon[k][l].getCheckOn()) {
                    checkTrue = false;
                    break;
                }
            }
            if (checkTrue) {
                return true;
            }

        }

        if (i == checkX2) {
            for (int l = checkX2 + 1; l < NUMBER_POKEMON_HORIZOLTAL + 2; l++) {
                if (pokemon[l][checkY1].getCheckOn()
                        || pokemon[l][checkY2].getCheckOn())
                    break;
                else {
                    checkTrue = true;
                    for (int m = z1 + 1; m < z2; m++) {
                        if (pokemon[l][m].getCheckOn()) {
                            checkTrue = false;
                            break;
                        }
                    }
                    if (checkTrue) {

                        return true;
                    }
                }
            }
        }
        if (j == checkX1) {
            for (int l = checkX1 - 1; l >= 0; l--) {
                if (pokemon[l][checkY1].getCheckOn()
                        || pokemon[l][checkY2].getCheckOn())
                    break;
                else {
                    checkTrue = true;
                    for (int m = z1 + 1; m < z2; m++) {
                        if (pokemon[l][m].getCheckOn()) {
                            checkTrue = false;
                            break;
                        }
                    }
                    if (checkTrue) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void pauseGame() {
        pauseThread();
        hideAllPokemonTemporary();
        showOptionsDialog();

    }

    public void hideAllPokemonTemporary() {
        for (int i = 1; i < NUMBER_POKEMON_HORIZOLTAL + 1; i++) {
            for (int j = 1; j < NUMBER_POKEMON_VERTICAL + 1; j++)
                pokemon[i][j].hideTemporary();
        }
        myFrame.setVisibility(GONE);

    }

    public void showOptionsDialog() {
        dialogOptions.show();

    }

    public void showPokemonToContinueGame() {
        Log.i(TAG, "showPokemonToContinueGame");
        for (int i = 1; i < NUMBER_POKEMON_HORIZOLTAL + 1; i++) {
            for (int j = 0; j < NUMBER_POKEMON_VERTICAL + 1; j++)
                pokemon[i][j].showToContinue();
        }
        myFrame.setVisibility(VISIBLE);
        continueThreadSetTime();
    }

    private void setTimeOfLevel() {
        timeOfLevel = (gameLevel + 5) * 20 + 1;

        if (timeOfLevel > 300)
            timeOfLevel = 300;
    }

    private GameData dataGame;
    private boolean replay;

    public void saveGameData() {
        if (!canContinueGame) {
            editor.putBoolean(GameData.CONTINUEGAME, false);
            return;
        }
        myFrame.setVisibility(GONE);
        editor.putBoolean(GameData.CONTINUEGAME, true);
        editor.putBoolean(GameData.CLASSIC_MODE, CLASSIC_MODE);
        editor.putInt(dataGame.indexBackGround, indexBackGround);
        editor.putInt(dataGame.indexBackGroundPokemon, indexBackGroundPokemon);

        for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
            for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                editor.putBoolean(dataGame.checkOn[i][j],
                        pokemon[i][j].getCheckOn());
                editor.putInt(dataGame.idImage[i][j], pokemon[i][j].getKind());
            }
        }
        editor.putInt(dataGame.timeRemaining, timeRemaining);
        editor.putInt(dataGame.helpRemaining, helpRemaning);
        editor.putInt(dataGame.level, gameLevel);
        editor.putInt(dataGame.score, score);
        editor.putInt(dataGame.live, live);
        editor.putBoolean(dataGame.replay, replay);
        editor.apply();
    }

    public void loadGameData() throws ArrayIndexOutOfBoundsException {
        Log.i(TAG, "loadGameData() throws ArrayIndexOutOfBoundsException");
        if (!preferencesSaveGamePlay.getBoolean(GameData.CONTINUEGAME, false))
            return;
        myFrame.setVisibility(View.GONE);
        CLASSIC_MODE = preferencesSaveGamePlay.getBoolean(GameData.CLASSIC_MODE, false);
        if (CLASSIC_MODE)
            btnHelp.setVisibility(GONE);
        numberPokemon = 0;
        int imageID;
        replay = preferencesSaveGamePlay.getBoolean(dataGame.replay, false);
        live = preferencesSaveGamePlay.getInt(dataGame.live, 10);
        indexBackGround = preferencesSaveGamePlay.getInt(dataGame.indexBackGround, 0);
        indexBackGroundPokemon = preferencesSaveGamePlay.getInt(
                dataGame.indexBackGroundPokemon, 0);
        setNewBackGround();
        timeRemaining = preferencesSaveGamePlay.getInt(dataGame.timeRemaining, 0) + 1;
        helpRemaning = preferencesSaveGamePlay.getInt(dataGame.helpRemaining, 0);
        gameLevel = preferencesSaveGamePlay.getInt(dataGame.level, 1);
        setNumberPokemonOfLevel();
        setTimeOfLevel();
        samePokemon = new ListXY[numberPokemonOfLevel];
        for (int i = 0; i < samePokemon.length; i++) {
            samePokemon[i] = new ListXY();
        }
        for (int i = 1; i <= NUMBER_POKEMON_HORIZOLTAL; i++) {
            for (int j = 1; j <= NUMBER_POKEMON_VERTICAL; j++) {
                if (!preferencesSaveGamePlay.getBoolean(dataGame.checkOn[i][j], false)) {
                    pokemon[i][j].hide();
                } else {
                    imageID = preferencesSaveGamePlay.getInt(dataGame.idImage[i][j], 1);
                    pokemon[i][j].setNewImage(imageID);
                    samePokemon[imageID].list.add(pokemon[i][j].getXY());
                    numberPokemon++;
                }
            }
        }
        score = preferencesSaveGamePlay.getInt(dataGame.score, 0);
        textViewScore.setText("Score: " + score);
        if (helpRemaning == 0)
            btnHelp.setBackgroundResource(R.drawable.help12);
        setTime();
        updateLevelAndLive();
        if (numberPokemon == 0) {
            win();
            return;
        }
        checkStillTheWay(false);
    }

    public void continueGame() {
        canContinueGame = true;
        showPokemon = true;
        runThreadDrawTimeCountDown();
    }

    private void replayGameLevel() {
        canContinueGame = true;
        live = 3;
        score = score / 2;
        updateScore(0);
        setPokemonImage();
        showPokemon = true;
        runThreadShowPokemon();
    }

}
