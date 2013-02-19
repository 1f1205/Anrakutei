package com.Ichif1205.anrakutei;

import android.graphics.Path;


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
	public Path draw(Path path) {
		path.moveTo(posX, posY);
		//左下から反時計回りに描画
		path.lineTo(posX-width/2, posY+height/4);
		path.lineTo(posX+width/2, posY+height/4);
		path.lineTo(posX+width/2, posY-height/4);
		path.lineTo(posX+width/6, posY-height/4);
		path.lineTo(posX+width/6, posY-height*3/4);
		path.lineTo(posX-width/6, posY-height*3/4);
		path.lineTo(posX-width/6, posY-height/4);
		path.lineTo(posX-width/2, posY-height/4);
		path.lineTo(posX-width/2, posY+height/4);
		
		return path;
	}
}
