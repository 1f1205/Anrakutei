package com.Ichif1205.anrakutei.beam;

import android.util.FloatMath;

public class SinWave extends BaseBeam {
	private float mCenterX;

	public SinWave(float x, float y) {
		super(x, y);

		mCenterX = x;
	}
	
	public SinWave(float x, float y, int speed) {
		super(x, y, speed);

		mCenterX = x;
	}

	@Override
	protected void updatePosition() {
		mPositionY += mSpeed;
		mPositionX = 60 * FloatMath.cos(mPositionY / 60) + mCenterX;
	}

}
