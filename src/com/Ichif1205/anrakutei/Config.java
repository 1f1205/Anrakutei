package com.Ichif1205.anrakutei;

/**
 * アプリケーション全体の設定クラス
 * 基本的に定数を持つ
 * 
 * @author tanakatatsuya
 */
public class Config {
	private Config() {}
	
	/**
	 * デバックフラグ
	 */
	public static final boolean _DEBUG_ = true;
	
	/**
	 * 敵の種類
	 * TODO enumの方が良さそう
	 */
	public static final int INV_PURPLE = 0;
	public static final int INV_YELLOW = 1;
	public static final int INV_LIGHTBLUE = 2;
	public static final int INV_ORANGE = 3;
	public static final int INV_GREEN = 4;
	public static final int INV_BOSS = 5;

}
