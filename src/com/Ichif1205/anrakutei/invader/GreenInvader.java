package com.Ichif1205.anrakutei.invader;

import android.content.Context;

import com.Ichif1205.anrakutei.R;

public class GreenInvader extends BaseInvader {

	public GreenInvader(Context context, float x, float y, InvarderListener li) {
		super(context, x, y, li);
	}

	@Override
	protected int getInvaderResource() {
		// TODO Auto-generated method stub
		return R.drawable.invader5;
	}

	@Override
	public void move(float playerPosX) {
		if (mMovePattern < 0.25) {
			mPositionX += speedX;
			mPositionY += speedY / 2;
		} else if (mMovePattern < 0.5) {
			mPositionX -= speedX;
			mPositionY += speedY / 2;
		} else if (mMovePattern < 0.75) {
			mPositionX += speedX;
			mPositionY -= speedY / 2;
		} else {
			mPositionX -= speedX;
			mPositionY -= speedY / 2;
		}

	}

}
