package com.Ichif1205.anrakutei;

import android.graphics.RectF;


public class Shot {

	private float posX;
	private float posY;
	private int width = 5;
	private int height = 20;
	private int speed = 20;
	
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
}
