package com.dev.thanhnam.pikachugame;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OpionsDialog extends Dialog implements OnCancelListener,
        OnCheckedChangeListener, OnSeekBarChangeListener {
    private static final String TAG = "DialogOpions";

    private SeekBar seekBarSound, seekBarMusic;

    private Handler handlerOfActivity;
    private Handler handlerOfGamePlay;
    private Message message;

    private int soundVolume, musicVolume;

    private CheckBox checkBoxSound, checkBoxMusic;

    public OpionsDialog(Context context, Handler handlerOfActivity, Handler handlerOfGamePlay, int soundVolume,
                        int musicVolume) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setOnCancelListener(this);
        this.soundVolume = soundVolume;
        this.musicVolume = musicVolume;
        this.handlerOfActivity = handlerOfActivity;
        this.handlerOfGamePlay = handlerOfGamePlay;
        setContentView(R.layout.options_dialog);
        initCheckBox();
        initSeekBar();
        setOnChangedCheckedforCheckBox();

    }

    private void initCheckBox() {
        checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxMusic = (CheckBox) findViewById(R.id.checkBoxMusic);
    }

    private void initSeekBar() {
        seekBarSound = (SeekBar) findViewById(R.id.seeBarSound);
        seekBarMusic = (SeekBar) findViewById(R.id.seeBarMusic);
        seekBarSound.setOnSeekBarChangeListener(this);
        seekBarMusic.setOnSeekBarChangeListener(this);

        seekBarSound.setProgress(soundVolume);
        seekBarMusic.setProgress(musicVolume);

    }

    private void setOnChangedCheckedforCheckBox() {
        checkBoxSound.setOnCheckedChangeListener(this);
        checkBoxMusic.setOnCheckedChangeListener(this);

    }

    private void resetOnChangedCheckedforCheckBox() {
        checkBoxSound.setOnCheckedChangeListener(null);
        checkBoxMusic.setOnCheckedChangeListener(null);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        ControllerActivity.sound.setVolume((float) seekBarSound.getProgress()
                / seekBarSound.getMax());
        Log.i(TAG, "onCancel(DialogInterface dialog)");
        handlerOfGamePlay.sendEmptyMessage(GamePlay.CONTINUE_GAME);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seeBarSound:
                if (progress == 0)
                    checkBoxSound.setChecked(false);
                else
                    checkBoxSound.setChecked(true);
                break;
            case R.id.seeBarMusic:
                if (progress == 0)
                    checkBoxMusic.setChecked(false);
                else
                    checkBoxMusic.setChecked(true);
                ControllerActivity.soundtrack.setVolume(progress
                        / (float) seekBarMusic.getMax());
                break;
            default:
                break;
        }
        upDateVolume();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        resetOnChangedCheckedforCheckBox();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setOnChangedCheckedforCheckBox();
        upDateVolume();
    }

    private void upDateVolume() {
        message = new Message();
        message.what = ControllerActivity.UPDATE_SEEBAR;
        message.arg1 = seekBarSound.getProgress();
        message.arg2 = seekBarMusic.getProgress();
        handlerOfActivity.sendMessage(message);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxSound:
                checkBoxSoundChangedChecked(isChecked);
                break;
            case R.id.checkBoxMusic:
                checkBoxMusicChangedChecked(isChecked);
                break;
            default:
                break;
        }
    }

    private void checkBoxSoundChangedChecked(boolean isChecked) {
        if (isChecked) {
            Sound.PLAY_SONUD = true;
            soundVolume = 50;
            seekBarSound.setProgress(soundVolume);
        } else {
            Sound.PLAY_SONUD = false;
            seekBarSound.setProgress(0);
        }
    }

    private void checkBoxMusicChangedChecked(boolean isChecked) {
        if (isChecked) {
            musicVolume = 50;
            seekBarMusic.setProgress(musicVolume);
        } else {
            seekBarMusic.setProgress(0);
        }
    }
}
