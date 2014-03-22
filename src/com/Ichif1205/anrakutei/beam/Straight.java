package com.Ichif1205.anrakutei.beam;

public class Straight extends BaseBeam {

	public Straight(float x, float y) {
		super(x, y);
	}
	
	public Straight(float x, float y, int speed) {
		super(x, y, speed);
	}

	@Override
	protected void updatePosition() {
		mPositionY += mSpeed;
	}

}
