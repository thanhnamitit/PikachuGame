package com.dev.thanhnam.pikachugame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class WayGoing {
	private XY p1, p2, p3, p4;
	private int lengthOfWay;

	public WayGoing() {
		p1 = new XY();
		p2 = new XY();
		p3 = new XY();
		p4 = new XY();
	}

	public WayGoing(WayGoing way) {
		p1 = way.getP1().clone();
		p2 = way.getP2().clone();
		p3 = way.getP3().clone();
		p4 = way.getP4().clone();
	}

	public XY getP1() {
		return p1;
	}

	public void setP1(XY p1) {
		this.p1 = p1;
	}

	public XY getP2() {
		return p2;
	}

	public void setP2(XY p2) {
		this.p2 = p2.clone();
	}

	public XY getP3() {
		return p3;
	}

	public void setP3(XY p3) {
		this.p3 = p3.clone();
	}

	public XY getP4() {
		return p4;
	}

	public void setP4(XY p4) {
		this.p4 = p4.clone();
	}

	public void setP2AndP3(XY p2, XY p3) {
		this.p2 = p2.clone();
		this.p3 = p3.clone();
	}

	public int lengthOfWay() {

		if ((p1.x == p2.x || p1.y == p2.y) && (p4.x == p2.x || p4.y == p2.y)) {
			lengthOfWay = Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
			lengthOfWay += (Math.abs(p4.x - p2.x) + Math.abs(p4.y - p2.y));
		} else if ((p1.x == p3.x || p1.y == p3.y)
				&& (p4.x == p3.x || p4.y == p3.y)) {
			lengthOfWay = Math.abs(p3.x - p1.x) + Math.abs(p3.y - p1.y);
			lengthOfWay += (Math.abs(p4.x - p3.x) + Math.abs(p4.y - p3.y));
		} else if (p1.x == p2.x || p1.y == p2.y) {
			lengthOfWay = Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
			lengthOfWay += (Math.abs(p3.x - p2.x) + Math.abs(p3.y - p2.y));
			lengthOfWay += (Math.abs(p4.x - p3.x) + Math.abs(p4.y - p3.y));
		} else {
			lengthOfWay = Math.abs(p3.x - p1.x) + Math.abs(p3.y - p1.y);
			lengthOfWay += (Math.abs(p3.x - p2.x) + Math.abs(p3.y - p2.y));
			lengthOfWay += (Math.abs(p4.x - p2.x) + Math.abs(p4.y - p2.y));
		}
		return lengthOfWay;
	}

	public void drawWay(Canvas canvas, Paint paint) {
		// canvas.drawBitmap(bm, matrix, paint);
		if ((p1.x == p2.x || p1.y == p2.y) && (p4.x == p2.x || p4.y == p2.y)) {
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
			canvas.drawLine(p2.x, p2.y, p4.x, p4.y, paint);
		} else if ((p1.x == p3.x || p1.y == p3.y)
				&& (p4.x == p3.x || p4.y == p3.y)) {
			canvas.drawLine(p1.x, p1.y, p3.x, p3.y, paint);
			canvas.drawLine(p3.x, p3.y, p4.x, p4.y, paint);
		} else if (p1.x == p2.x || p1.y == p2.y) {
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
			canvas.drawLine(p2.x, p2.y, p3.x, p3.y, paint);
			canvas.drawLine(p3.x, p3.y, p4.x, p4.y, paint);
		} else {
			canvas.drawLine(p1.x, p1.y, p3.x, p3.y, paint);
			canvas.drawLine(p2.x, p2.y, p3.x, p3.y, paint);
			canvas.drawLine(p2.x, p2.y, p4.x, p4.y, paint);
		}
	}

	public void drawHorizontal(Canvas canvas, Paint paint, XY xy1, XY xy2) {

	}

	public boolean equals(WayGoing other) {
		if (lengthOfWay() >= other.lengthOfWay())
			return true;
		return false;
	}

}
