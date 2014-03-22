package com.Ichif1205.anrakutei.beam;

import java.util.Random;

public class BeamFactory {
	/**
	 * ビームの総数
	 */
	private static final int BEAM_PATTERN = 3;

	/**
	 * ビームの種類
	 */
	private static final int BEAM_STRAIGHT = 0;
	private static final int BEAM_SINWAVE = 1;
	private static final int BEAM_20SKEW = 2;
	
	private static final int BOSS_BEAM_SPEED = 7;

	private BeamFactory() {}
	
	/**
	 * ビームを生成する 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BaseBeam createBeam(float x, float y) {
		final Random rand = new Random();
		final int beamType = rand.nextInt(BEAM_PATTERN);
		
		if (beamType == BEAM_STRAIGHT) {
			return new Straight(x, y);
		} else if (beamType == BEAM_SINWAVE) {
			return new SinWave(x, y);
		} else {
			return new Skew(x, y);
		}
	}
	
	/**
	 * ボスのビームを生成する
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BaseBeam createBossBeam(float x, float y) {
		final Random rand = new Random();
		final int beamType = rand.nextInt(BEAM_PATTERN);
		
		if (beamType == BEAM_STRAIGHT) {
			return new Straight(x, y, BOSS_BEAM_SPEED);
		} else if (beamType == BEAM_SINWAVE) {
			return new SinWave(x, y, BOSS_BEAM_SPEED);
		} else {
			return new Skew(x, y, BOSS_BEAM_SPEED);
		}
	}
}
