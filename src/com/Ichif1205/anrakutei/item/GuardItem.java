package com.Ichif1205.anrakutei.item;

import com.Ichif1205.anrakutei.R;

import android.content.Context;

public class GuardItem extends BaseItem {

	public GuardItem(Context context, ItemMediator mediator, float positionX,
			float positionY) {
		super(context, mediator, positionX, positionY);
	}

	@Override
	public void adjustEffective() {
		mMediator.getGuard();
	}

	@Override
	protected int getItemResource() {
		return R.drawable.item4;
	}

}
