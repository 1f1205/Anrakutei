package com.Ichif1205.anrakutei.item;

import com.Ichif1205.anrakutei.R;

import android.content.Context;

/**
 * ItemS
 * 
 * @author tanakatatsuya
 *
 */
public class ChangePlayerWidthItem extends BaseItem {

	public ChangePlayerWidthItem(Context context, ItemMediator mediator,
			float positionX, float positionY) {
		super(context, mediator, positionX, positionY);
	}

	@Override
	public void adjustEffective() {
		mMediator.updatePlayerWidth();
	}

	@Override
	protected int getItemResource() {
		return R.drawable.item3;
	}

}
