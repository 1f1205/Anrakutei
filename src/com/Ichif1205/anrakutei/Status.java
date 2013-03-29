package com.Ichif1205.anrakutei;

import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * ゲーム状態を保存するオブジェクト
 */
public class Status {
	public Bitmap bitmap;
	public ArrayList<Shot> shotList;
	public ArrayList<InvaderBeam> invBeamList;
	public ArrayList<Invader> invaderList;
	public ArrayList<Item> item;
	public boolean pauseFlg;
}
