package com.Ichif1205.anrakutei.invader;

import android.content.Context;

import com.Ichif1205.anrakutei.R;

public class PurpleInvader extends BaseInvader {

	public PurpleInvader(Context context, float x, float y, InvarderListener li) {
		super(context, x, y, li);
	}

	@Override
	protected int getInvaderResource() {
		return R.drawable.invader;
	}

	@Override
	public void move(float playerPosX) {
		if (mMovePattern < 0.5) {
			mPositionX += speedX;
		} else {
			mPositionX -= speedX;
		}
	}

}
