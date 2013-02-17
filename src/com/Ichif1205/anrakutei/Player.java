package com.Ichif1205.anrakutei;


public class Player {

	private float posX;
	private float posY;
	private int width = 60;
	private int height = 40;
	
	public float getPlayerPosX() {
		return posX;	
	}
	public float getPlayerPosY() {
		return posY;	
	}
	public int getPlayerWidth() {
		return width;	
	}
	public int getPlayerHeight() {
		return height;	
	}
	public void setPlayerPosX(float x) {
		posX = x;
	}
	public void setPlayerPosY(float y) {
		posY = y;
	}
	public void setPlayerWidth(int w) {
		width = w;
	}
	public void setPlayerHeight(int h) {
		height = h;
	}
	
}
