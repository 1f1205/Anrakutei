package com.Ichif1205.anrakutei.score;

/**
 * スコアを管理するクラス 
 * シングルトン
 * 
 * @author tanakatatsuya
 */
public class Score {
	private static Score sInstance;
	private int sScore = 0;

	private Score() { }
	
	public static Score getInstance() {
		if (sInstance == null) {
			sInstance = new Score();
		}
		return sInstance;
	}
	
	/**
	 * スコアの初期化
	 */
	public synchronized void initializeScore() {
		sScore = 0;
	}

	/**
	 * 引数の値をスコアに加算
	 * 
	 * @param addScore 加算する値
	 */
	public synchronized void addScore(int addScore) {
		sScore += addScore;
	}
	
	/**
	 * スコアをセット
	 * 
	 * @param initialScore
	 */
	public synchronized void setScore(int initialScore) {
		sScore = initialScore;
	}
	
	/**
	 * スコアを取得
	 * 
	 * @return
	 */
	public int getScore() {
		return sScore;
	}
	
	@Override
	public String toString() {
		return String.valueOf(sScore);
	}

}
