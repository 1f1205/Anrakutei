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

	private float posX;
	private float posY;

	private final int mWindowWidth;
	private final int mWindowHeight;
	
	private int width = 50;
	private int height = 30;
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
		return width;
	}
	
	public int getHeight() {
		return height;
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
		
		final Path path = new Path();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		if (mIsGuard) {
			// ガードの描画
			canvas.drawPath(
					drawGurd(path, mWindowWidth / 2, mWindowHeight * 3 / 4),
					paint);
		}
		
		canvas.drawPath(draw(path), paint);
	}
	
	private Path draw(Path path) {
		// itemSをとった場合
		if (mItemS) {
			width = 15;
		}

		path.moveTo(posX, posY);
		// 左下から反時計回りに描画
		path.lineTo(posX - width / 2, posY + height / 4);
		path.lineTo(posX + width / 2, posY + height / 4);
		path.lineTo(posX + width / 2, posY - height / 4);
		path.lineTo(posX + width / 6, posY - height / 4);
		path.lineTo(posX + width / 6, posY - height * 3 / 4);
		path.lineTo(posX - width / 6, posY - height * 3 / 4);
		path.lineTo(posX - width / 6, posY - height / 4);
		path.lineTo(posX - width / 2, posY - height / 4);
		path.lineTo(posX - width / 2, posY + height / 4);

		return path;
	}

	// ガード描画のパス
	private Path drawGurd(Path path, float x, float y) {
		gurdX = x;
		gurdY = y;
		path.moveTo(gurdX, gurdY);
		// 左下から反時計回りに描画
		path.lineTo(gurdX - width, gurdY + height / 4);
		path.lineTo(gurdX + width, gurdY + height / 4);
		path.lineTo(gurdX + width, gurdY - height / 4);
		path.lineTo(gurdX + width, gurdY - height / 4);
		path.lineTo(gurdX + width, gurdY - height * 3 / 4);
		path.lineTo(gurdX - width, gurdY - height * 3 / 4);
		path.lineTo(gurdX - width, gurdY - height / 4);
		path.lineTo(gurdX - width, gurdY - height / 4);
		path.lineTo(gurdX - width, gurdY + height / 4);

		return path;
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((posX - width / 2 <= shotX && posX + width / 2 >= shotX)
				&& (posY - height / 2 <= shotY && posY + height / 2 >= shotY)) {
			return true;
		}
		return false;
	}

	// guard判定
	public boolean isGurded(float shotX, float shotY) {
		if ((gurdX - width <= shotX && gurdX + width >= shotX)
				&& (gurdY - height <= shotY && gurdY + height >= shotY)) {
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
