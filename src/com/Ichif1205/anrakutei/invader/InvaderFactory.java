package com.Ichif1205.anrakutei.invader;

import android.content.Context;

import com.Ichif1205.anrakutei.Config;
import com.Ichif1205.anrakutei.invader.BaseInvader.InvarderListener;

public class InvaderFactory {
	private InvaderFactory() {
	}

	/**
	 * invTypeからインベーダーを生成する TODO
	 * 引数多いからどうにかしたいけど、オブジェクトの生成したくないからコンストラクタでセットするようにしたくない
	 * 
	 * @param context
	 * @param width
	 * @param height
	 * @param listener
	 * @param invType
	 * @return
	 */
	public static BaseInvader create(Context context, float width,
			float height, InvarderListener listener, int invType) {
		final BaseInvader invader;

		if (invType == Config.INV_PURPLE) {
			invader = new PurpleInvader(context, width, height / 2, listener);
		} else if (invType == Config.INV_YELLOW) {
			invader = new YellowInvader(context, width, height / 2, listener);
		} else if (invType == Config.INV_LIGHTBLUE) {
			invader = new LightBlueInvader(context, width, height / 2, listener);
		} else if (invType == Config.INV_ORANGE) {
			invader = new OrangeInvader(context, width, height / 2, listener);
		} else if (invType == Config.INV_GREEN) {
			invader = new GreenInvader(context, width, height / 2, listener);
		} else {
			invader = new BossInvader(context, width, height / 2, listener);
		}

		return invader;
	}
}
