package com.Ichif1205.anrakutei;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Invader {

	private int posX;
	private int posY;
	private int width = 40;
	private int height = 40;
	private int speedX = 5;
	private int speedY = 5;
	private int pattern;
	private double theta = 0;
	private int radius = 50;
	private int centerX;
	private int centerY;
	private double alpha = 0;
	private boolean existFlag;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;

	Invader(int x, int y, InvarderListener li) {
		posX = getRandomPosition(x);
		posY = getRandomPosition(y);
		existFlag = true;
		mIl = li;
		Random pt_rand = new Random();
		pattern = pt_rand.nextInt(3);
		if (pattern == 2) {
			centerX = posX;
			centerY = posY;
		}

		mShootTimer = new Timer();
		mShootTimer.schedule(new ShootTask(), mTerm);
	}

	public int getInvaderPosX() {
		return posX;
	}

	public int getInvaderPosY() {
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

	public void setInvaderPosX(int x) {
		posX = x;
	}

	public void setInvaderPosY(int y) {
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

	public int getRandomPosition(int length) {
		double rand = Math.random();
		return (int) (rand * length);
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((shotX >= posX) && (shotX <= posX + width) && (shotY >= posY)
				&& (shotY <= posY + height)) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryWidth(int w) {
		if (posX > w - width || posX < 0) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryHeight(int h) {
		if (posY > h - height || posY < 0) {
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
		alpha += speedX * 1.0 / radius;
		posX = (int) (radius * Math.cos(theta + alpha) + centerX);
		posY = (int) (radius * Math.sin(theta + alpha) + centerY);
	}

	public void remove() {
		posY = 1000;
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
