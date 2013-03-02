package com.Ichif1205.anrakutei;

//import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Invader {

	private float posX;
	private float posY;
	private int width = 30;
	private int height = 60;
	private int speed = 5;
	//private int pattern;
	private boolean existFlag;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;
	
	Invader(int x, int y, InvarderListener li) {
		posX = x;
		posY = y;
		existFlag = true;
		mIl = li;
		//Random rand = new Random();
		//pattern = rand.nextInt(2);
		
		 mShootTimer = new Timer();
	     mShootTimer.schedule(new ShootTask(), mTerm); 
	}
	
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
	public boolean getInvaderExistFlag() {
		return existFlag;
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
	public void setInvaderExistFlag(boolean exflag){
		existFlag = exflag;
	}
	public boolean isShooted(float shotX, float shotY) {
		if ((posX-width/2 <= shotX && posX+width/2 >= shotX)
				&& (posY-height/2 <= shotY && posY+height/2 >= shotY)){
			return true;
		}
		return false;
	}
	public boolean isOverBoundary(int width) {
		if (posX > width || posX < 0) {
			return true;
		}
		return false;
	}
	public void reverseSpeedDirection() {
		speed =- speed;
	}
	public void updatePosition() {
		//異なる移動パターンを実行
		posX += speed;
			//moveLR();
		/*if(pattern == 0) {
			moveLR();
		} else {
			moveSkew();
		}*/ 
	}
	/*private void moveSkew() {
		posX += speed;
		posY += speed;
	}*/
	public void remove() {
		posY = -100;
		existFlag = false;
		mShootTimer.cancel(); 
	}
	
	public interface InvarderListener {
		public void shootBeamEvent(float shotX, float shotY);
	}
	
	class ShootTask extends TimerTask {
	    public void run() {
	    	mIl.shootBeamEvent(posX, posY);
	    	mTerm = mTerm + 2000;
	    	mShootTimer.schedule(new ShootTask(), mTerm); 
	    }
	}	
	
}
