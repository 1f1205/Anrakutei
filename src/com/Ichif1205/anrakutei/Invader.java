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
	private double mv_pattern;
	private int type;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;
	// 敵の動き関係
	private int speedX;
	private int speedY;
	private float theta = 0;
	private int radius;
	private float centerX;
	private float centerY;
	private float alpha = 0;
	// 敵の種類定義
	private static int INV_PURPLE = 0;
	private static int INV_YELLOW = 1;
	private static int INV_LIGHTBLUE = 2;
	private static int INV_ORANGE = 3;
	private static int INV_GREEN = 4;
	private static int INV_BOSS = 5;

	Invader(float x, float y, InvarderListener li) {
		posX = getRandomPosition(x);
		posY = getRandomPosition(y);
		Random speed_rand = new Random();
		int spd = speed_rand.nextInt(3) + 3;
		speedX = spd;
		speedY = spd;
		existFlag = true;
		Random type_rand = new Random();
		type = type_rand.nextInt(6);
		System.out.println("TYPE" + type);
		mv_pattern = Math.random();
		mIl = li;
		if (type == INV_LIGHTBLUE) {
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
		// インベーダーの種類によって動き異なる
		if (type == INV_PURPLE) {
			moveLR();
		} else if (type == INV_YELLOW) {
			moveSkew();
		} else if (type == INV_LIGHTBLUE) {
			moveCircle();
		} else if (type == INV_ORANGE) {
			moveUD();
		} else if (type == INV_GREEN) {
			moveLR();
		} else {
			moveVibration();
		}
	}

	private void moveLR() {
		if (mv_pattern < 0.5) {
			posX += speedX;
		} else {
			posX -= speedX;
		}
	}

	private void moveSkew() {
		if (mv_pattern < 0.25) {
			posX += speedX;
			posY += speedY;
		} else if (mv_pattern < 0.5) {
			posX -= speedX;
			posY += speedY;
		} else if (mv_pattern == 0.75) {
			posX += speedX;
			posY -= speedY;
		} else {
			posX -= speedX;
			posY -= speedY;
		}
	}

	private void moveCircle() {
		if (mv_pattern < 0.5) {
			alpha += speedX * 1.0 / radius;
		} else {
			alpha -= speedX * 1.0 / radius;
		}
		posX = radius * FloatMath.cos(theta + alpha) + centerX;
		posY = radius * FloatMath.sin(theta + alpha) + centerY;
	}

	private void moveUD() {
		if (mv_pattern < 0.5) {
			posY += speedY;
		} else {
			posY -= speedY;
		}
	}

	private void moveVibration() {
		double mvvi = Math.random();
		if (mvvi < 0.03125) {
			posX += speedX + 3;
		} else if (mvvi < 0.0625) {
			posX -= speedX + 3;
		} else if (mvvi < 0.09375) {
			posY += speedY + 3;
		} else if (mvvi < 0.125) {
			posY -= speedY + 3;
		}
	}

	public void remove() {
		posY = 1000;
		existFlag = false;
		mShootTimer.cancel();
	}

	public interface InvarderListener {
		public void shootBeamEvent(float shotX, float shotY, int type);

		public void Item(float shotX, float shotY);
	}

	class ShootTask extends TimerTask {
		public void run() {
			mIl.shootBeamEvent(posX, posY, type);
			Random beam_rand = new Random();
			int randBeam;
			if (type == INV_BOSS) {
				randBeam = 1000 + (200 * beam_rand.nextInt(5));
			} else {
				randBeam = 1500 + (500 * beam_rand.nextInt(5));
			}
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}

	public void ItemAdd() {
		mIl.Item(posX, posY);
	}

}
