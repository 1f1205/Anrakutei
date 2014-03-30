package com.Ichif1205.anrakutei.item;

import android.content.Context;


public class ItemFactory {
	/**
	 * 3回に一回の割合でアイテムを生成する
	 */
	private static final int ITEM_CRAETE = 3;

	/**
	 * アイテムの種類
	 * アイテムを追加する時は書き換える
	 */
	private static final int ITEM_PATTERN = 5;
	
	private ItemFactory() {}
	
	public static BaseItem create(Context context, ItemMediator mediator, float posX, float posY){
		final int createRand = (int) (Math.random() * ITEM_CRAETE);
		if (createRand != 1) {
			// 1以外の時はNullを返す
			return null;
		}
		
		final int randNum = (int) (Math.random() * ITEM_PATTERN);
		switch (randNum) {
		case 0:
			return new ClearBeamItem(context, mediator, posX, posY);
		case 1:
			return new BeamItem(context, mediator, posX, posY);
		case 2:
			return new ChangePlayerWidthItem(context, mediator, posX, posY);
		case 3:
			return new GuardItem(context, mediator, posX, posY);
		case 4:
		default:
			return new AddScore(context, mediator, posX, posY);
		}
	}

}
