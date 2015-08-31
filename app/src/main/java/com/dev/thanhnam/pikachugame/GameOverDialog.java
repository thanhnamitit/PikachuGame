package com.dev.thanhnam.pikachugame;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.Window;


public class GameOverDialog extends Dialog implements OnCancelListener {
    private Handler handler;

    public GameOverDialog(Context context, Handler handler) {
        super(context);
        this.handler = handler;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over_dialog);
        setOnCancelListener(this);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub
        handler.sendEmptyMessage(ControllerActivity.GAME_OVER);
    }

}
