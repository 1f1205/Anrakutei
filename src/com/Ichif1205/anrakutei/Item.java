package com.Ichif1205.anrakutei;

import android.graphics.RectF;

public class Item {
	private float posX;
	private float posY;
	private int width = 5;
	private int height = 20;
	private int speed = 5;
	
	Item(float x, float y) {
		posX = x;
		posY = y;
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
	public void updatePosition() {
		posY += speed;
	}
	public RectF createRectangle(){
		RectF rectf = new RectF(posX-width/2, posY-height/2, 
				posX+width/2, posY+height/2);
		return rectf;
	}
	public void remove() {
		posY = 1000;
	}
}
