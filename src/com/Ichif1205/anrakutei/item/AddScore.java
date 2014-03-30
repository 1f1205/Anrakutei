package com.Ichif1205.anrakutei.item;

import com.Ichif1205.anrakutei.R;

import android.content.Context;

public class AddScore extends BaseItem {

	public AddScore(Context context, ItemMediator mediator, float positionX,
			float positionY) {
		super(context, mediator, positionX, positionY);
	}

	@Override
	public void adjustEffective() {
		mMediator.addScore();
	}

	@Override
	protected int getItemResource() {
		return R.drawable.item5;
	}

}
