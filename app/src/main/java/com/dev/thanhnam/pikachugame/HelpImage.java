package com.dev.thanhnam.pikachugame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class HelpImage {
	private Bitmap helpImage;
	private Matrix matrix;
	private Context context;
	private int angel;
	private float wScale, hScale;

	public HelpImage(Context context) {
		angel = 0;
		this.context = context;
		initBitmap();
		// TODO Auto-generated constructor stub
	}

	public void initBitmap() {

		helpImage = BitmapFactory.decodeResource(context.getResources(),
				GameResources.helpImageID);
		wScale = Pokemon.IMAGE_SIZE / (float) helpImage.getWidth();
		hScale = Pokemon.IMAGE_SIZE / (float) helpImage.getHeight();

	}

	public void drawImage(Canvas v, Paint p, XY xy) {
		p.setAlpha(200);
		matrix = new Matrix();
		matrix.postScale(wScale, hScale);
		matrix.postRotate(angel += 3,
				(int) (helpImage.getWidth() * wScale / 2), (int) (hScale
						* helpImage.getHeight() / 2));
		if (angel == 360)
			angel = 0;
		matrix.postTranslate(xy.x, xy.y);
		v.drawBitmap(helpImage, matrix, p);
	}

}
