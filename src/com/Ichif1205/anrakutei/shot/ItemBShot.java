package com.Ichif1205.anrakutei.shot;

import android.graphics.RectF;

public class ItemBShot extends Shot {

	/**
	 * ItemBを取った後の弾の横幅
	 */
	private static final int ITEM_B_SHOT_WIDTH = 15;

	public ItemBShot(float x, float y) {
		super(x, y);
	}

	@Override
	protected RectF createRectangle() {
		final RectF rectf = new RectF(getShotPosX() - ITEM_B_SHOT_WIDTH / 2,
				getShotPosY() - SHOT_HEIGHT / 2,
				getShotPosX() + ITEM_B_SHOT_WIDTH / 2, getShotPosY() + SHOT_HEIGHT / 2);
		return rectf;
	}

}
