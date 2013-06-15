package com.Ichif1205.anrakutei;

import android.util.SparseArray;

/**
 * Singleton
 */
public class StageInfos {
	private static SparseArray<StageInfo> instance;
	
	/**
	 * StageInfosのインスタンスを取得
	 * @return
	 */
	public static SparseArray<StageInfo> getInstance() {
		if (instance == null) {
			instance = new SparseArray<StageInfo>();
		}
		return instance;
	}
	
	/**
	 * インスタンスの開放
	 */
	public static void remove() {
		instance = null;
	}
	
}
