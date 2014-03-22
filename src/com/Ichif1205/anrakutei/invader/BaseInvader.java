package com.Ichif1205.anrakutei.invader;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.Ichif1205.anrakutei.Player;
import com.Ichif1205.anrakutei.beam.BaseBeam;
import com.Ichif1205.anrakutei.beam.BeamFactory;
import com.Ichif1205.anrakutei.shot.Shot;

/**
 * インベーダー（敵）の基底クラス
 * 
 * @author tanakatatsuya
 */
public abstract class BaseInvader {
	@SuppressWarnings("unused")
	private final String TAG = BaseInvader.class.getSimpleName();

	/**
	 * インベーダーを消す時のY座標
	 */
	private static final float DISMISS_INVADER_POSITION_Y = -1000;

	/**
	 * インベーダーの画像の横幅
	 */
	private static final int IMAGE_WIDTH = 36;

	/**
	 * インベーダーの画像の縦幅
	 */
	private static final int IMAGE_HEIGHT = 36;

	/**
	 * 撃墜ポイント
	 */
	private static final int DEFAULT_POINT = 1000;

	/**
	 * インベーダーの存在 true: 生存 false: 死亡
	 */
	private boolean mIsExist = false;

	/**
	 * インベーダーのX座標
	 */
	protected float mPositionX;

	/**
	 * インベーダーのY座標
	 */
	protected float mPositionY;

	protected int speedX;

	protected int speedY;

	protected double mMovePattern;

	protected Timer mShootTimer;

	protected InvarderListener mIl;

	private Bitmap mInvaderBitmap;

	public BaseInvader(Context context, float x, float y, InvarderListener li) {
		mPositionX = getRandomPosition(x);
		mPositionY = getRandomPosition(y);
		Random speed_rand = new Random();
		int spd = speed_rand.nextInt(3) + 3;
		speedX = spd;
		speedY = spd;
		mIsExist = true;
		mIl = li;

		// 画像生成
		final Bitmap nonScaleBitmap = BitmapFactory.decodeResource(
				context.getResources(), getInvaderResource());
		mInvaderBitmap = Bitmap.createScaledBitmap(nonScaleBitmap,
				getImageWidth(), getImageHeight(), true);

		mMovePattern = Math.random();
		mShootTimer = new Timer();
		mShootTimer.schedule(new ShootTask(), 1000);
	}

	/**
	 * 画像の横幅を返す
	 * 
	 * @return 敵画像の横幅
	 */
	protected int getImageWidth() {
		return IMAGE_WIDTH;
	}

	/**
	 * 画像の縦幅を返す
	 * 
	 * @return 敵画像の縦幅
	 */
	protected int getImageHeight() {
		return IMAGE_HEIGHT;
	}

	/**
	 * インベーダーのBitmapを生成する
	 * <p>
	 * Note: 画像をインベーダーのサイズに合わせてScaleする
	 * </p>
	 * 
	 * @return インベーダーの縦、横幅に合わせて生成されたBitmap
	 */
	private Bitmap createBitmap() {
		return mInvaderBitmap;
	}

	/**
	 * インベーダーの画像リソースを返す
	 * 
	 * @return drawableのファイル名
	 */
	protected abstract int getInvaderResource();

	/**
	 * 倒されたあとの処理
	 */
	public void remove() {
		mPositionY = DISMISS_INVADER_POSITION_Y;
		mIsExist = false;
		mInvaderBitmap = null;
		mShootTimer.cancel();
	}

	public boolean isShooted(Shot shot) {
		if ((shot.getShotPosX() >= mPositionX)
				&& (shot.getShotPosX() <= mPositionX + getImageWidth())
				&& (shot.getShotPosY() >= mPositionY)
				&& (shot.getShotPosY() <= mPositionY + getImageHeight())) {
			return true;
		}
		return false;
	}

	/**
	 * インベーダーを描画する
	 * 
	 * TODO screenWidth, screenHeightは１つにまとめまれそう
	 * 
	 * @param canvas
	 * @param paint
	 * @param player
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void onDraw(Canvas canvas, Paint paint, Player player,
			int screenWidth, int screenHeight) {
		if (!mIsExist) {
			return;
		}

		if (isOverBoundaryWidth(screenWidth)) {
			reverseSpeedXDirection();
		}

		if (isOverBoundaryHeight((int) (screenWidth * Player.PLAYER_INIT_HEIGHT_RATE))) {
			reverseSpeedYDirection();
		}

		move(player.getPlayerPosX());
		canvas.drawBitmap(createBitmap(), mPositionX, mPositionY, paint);
	}

	private boolean isOverBoundaryWidth(int w) {
		if (mPositionX > w - getImageWidth() || mPositionX < 0) {
			return true;
		}
		return false;
	}

	private boolean isOverBoundaryHeight(int h) {
		if (mPositionY > h - getImageHeight() || mPositionY < 0) {
			return true;
		}
		return false;
	}

	/**
	 * 画面の端(横幅)に到達したら反転する
	 */
	protected void reverseSpeedXDirection() {
		speedX = -speedX;
	}

	/**
	 * 画面の端(縦幅)に到達したら反転する
	 */
	protected void reverseSpeedYDirection() {
		speedY = -speedY;
	}

	/**
	 * ビームの動きを定義したInvaderBeamオブジェクトを返す
	 * 
	 * @return
	 */
	protected BaseBeam[] getBeam() {
		final BaseBeam beam = BeamFactory.createBeam(mPositionX
				+ getImageWidth() / 2, mPositionY + getImageHeight());
		BaseBeam[] beams = { beam };
		return beams;
	}

	/**
	 * インベーダーの動きを実装する
	 */
	public abstract void move(float playerPositionX);

	/**
	 * 倒した時のポイントを返す
	 * 
	 * @return ポイント
	 */
	public int getPoint() {
		return DEFAULT_POINT;
	}

	/**
	 * レイアウトの領域からインベーダーの位置をランダムに返す
	 * 
	 * @param layoutLength
	 *            レイアウトの長さ(width, height)
	 * @return レイアウトの領域内のランダムな数字
	 */
	private float getRandomPosition(float layoutLength) {
		float rate;
		// TODO ループしないように直す
		while (true) {
			rate = (float) Math.random();
			if (rate > 0.20 && rate < 0.80) {
				break;
			}
		}
		float pos = rate * layoutLength;

		return pos;
	}

	/**
	 * インベーダーのイベントリスナー
	 * 
	 * @author tanakatatsuya
	 */
	public interface InvarderListener {
		/**
		 * インベーダーがビームを発射したイベント
		 * 
		 */
		public void shootBeam(BaseBeam... beam);

		public void Item(float shotX, float shotY);
	}

	public void ItemAdd() {
		mIl.Item(mPositionX, mPositionY);
	}

	protected class ShootTask extends TimerTask {
		public void run() {
			mIl.shootBeam(getBeam());
			final Random binterval_rand = new Random();
			final int randBeam = 1000 + (100 * binterval_rand.nextInt(5));
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}
}
