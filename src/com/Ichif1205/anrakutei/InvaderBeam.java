package com.Ichif1205.anrakutei;

import java.util.Random;

import android.graphics.RectF;
import android.util.FloatMath;

public class InvaderBeam {

	private float posX;
	private float posY;
	private int invType;
	private int width = 5;
	private int height = 20;
	private double mv_pattern;
	private int speed;
	private int beamType;
	private float centerX;
	private float dy;
	private Invader mInvader;
	private static final int BEAM_STRAIGHT = 0;
	private static final int BEAM_SINWAVE = 1;
	private static final int BEAM_20SKEW = 2;

	InvaderBeam(float x, float y, int type) {
		posX = x;
		posY = y;
		invType = type;
		Random speed_rand = new Random();
		speed = speed_rand.nextInt(3) + 3;
		Random beam_rand = new Random();
		beamType = beam_rand.nextInt(3);
		mv_pattern = Math.random();
		if (beamType == BEAM_SINWAVE || beamType == BEAM_20SKEW) {
			centerX = posX;
		}
		if (invType == mInvader.INV_BOSS) {
			speed = 7;
		}
	}

	public float getInvBeamPosX() {
		return posX;
	}

	public float getInvBeamPosY() {
		return posY;
	}

	public int getInvType() {
		return invType;
	}

	public boolean isInsideScreen(int windowHeight) {
		if (posY <= windowHeight && posY >= 0) {
			return true;
		}
		return false;
	}

	public void updatePosition() {
		// patternによって移動パターン決定
		if (beamType == BEAM_STRAIGHT) {
			moveStraight();
		} else if (beamType == BEAM_SINWAVE) {
			moveSinWave();
		} else {
			move20Skew();
		}
	}

	public void moveStraight() {
		posY += speed;
	}

	public void moveSinWave() {
		posY += speed;
		posX = 60 * FloatMath.cos(posY / 60) + centerX;
	}

	public void move20Skew() {
		posY += speed;
		dy += speed * (float) Math.tan(Math.PI / 9);
		if (mv_pattern > 0.5) {
			posX = dy + centerX;
		} else {
			posX = -dy + centerX;
		}
	}

	public RectF createRectangle() {
		RectF rectf = new RectF(posX - width / 2, posY - height / 2, posX
				+ width / 2, posY + height / 2);
		return rectf;
	}

	public void remove() {
		posY = -1;
	}
}
