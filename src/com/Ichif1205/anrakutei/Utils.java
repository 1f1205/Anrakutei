package com.Ichif1205.anrakutei;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.util.Log;

public class Utils {
	private static final String TAG = Utils.class.getSimpleName();

	/**
	 * フォントを取得
	 * 
	 * @param context
	 * @return
	 */
	public static Typeface getFonts(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/BallsoOnTheRampage.ttf");
	}

	/**
	 * BGMを流す
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isRingerEnable(Context context) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		final int audioMode = am.getRingerMode();
		if (audioMode == AudioManager.RINGER_MODE_NORMAL) {
			return true;
		} else {
			return false;
		}
	}
}
