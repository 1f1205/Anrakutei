package com.Ichif1205.anrakutei;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Invader {

	private float posX;
	private float posY;
	private int width = 30;
	private int height = 60;
	private int speedX = 5;
	private int speedY = 5;
	private int pattern;
	private double theta = 0;
	private int radius = 50;
	private int centerX = 200;
	private int centerY = 200;
	private double alpha = 0;
	private boolean existFlag;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;

	Invader(int x, int y, InvarderListener li) {
		posX = x;
		posY = y;
		existFlag = true;
		mIl = li;
		Random rand = new Random();
		pattern = rand.nextInt(3);

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

	public void setInvaderExistFlag(boolean exflag) {
		existFlag = exflag;
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((posX - width / 2 <= shotX && posX + width / 2 >= shotX)
				&& (posY - height / 2 <= shotY && posY + height / 2 >= shotY)) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryWidth(int width) {
		if (posX > width || posX < 0) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryHeight(int height) {
		if (posY > height || posY < 0) {
			return true;
		}
		return false;
	}

	public void reverseSpeedXDirection() {
		speedX = -speedX;
	}

	public void reverseSpeedYDirection() {
		speedY = -speedY;
	}

	public void updatePosition() {
		// patternの値によって移動パターン決定
		pattern = 2;
		if (pattern == 0) {
			moveLR();
		} else if (pattern == 1) {
			moveSkew();
		} else {
			moveCircle();
		}
	}

	private void moveLR() {
		posX += speedX;
	}

	private void moveSkew() {
		posX += speedX;
		posY += speedY;
	}

	private void moveCircle() {
		alpha += speedX;
		posX =  (int)(radius * Math.cos(theta + alpha) + centerX);
		posY =  (int)(radius * Math.sin(theta + alpha) + centerY);
	}

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
