package com.Ichif1205.anrakutei.item;

import java.util.ArrayList;
import java.util.Random;

import com.Ichif1205.anrakutei.Player;
import com.Ichif1205.anrakutei.beam.BaseBeam;
import com.Ichif1205.anrakutei.score.Score;

public class ItemMediator {
	@SuppressWarnings("unused")
	private static final String TAG = ItemMediator.class.getSimpleName();
	
	/**
	 * プレイヤーオブジェクト
	 */
	private final Player mPlayer;
	
	/**
	 * インベーダーのビームを管理するArrayList
	 */
	private final ArrayList<BaseBeam> mInvBeams;
	
	/**
	 * スコアオブジェクト
	 */
	private final Score mScore;
	
	public ItemMediator(Player player, ArrayList<BaseBeam> invBeams) {
		mPlayer = player;
		mInvBeams = invBeams;
		mScore = Score.getInstance();
	}
	

	/**
	 * 弾の幅を変更する
	 */
	public void updateShotWidht() {
		mPlayer.mItemB = true;
	}
	
	/**
	 * インベーダーのビームを全て消す
	 */
	public void getClearInvBeam() {
		mInvBeams.clear();
	}
	
	/**
	 * ガードアイテムの取得
	 */
	public void getGuard() {
		mPlayer.mIsGuard = true;
	}
	
	/**
	 * スコアアイテムの取得
	 */
	public void addScore() {
		final Random rand = new Random();
		final int addScore = (rand.nextInt(4) + 1) * 100;
		mScore.addScore(addScore);
	}
	
	/**
	 * プレイヤーの幅を変更
	 */
	public void updatePlayerWidth() {
		mPlayer.mItemS = true;
	}
}
