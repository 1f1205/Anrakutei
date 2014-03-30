package com.Ichif1205.anrakutei.item;

import android.content.Context;

import com.Ichif1205.anrakutei.R;

public class BeamItem extends BaseItem {

	public BeamItem(Context context, ItemMediator mediator, float positionX, float positionY) {
		super(context, mediator, positionX, positionY);
	}


	@Override
	public void adjustEffective() {
		mMediator.updateShotWidht();
	}

	@Override
	protected int getItemResource() {
		return R.drawable.item2;
	}

}
