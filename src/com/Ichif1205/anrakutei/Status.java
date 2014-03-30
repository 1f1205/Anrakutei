package com.Ichif1205.anrakutei;

import java.util.ArrayList;

import com.Ichif1205.anrakutei.beam.BaseBeam;
import com.Ichif1205.anrakutei.invader.BaseInvader;
import com.Ichif1205.anrakutei.item.BaseItem;
import com.Ichif1205.anrakutei.shot.Shot;

/**
 * ゲーム状態を保存するオブジェクト
 */
public class Status {
	public ArrayList<Shot> shotList;
	public ArrayList<BaseBeam> invBeamList;
	public ArrayList<BaseInvader> invaderList;
	public ArrayList<BaseItem> item;
	public boolean pauseFlg;
}
