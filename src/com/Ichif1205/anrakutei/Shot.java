package com.Ichif1205.anrakutei;

import android.graphics.RectF;

public class Shot {

	private float posX;
	private float posY;
	private int width = 5;
	private int height = 20;
	private int speed = 20;

	Shot(float x, float y) {
		posX = x;
		posY = y;
	}

	public float getShotPosX() {
		return posX;
	}

	public float getShotPosY() {
		return posY;
	}

	public int getShotWidth() {
		return width;
	}

	public int getShotHeight() {
		return height;
	}

	public int getShotSpeed() {
		return speed;
	}

	public void setShotPosX(float x) {
		posX = x;
	}

	public void setShotPosY(float y) {
		posY = y;
	}

	public void setShotWidth(int w) {
		width = w;
	}

	public void setShotHeight(int h) {
		height = h;
	}

	public void updatePosition() {
		posY -= speed;
	}

	public RectF createRectangle(int x) {
		// itemBをとった場合
		if(x == 1){
			width = 15;
		}

		RectF rectf = new RectF(posX - width / 2, posY - height / 2, posX
				+ width / 2, posY + height / 2);
		return rectf;
	}

	public void remove() {
		posY = 0;
	}

}
