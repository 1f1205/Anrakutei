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
	private int speed;
	private int beamType;
	private float centerX;
	private float dy;

	InvaderBeam(float x, float y, int type) {
		posX = x;
		posY = y;
		invType = type;
		Random speed_rand = new Random();
		speed = speed_rand.nextInt(3) + 3;
		Random beam_rand = new Random();
		beamType = beam_rand.nextInt(4);
		if (beamType >= 1) {
			centerX = posX;
		}
		if (invType == 5) {
			speed = 8;
		}
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

	public int getInvType() {
		return invType;
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

	public boolean isInsideScreen(int windowHeight) {
		if (posY <= windowHeight && posY >= 0) {
			return true;
		}
		return false;
	}

	public void updatePosition() {
		// patternによって移動パターン決定
		if (beamType == 0) {
			posY += speed;
		} else if (beamType == 1) {
			posY += speed;
			posX = 60 * FloatMath.cos(posY / 60) + centerX;
		} else if (beamType == 2) {
			posY += speed;
			dy += speed * (float) Math.tan(Math.PI / 9);
			posX = dy + centerX;
		} else {
			posY += speed;
			dy += speed * (float) Math.tan(Math.PI / 9);
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
