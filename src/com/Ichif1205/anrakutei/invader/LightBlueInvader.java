package com.Ichif1205.anrakutei.invader;

import java.util.Random;

import com.Ichif1205.anrakutei.R;

import android.content.Context;
import android.util.FloatMath;

public class LightBlueInvader extends BaseInvader {
	private float centerX;
	private float centerY;
	private float alpha = 0;
	private float theta;
	private int radius;

	public LightBlueInvader(Context context, float x, float y,
			InvarderListener li) {
		super(context, x, y, li);
		radius = getRadius();
		calcCenterPosition(x, y);
	}

	@Override
	protected int getInvaderResource() {
		return R.drawable.invader3;
	}
	
	@Override
	public void reverseSpeedYDirection() {
		speedX = -speedX;
	}

	@Override
	public void move(float playerPosX) {
		if (mMovePattern < 0.5) {
			alpha += speedX * 1.0 / radius;
		} else {
			alpha -= speedX * 1.0 / radius;
		}
		mPositionX = radius * FloatMath.cos(theta + alpha) + centerX;
		mPositionY = radius * FloatMath.sin(theta + alpha) + centerY;
	}

	private void calcCenterPosition(float x, float y) {
		centerX = mPositionX;
		centerY = mPositionY;

		// centerの位置座標によってthetaの初期値を決定
		// 例えば画面の右下が初期位置ならthetaの開始は円の左上
		if (mPositionX <= x / 2 && mPositionY > y / 2) {
			theta = (float) (7 * Math.PI / 4);
		} else if (mPositionX > x / 2 && mPositionY > y / 2) {
			theta = (float) (5 * Math.PI / 4);
		} else if (mPositionX > x / 2 && mPositionY <= y / 2) {
			theta = (float) (3 * Math.PI / 4);
		} else {
			theta = (float) (Math.PI / 4);
		}
	}
	
	private int getRadius() {
		final Random r_rand = new Random();
		int r_times = r_rand.nextInt(3);
		return 50 * (2 + r_times);
	}
}
