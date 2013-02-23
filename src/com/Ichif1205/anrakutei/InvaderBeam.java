package com.Ichif1205.anrakutei;

import android.graphics.RectF;


public class InvaderBeam {

	private float posX;
	private float posY;
	private int width = 5;
	private int height = 20;
	private int speed = 5;
	
	InvaderBeam(float x, float y) {
		posX = x;
		posY = y;
	}
	
	public float getInvBeamPosX() {
		return posX;	
	}
	public float getInvBeamPosY() {
		return posY;	
	}
	public int getInvBeamWidth() {
		return width;	
	}
	public int getInvBeamHeight() {
		return height;	
	}
	public int getInvBeamSpeed() {
		return speed;
	}
	public void setInvBeamPosX(float x) {
		posX = x;
	}
	public void setInvBeamPosY(float y) {
		posY = y;
	}
	public void setInvBeamWidth(int w) {
		width = w;
	}
	public void setInvBeamHeight(int h) {
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
