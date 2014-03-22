package com.Ichif1205.anrakutei.invader;

import java.util.Random;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.Ichif1205.anrakutei.Player;
import com.Ichif1205.anrakutei.R;
import com.Ichif1205.anrakutei.beam.BaseBeam;
import com.Ichif1205.anrakutei.beam.BeamFactory;
import com.Ichif1205.anrakutei.shot.Shot;

public class BossInvader extends BaseInvader {
	@SuppressWarnings("unused")
	private static final String TAG = BossInvader.class.getSimpleName();
	/**
	 * ボスの横画像サイズ
	 */
	protected static final int BOSS_INV_IMAGE_WIDTH = 72;

	/**
	 * ボスの縦画像サイズ
	 */
	protected static final int BOSS_INV_IMAGE_HEIGHT = 72;

	/**
	 * ボスのHP
	 */
	private static final int MAX_BOSS_HP = 10; 

	private int mBossHP; // BossのHP

	public BossInvader(Context context, float x, float y, InvarderListener li) {
		super(context, x, y, li);
		mBossHP = MAX_BOSS_HP;
	}
	
	@Override
	protected int getImageHeight() {
		return BOSS_INV_IMAGE_HEIGHT;
	}
	
	@Override
	protected int getImageWidth() {
		return BOSS_INV_IMAGE_WIDTH;
	}

	@Override
	protected int getInvaderResource() {
		return R.drawable.invader6;
	}
	
	@Override
	public void onDraw(Canvas canvas, Paint paint, Player player,
			int screenWidth, int screenHeight) {
		super.onDraw(canvas, paint, player, screenWidth, screenHeight);

		Path path = new Path();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawPath(
				drawBossHPMeter(path, mPositionX, mPositionY), paint);
	}
	
	@Override
	protected BaseBeam[] getBeam() {
		final BaseBeam invBeam = BeamFactory.createBossBeam(mPositionX, mPositionY 
				+ getImageHeight()/ 2);
		final BaseBeam invBeam2 = BeamFactory.createBossBeam(mPositionX + getImageWidth()/ 2, mPositionY 
				+ getImageHeight()/ 2);
		final BaseBeam invBeam3 = BeamFactory.createBossBeam(mPositionX + getImageWidth(), mPositionY 
				+ getImageHeight()/ 2);
		final BaseBeam[] beams = {invBeam, invBeam2, invBeam3};
		return beams;
	}

	@Override
	public void move(float playerPosX) {
		double mvvi = Math.random();
		// 1/8 の確率で移動
		// そのうち各方向に移動する確率は 上: 2/8, 下: 2/8,
		// 自機の方向: 3/8, 自機と逆方向: 1/8
		if (mvvi < 0.03125) {		
			mPositionY += speedY + 4;
		} else if (mvvi < 0.0625) {
			mPositionY -= speedY + 4;
		} else if (mvvi < 0.09375) {
			if (mPositionX > playerPosX) {
				mPositionX -= speedX + 4;
			} else {
				mPositionX += speedX + 4;
			}
		} else if (mvvi < 0.0625) {
			if (mPositionX > playerPosX) {
				mPositionX += speedX + 4;
			} else {
				mPositionX -= speedX + 4;
			}
		}

	}

	// ボスHP描画のパス
	private Path drawBossHPMeter(Path path, float x, float y) {
		final int HPwidth = 50 / MAX_BOSS_HP * mBossHP;
		final int HPheight = 8;
		final float HPX = x + 36; // width/2
		final float HPY = y - 20; // 適当
		path.moveTo(HPX, HPY);
		// 左下から反時計回りに描画
		path.lineTo(HPX - HPwidth, HPY + HPheight);
		path.lineTo(HPX + HPwidth, HPY + HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY + HPheight);

		return path;
	}
	
	@Override
	public int getPoint() {
		return 3000;
	}

	protected class ShootTask extends TimerTask {
		public void run() {
			mIl.shootBeam(getBeam());
			Random binterval_rand = new Random();
			int randBeam = 1000 + (100 * binterval_rand.nextInt(5));
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}
	
	@Override
	public boolean isShooted(Shot shot) {
		if (!super.isShooted(shot)) {
			return false;
		}

		mBossHP--;
		if (mBossHP != 0) {
			shot.remove();
			return false;
		}
		
		return true;
	}
}
