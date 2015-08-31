package com.dev.thanhnam.pikachugame;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogWhenCreateNewGame extends Dialog implements
		android.view.View.OnClickListener {
	private Handler handler;
	private Button btnYes, btnNo;
	private boolean classicMode;

	public DialogWhenCreateNewGame(Context context, Handler handler) {
		super(context);
		this.handler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_new_game_dialog);
		btnYes = (Button) findViewById(R.id.yesButtonNewGame);
		btnNo = (Button) findViewById(R.id.noButtonNewGame);
		btnNo.setOnClickListener(this);
		btnYes.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.noButtonNewGame) {
			hide();

		} else {
			if (classicMode) {
				handler.sendEmptyMessage(ControllerActivity.CREATE_NEW_CLASSIC_GAME);
			} else {
				handler.sendEmptyMessage(ControllerActivity.CREATE_NEW_MODERN_GAME);
			}
			hide();
		}
	}

	public void show(boolean mode) {
		this.classicMode = mode;
		show();
	}

}
