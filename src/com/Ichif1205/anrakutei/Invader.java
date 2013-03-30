package com.Ichif1205.anrakutei;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.util.FloatMath;
import android.util.Log;

public class Invader {

	private float posX;
	private float posY;
	private int width = 36;
	private int height = 36;
	private boolean existFlag;
	private int type;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;
	// 敵の動き関係
	private int speedX;
	private int speedY;
	private int mv_pattern;
	private float theta = 0;
	private int radius;
	private float centerX;
	private float centerY;
	private float alpha = 0;

	Invader(float x, float y, InvarderListener li) {
		posX = getRandomPosition(x);
		posY = getRandomPosition(y);
		Random speed_rand = new Random();
		int spd = speed_rand.nextInt(3) + 3;
		speedX = spd;
		speedY = spd;
		existFlag = true;
		Random type_rand = new Random();
		type = type_rand.nextInt(4);
		mIl = li;
		mv_pattern = (int) (Math.random() * 10);
		if (mv_pattern >= 6) {
			centerX = posX;
			centerY = posY;
			Random r_rand = new Random();
			int r_times = r_rand.nextInt(4) + 1;
			radius = 50 * r_times;
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

	public int getInvType() {
		return type;
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
		float rate;
		while (true) {
			rate = (float) Math.random();
			if (rate > 0.1 && rate < 0.9) {
				break;
			}
		}
		float pos = rate * length;
		return pos;
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((shotX >= posX) && (shotX <= posX + width) && (shotY >= posY)
				&& (shotY <= posY + height)) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryWidth(int w) {
		if (posX > w - width || posX <= 0) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryHeight(int h) {
		if (posY > h - height || posY <= 0) {
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
		// 0-3: 横移動, 4-7: 斜め移動, 8-9: 円移動
		if (mv_pattern <= 3) {
			moveLR();
		} else if (mv_pattern <= 7) {
			moveSkew();
		} else {
			moveCircle();
		}
	}

	private void moveLR() {
		if (mv_pattern <= 1) {
			posX += speedX;
		} else {
			posX -= speedX;
		}
	}

	private void moveSkew() {
		int skew_pattern = (int) Math.random();
		if (skew_pattern < 0.25) {
			posX += speedX;
			posY += speedY;
		} else if (skew_pattern < 0.5) {
			posX -= speedX;
			posY += speedY;
		} else if (skew_pattern < 0.75) {
			posX += speedX;
			posY -= speedY;
		} else {
			posX -= speedX;
			posY -= speedY;
		}
	}

	private void moveCircle() {
		if (mv_pattern == 8) {
			alpha += speedX * 1.0 / radius;
		} else {
			alpha -= speedX * 1.0 / radius;
		}
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

		public void Item(float shotX, float shotY);
	}

	class ShootTask extends TimerTask {
		public void run() {
			mIl.shootBeamEvent(posX, posY);
			Random rBeam = new Random();
			int randBeam = 1000 + (500 * rBeam.nextInt(5));
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}

	public void ItemDrop() {
		mIl.Item(posX, posY);
	}

}
