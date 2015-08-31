package com.dev.thanhnam.pikachugame;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class Pokemon extends ImageView {
    public static int SIZE = 24;
    public static int MARGIN_BOTTOM = 02;
    public static int IMAGE_SIZE = 1995;
    private int xDraw = 0, yDraw = 0; // draw help at this
    private int w, h;
    private int xPath, yPath;
    private int kind;
    private boolean checkOn;
    private XY xy;
    public static int backgroundID1 = GameResources.backGroundPokemonID1[0];
    public static int backgroundID2 = GameResources.backGroundPokemonID2[0];

    public static final int ROTATION = 30;

    public Pokemon(Context context, int x, int y, int w, int h) {
        super(context);
        this.xDraw = (ControllerActivity.WIDTH_SCREEN - w * SIZE) / 2 + (-1 + x)
                * SIZE;

        this.yDraw = (ControllerActivity.HEIGHT_SCREEN - MARGIN_BOTTOM)
                - (h - y + 1) * SIZE;

        xPath = this.xDraw + IMAGE_SIZE / 2;
        yPath = this.yDraw + IMAGE_SIZE / 2;

        checkOn = false;
    }

    public int getXPath() {
        return xPath;
    }

    public int getYPath() {
        return yPath;
    }

    public Pokemon(Context context, int indexImage, int w, int h) {
        super(context);
        setBackgroundResource(backgroundID1);
        setImageResource(GameResources.pokemonImageID[indexImage]);
        this.w = w;
        this.h = h;
        kind = indexImage;
        checkOn = false;
        setVisibility(View.GONE);
    }

    public void setNewImage(int indexImage) {
        setVisibility(Button.GONE);
        setImageResource(GameResources.pokemonImageID[indexImage]);
        setBackground1();
        kind = indexImage;
        checkOn = true;
    }

    public void setXY(int x, int y) {
        xy = new XY(x, y);

        this.xDraw = (ControllerActivity.WIDTH_SCREEN - w * SIZE) / 2 + (-1 + x)
                * SIZE;

        this.yDraw = (ControllerActivity.HEIGHT_SCREEN - MARGIN_BOTTOM)
                - (h - y + 1) * SIZE;

        LayoutParams lp = new LayoutParams(IMAGE_SIZE, IMAGE_SIZE);
        lp.setMargins(this.xDraw, this.yDraw, 0, 0);
        setLayoutParams(lp);
        xPath = this.xDraw + IMAGE_SIZE / 2;
        yPath = this.yDraw + IMAGE_SIZE / 2;
        setId(10 * x + y);
    }

    public boolean getCheckOn() {
        return checkOn;
    }

    public void setKind(int indexImage) {
        setImageResource(GameResources.pokemonImageID[indexImage]);
        kind = indexImage;
    }

    public int getKind() {
        return kind;
    }

    public void hide() {
        checkOn = false;
        setVisibility(View.GONE);

    }

    public void hideTemporary() {
        setVisibility(View.INVISIBLE);
    }

    public void showToContinue() {
        if (checkOn)
            setVisibility(View.VISIBLE);
    }

    public void show() {
        checkOn = true;
        setBackground1();
        setVisibility(VISIBLE);
    }

    public void setCheckOn() {
        checkOn = false;
    }

    public void setBackground1() {
        setRotation(0);
        setBackgroundResource(backgroundID1);
    }

    public void setBackground2() {
        setRotation(ROTATION);
        setBackgroundResource(backgroundID2);
    }

    public XY getXandY() {
        return new XY(xDraw, yDraw);
    }

    public void setXPathLeft() {
        xPath += IMAGE_SIZE / 3;
    }

    public void setXPathRigth() {
        xPath -= IMAGE_SIZE / 3;

    }

    public void setYPathTop() {
        yPath += IMAGE_SIZE / 3;
    }

    public void setYPathBot() {
        yPath -= IMAGE_SIZE / 3;
    }

    public XY getXY() {
        return xy;
    }

	/*
     * public void setXOfXY(int value) { xy.x += value;
	 * x+=value*StartLayout.width / (IMAGE_SIZE - 1); }
	 * 
	 * public void setYOfXY(int value) { xy.y += value; y+=StartLayout.width /
	 * (IMAGE_SIZE - 1); }
	 */

}