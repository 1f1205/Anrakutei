package com.Ichif1205.anrakutei;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.util.FloatMath;

public class Invader {

	private float posX;
	private float posY;
	private int width = 36;
	private int height = 36;
	private int speedX = 4;
	private int speedY = 4;
	private int pattern;
	private float theta = 0;
	private int radius = 50;
	private float centerX;
	private float centerY;
	private float alpha = 0;
	private boolean existFlag;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;

	Invader(float x, float y, InvarderListener li) {
		posX = getRandomPosition(x);
		posY = getRandomPosition(y);
		existFlag = true;
		mIl = li;
		Random ptn_rand = new Random();
		pattern = ptn_rand.nextInt(3);
		if (pattern == 2) {
			centerX = posX;
			centerY = posY;
		}

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

	public boolean isExisted() {
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

	public float getRandomPosition(float length) {
		Random randf = new Random();
		float rate = randf.nextFloat();
		return rate * length;
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
		posX = radius * FloatMath.cos(theta + alpha) + centerX;
		posY = radius * FloatMath.sin(theta + alpha) + centerY;
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
			Random rBeam = new Random();
			int randBeam = 1000 + (500 * rBeam.nextInt(5));
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}

}
