package com.dev.thanhnam.pikachugame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class DrawTimeCountDown {
    private Bitmap[] number;
    public static int[] ID_NUMBER = new int[]{R.drawable.go};
    // R.drawable.one, R.drawable.two, R.drawable.three};
    private Float[] scale;
    private Float[] valueSubScale;
    private Matrix matrix;

    public DrawTimeCountDown(Context context) {
        number = new Bitmap[ID_NUMBER.length];
        scale = new Float[ID_NUMBER.length];
        valueSubScale = new Float[ID_NUMBER.length];
        for (int i = 0; i < ID_NUMBER.length; i++) {
            number[i] = BitmapFactory.decodeResource(context.getResources(),
                    ID_NUMBER[i]);
        }
        setScale();
        for (int i = 0; i < ID_NUMBER.length; i++) {

            valueSubScale[i] = scale[i] / 100;
        }
    }

    public void setScale() {

        for (int i = 0; i < ID_NUMBER.length; i++) {

            scale[i] = Pokemon.IMAGE_SIZE * 6 / (float) number[i].getWidth();
        }
    }

    public void drawNumber(Canvas c, Paint p, int index) {
        matrix = new Matrix();

        matrix.postScale(scale[index] -= valueSubScale[index], scale[index]);
        matrix.postTranslate(
                (ControllerActivity.WIDTH_SCREEN - number[index].getWidth()
                        * scale[index]) / 2,
                (ControllerActivity.HEIGHT_SCREEN - number[index].getHeight()
                        * scale[index]) / 2);
        c.drawBitmap(number[index], matrix, p);

    }
}
