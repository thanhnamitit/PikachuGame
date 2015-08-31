package com.dev.thanhnam.pikachugame;

public class XY {
	public int x = 0;
	public int y = 1;

	public XY() {
	}

	public XY(int id) {
		x = id / 10;
		y = id % 10;
	}

	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public XY clone() {
		return new XY(x, y);
	}

	public boolean equals(XY xy) {
		return this.x == xy.x && this.y == xy.y;
	}
}
