package com.Ichif1205.anrakutei.invader;

import android.content.Context;

import com.Ichif1205.anrakutei.R;

public class YellowInvader extends BaseInvader {

	public YellowInvader(Context context, float x, float y, InvarderListener li) {
		super(context, x, y, li);
	}

	@Override
	protected int getInvaderResource() {
		return R.drawable.invader2;
	}


	@Override
	public void move(float playerPosX) {
		if (mMovePattern < 0.25) {
			mPositionX += speedX;
			mPositionY += speedY;
		} else if (mMovePattern < 0.5) {
			mPositionX -= speedX;
			mPositionY += speedY;
		} else if (mMovePattern < 0.75) {
			mPositionX += speedX;
			mPositionY -= speedY;
		} else {
			mPositionX -= speedX;
			mPositionY -= speedY;
		}
	}
}
