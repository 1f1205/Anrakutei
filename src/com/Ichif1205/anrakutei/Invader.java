package com.Ichif1205.anrakutei;

import android.graphics.Path;


public class Invader {

	private float posX;
	private float posY;
	private int width = 60;
	private int height = 40;
	private int speed = 5;
	
	public float getInvaderPosX() {
		return posX;	
	}
	public float getInvaderPosY() {
		return posY;	
	}
	public int getInvaderWidth() {
		return width;	
	}
	public int getInvaderHeight() {
		return height;	
	}
	public int getInvaderSpeed() {
		return speed;	
	}
	public void setInvaderPosX(float x) {
		posX = x;
	}
	public void setInvaderPosY(float y) {
		posY = y;
	}
	public void setInvaderWidth(int w) {
		width = w;
	}
	public void setInvaderHeight(int h) {
		height = h;
	}
	public void setInvaderSpeed(int spd) {
		speed = spd;
	}
}
