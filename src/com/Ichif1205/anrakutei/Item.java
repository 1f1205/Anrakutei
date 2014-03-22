package com.Ichif1205.anrakutei;

import java.util.Random;

import android.graphics.RectF;
import android.util.FloatMath;

public class Item {
	public static final int ITEM_IMAGE_WIDTH = 36;
	public static final int ITEM_IMAGE_HEIGHT = 36;
	private static final int SPEED = 4;
	private static final int width = 5;
	private static final int height = 20;

	private float posX;
	private float posY;
	private int mPattern;
	private float centerX;
	private float dy;
	private String select_item;
	private int item_pattern = -1;

	Item(float x, float y) {
		posX = x;
		posY = y;
		Random ptn_rand = new Random();
		mPattern = ptn_rand.nextInt(4);
		if (mPattern >= 1) {
			centerX = posX;
		}
	}

	public float getItemPosX() {
		return posX;
	}

	public float getItemPosY() {
		return posY;
	}

	public boolean isInsideScreen(int windowHeight) {
		if (posY <= windowHeight && posY >= 0) {
			return true;
		}
		return false;
	}

	public String selectItem() {
		if (item_pattern != -1) {
			// 既にアイテムを決まってる
			return select_item;
		}
		final Random ptn_rand = new Random();
		Random flg = new Random();
		if (flg.nextInt(3) == 1) {
			item_pattern = ptn_rand.nextInt(5);
			// debug用
			// item_pattern=4;
			if (item_pattern == 0) {
				select_item = "M";
			} else if (item_pattern == 1) {
				select_item = "B";
			} else if (item_pattern == 2) {
				select_item = "S";
			} else if (item_pattern == 3) {
				select_item = "G";
			} else if (item_pattern == 4) {
				select_item = "P";
			}
		} else {
			select_item = "null";
		}
		return select_item;
	}

	public void updatePosition() {
		// patternによって移動パターン決定
		if (mPattern == 0) {
			posY += SPEED;
		} else if (mPattern == 1) {
			posY += SPEED;
			posX = 60 * FloatMath.cos(posY / 60) + centerX;
		} else if (mPattern == 2) {
			posY += SPEED;
			dy += SPEED * (float) Math.tan(Math.PI / 9);
			posX = dy + centerX;
		} else {
			posY += SPEED;
			dy += SPEED * (float) Math.tan(Math.PI / 9);
			posX = -dy + centerX;
		}
	}

	public void remove() {
		posY = -1;
	}
}
