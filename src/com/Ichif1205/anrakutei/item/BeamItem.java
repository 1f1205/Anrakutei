package com.Ichif1205.anrakutei.item;

import android.content.Context;

import com.Ichif1205.anrakutei.R;

public class BeamItem extends BaseItem {

	public BeamItem(Context context, float positionX, float positionY) {
		super(context, positionX, positionY);
	}


	@Override
	protected void adjustEffective() {
		
	}

	@Override
	protected int getItemResource() {
		return R.drawable.item2;
	}

}
