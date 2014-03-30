package com.Ichif1205.anrakutei;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.Ichif1205.anrakutei.shot.Shot;
import com.Ichif1205.anrakutei.shot.ShotFactory;

public class Player implements OnTouchListener{

	public static final double PLAYER_INIT_WIDTH_RATE = 0.5;
	public static final double PLAYER_INIT_HEIGHT_RATE = 0.84375; // =27/32

	/**
	 * ガードのデフォルトサイズ
	 * プレイヤーのデフォルトサイズと同じ
	 */
	private static final int DEFAULT_GUARD_WIDTH = 50;
	private static final int DEFAULT_GAURD_HEIGHT = 30;

	private float posX;
	private float posY;

	private final int mWindowWidth;
	private final int mWindowHeight;
	
	private int mPlayerWidth = 50;
	private int mPlayerHeight = 30;

	private boolean isExist;
	private float gurdX;
	private float gurdY;
	
	public boolean mItemB = false;
	public boolean mItemS = false;
	public boolean mIsGuard = false;

	Player(int x, int y, int windowWidth, int windowHeight) {
		posX = x;
		posY = y;
		
		mWindowWidth = windowWidth;
		mWindowHeight = windowHeight;
		
		isExist = true;
	}

	public float getPlayerPosX() {
		return posX;
	}

	public float getPlayerPosY() {
		return posY;
	}
	
	public int getWidth() {
		return mPlayerWidth;
	}
	
	public int getHeight() {
		return mPlayerHeight;
	}

	public void setPlayerPosX(float x) {
		posX = x;
	}

	/**
	 * プレイヤーを描画する
	 */
	public void onDraw(Canvas canvas, Paint paint) {
		if (!isExist) {
			return;
		}
		
		final Path guardPath = new Path();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		if (mIsGuard) {
			// ガードの描画
			canvas.drawPath(
					drawGurd(guardPath, mWindowWidth / 2, mWindowHeight * 3 / 4),
					paint);
		}
		
		final Path playerPath = new Path();
		canvas.drawPath(draw(playerPath), paint);
	}
	
	private Path draw(Path path) {
		// itemSをとった場合
		if (mItemS) {
			mPlayerWidth = 15;
		}

		path.moveTo(posX, posY);
		// 左下から反時計回りに描画
		path.lineTo(posX - mPlayerWidth / 2, posY + mPlayerHeight / 4);
		path.lineTo(posX + mPlayerWidth / 2, posY + mPlayerHeight / 4);
		path.lineTo(posX + mPlayerWidth / 2, posY - mPlayerHeight / 4);
		path.lineTo(posX + mPlayerWidth / 6, posY - mPlayerHeight / 4);
		path.lineTo(posX + mPlayerWidth / 6, posY - mPlayerHeight * 3 / 4);
		path.lineTo(posX - mPlayerWidth / 6, posY - mPlayerHeight * 3 / 4);
		path.lineTo(posX - mPlayerWidth / 6, posY - mPlayerHeight / 4);
		path.lineTo(posX - mPlayerWidth / 2, posY - mPlayerHeight / 4);
		path.lineTo(posX - mPlayerWidth / 2, posY + mPlayerHeight / 4);

		return path;
	}

	// ガード描画のパス
	private Path drawGurd(Path path, float x, float y) {
		gurdX = x;
		gurdY = y;
		path.moveTo(gurdX, gurdY);
	
		// 左下から反時計回りに描画
		path.lineTo(gurdX - DEFAULT_GUARD_WIDTH, gurdY + DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX + DEFAULT_GUARD_WIDTH, gurdY + DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX + DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX + DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX + DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT * 3 / 4);
		path.lineTo(gurdX - DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT * 3 / 4);
		path.lineTo(gurdX - DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX - DEFAULT_GUARD_WIDTH, gurdY - DEFAULT_GAURD_HEIGHT / 4);
		path.lineTo(gurdX - DEFAULT_GUARD_WIDTH, gurdY + DEFAULT_GAURD_HEIGHT / 4);

		return path;
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((posX - mPlayerWidth / 2 <= shotX && posX + mPlayerWidth / 2 >= shotX)
				&& (posY - mPlayerHeight / 2 <= shotY && posY + mPlayerHeight / 2 >= shotY)) {
			return true;
		}
		return false;
	}

	// guard判定
	public boolean isGurded(float shotX, float shotY) {
		if ((gurdX - DEFAULT_GUARD_WIDTH <= shotX && gurdX + DEFAULT_GUARD_WIDTH >= shotX)
				&& (gurdY - DEFAULT_GAURD_HEIGHT <= shotY && gurdY + DEFAULT_GAURD_HEIGHT >= shotY)) {
			return true;
		}
		return false;
	}

	/**
	 * 弾に当たった時の終了処理
	 */
	public void remove() {
		posY = -100;
		isExist = false;
	}

	private PlayerEventListener playerListener;
	
	/**
	 * プレイヤー
	 * 
	 * @param listener
	 */
	public void setPlayerListener(PlayerEventListener listener) {
		playerListener = listener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 弾発射
			playerListener.addShot(ShotFactory.create(Player.this));
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			setPlayerPosX(event.getX());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// mPlayer.setPlayerPosX(event.getX());
		}
		return true;
	}
	
	/**
	 * プレイヤークラスのイベント
	 */
	public interface PlayerEventListener {

		/**
		 * プレイヤーの弾発射イベント
		 * 
		 * @param shot
		 */
		public void addShot(Shot shot);
	}
}
