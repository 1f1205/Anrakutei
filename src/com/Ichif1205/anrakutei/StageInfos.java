package com.Ichif1205.anrakutei;

import android.util.SparseArray;

/**
 * Singleton
 */
public class StageInfos {
	private static SparseArray<StageInfo> instance;
	
	public StageInfos() {
		instance = null;
	}
	
	public static SparseArray<StageInfo> getInstance() {
		if (instance == null) {
			instance = new SparseArray<StageInfo>();
		}
		return instance;
	}
	
	
}
