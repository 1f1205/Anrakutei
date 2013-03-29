package com.Ichif1205.anrakutei;

import java.util.Random;

import android.graphics.RectF;
import android.util.FloatMath;

public class Item {

	private float posX;
	private float posY;
	private int width = 5;
	private int height = 20;
	private int speed = 4;
	private int pattern;
	private float centerX;
	private float dy;
	private String select_item;
	private int item_pattern;

	Item(float x, float y) {
		posX = x;
		posY = y;
		Random ptn_rand = new Random();
		pattern = ptn_rand.nextInt(4);
		if (pattern >= 1) {
			centerX = posX;
		}
	}

	public float getItemPosX() {
		return posX;
	}

	public float getItemPosY() {
		return posY;
	}

	public int getItemWidth() {
		return width;
	}

	public int getItemHeight() {
		return height;
	}

	public int getItemSpeed() {
		return speed;
	}

	public void setItemPosX(float x) {
		posX = x;
	}

	public void setItemPosY(float y) {
		posY = y;
	}

	public void setItemWidth(int w) {
		width = w;
	}

	public void setItemHeight(int h) {
		height = h;
	}

	public boolean isInsideScreen(int windowHeight) {
		if (posY <= windowHeight && posY >= 0) {
			return true;
		}
		return false;
	}

	public String selectItem() {
		Random ptn_rand = new Random();
		item_pattern = ptn_rand.nextInt(1);
		if (pattern == 0) {
			select_item = "M";
		} else if (pattern == 1) {
			select_item = "B";
		}
		return select_item;
	}

	public void updatePosition() {
		// patternによって移動パターン決定
		if (pattern == 0) {
			posY += speed;
		} else if (pattern == 1) {
			posY += speed;
			posX = 60 * FloatMath.cos(posY / 60) + centerX;
		} else if (pattern == 2) {
			posY += speed;
			dy += speed * (float) Math.tan(Math.PI / 9);
			posX = dy + centerX;
		} else {
			posY += speed;
			dy += speed * (float) Math.tan(Math.PI / 9);
			posX = -dy + centerX;
		}
	}

	public RectF createRectangle() {
		RectF rectf = new RectF(posX - width / 2, posY - height / 2, posX
				+ width / 2, posY + height / 2);
		return rectf;
	}

	public void remove() {
		posY = -1;
	}
}
