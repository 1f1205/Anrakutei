package com.Ichif1205.anrakutei.beam;

public class Skew extends BaseBeam {

	/**
	 * カーブするときの中心
	 */
	private float mCenterX;
	
	/**
	 * 動きのパターン
	 */
	private double mMovePattern;
	
	private float mDynamic = 0;

	public Skew(float x, float y) {
		super(x, y);
		
		mCenterX = x;
	}

	public Skew(float x, float y, int speed) {
		super(x, y, speed);

		mCenterX = x;
	}

	@Override
	protected void updatePosition() {
		mPositionY += mSpeed;
		mDynamic += mSpeed * (float) Math.tan(Math.PI / 9);
		if (mMovePattern > 0.5) {
			mPositionX = mDynamic + mCenterX;
		} else {
			mPositionX = -mDynamic + mCenterX;
		}
	}

}
