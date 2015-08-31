package com.dev.thanhnam.pikachugame;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ReplayDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Button btnYes, btnNo;
    private Handler handlerOfGamePlayClass, handlerOfLayoutPlay;

    public ReplayDialog(Context context, Handler handlerOfGamePlayClass,
                        Handler handlerOfLayoutPlay) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_replay);
        this.handlerOfGamePlayClass = handlerOfGamePlayClass;
        this.handlerOfLayoutPlay = handlerOfLayoutPlay;
        btnYes = (Button) findViewById(R.id.yesButtonReplay);
        btnNo = (Button) findViewById(R.id.noButtonReplay);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yesButtonReplay) {
            handlerOfGamePlayClass.sendEmptyMessage(GamePlay.REPLAY_THIS_LEVEL);
        } else {
            handlerOfLayoutPlay.sendEmptyMessage(ControllerActivity.GAME_OVER);
        }
        hide();
    }

}
