package com.Ichif1205.anrakutei.invader;

import com.Ichif1205.anrakutei.R;

import android.content.Context;

public class OrangeInvader extends BaseInvader {

	public OrangeInvader(Context context, float x, float y, InvarderListener li) {
		super(context, x, y, li);
	}

	@Override
	protected int getInvaderResource() {
		return R.drawable.invader4;
	}

	@Override
	public void move(float playerPosX) {
		if (mMovePattern < 0.5) {
		 	mPositionY += speedY;
		} else {
			mPositionY -= speedY;
		}
	}

}
