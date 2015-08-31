package com.dev.thanhnam.pikachugame;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.EditText;

public class HighScoreDialog extends Dialog implements OnCancelListener {
    private Handler handlerOfActivity;
    private EditText edit;
    private int score;
    private boolean classicMode;

    public void show(int score, boolean classicMode) {
        this.classicMode = classicMode;
        this.score = score;
        edit.setText("");
        show();
    }

    public HighScoreDialog(Context context, Handler handler) {
        super(context);
        this.handlerOfActivity = handler;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_high_score);
        edit = (EditText) findViewById(R.id.editTextHighScore);
        setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (edit.getText().toString().trim().equals("")) {
            show();
            return;
        }

        Message message = new Message();
        message.obj = edit.getText().toString();
        message.arg1 = score;
        if (classicMode)
            message.what = ControllerActivity.UPDATE_NEW_CLASSIC_HIGH_SCORE;
        else {
            message.what = ControllerActivity.UPDATE_NEW_MODERN_HIGH_SCORE;
        }
        message.setTarget(handlerOfActivity);
        message.sendToTarget();
        hide();
    }

}
