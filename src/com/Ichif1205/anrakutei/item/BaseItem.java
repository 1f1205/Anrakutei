package com.Ichif1205.anrakutei.item;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;

import com.Ichif1205.anrakutei.Player;

public abstract class BaseItem {
	@SuppressWarnings("unused")
	private static final String TAG = BaseItem.class.getSimpleName();
	/**
	 * 総移動パターン
	 */
	private static final int MOVE_PATTERN = 4;

	/**
	 * アイテム画像の横幅
	 */
	private static final int ITEM_IMAGE_WIDTH = 36;

	/**
	 * アイテム画像の縦幅
	 */
	private static final int ITEM_IMAGE_HEIGHT = 36;

	/**
	 * アイテムの移動速度
	 */
	private static final int MOVE_SPEED = 4;

	/**
	 * アイテムのBitmap
	 */
	private Bitmap mItemBitmap;

	/**
	 * アイテムの位置(X座標)
	 */
	private float mPositionX;

	/**
	 * アイテムの位置(Y座標)
	 */
	private float mPositionY;
	
	/**
	 * 移動パターン
	 */
	private int mMovePattern;
	
	/**
	 * X座標の中心位置
	 */
	private float mCenterX;
	
	private float mDynamic;
	
	protected final ItemMediator mMediator;

	/**
	 * @param positionX
	 *            アイテムの表示開始位置(X座標)
	 * @param positionY
	 *            アイテムの表示開始位置(Y座標)
	 */
	public BaseItem(Context context, ItemMediator mediator, float positionX, float positionY) {
		mCenterX = positionX;
		mMediator = mediator;
		mPositionX = positionX;
		mPositionY = positionY;

		mItemBitmap = createBitmap(context);
		mMovePattern = fixedMovePattern();
	}
	
	public float getPositionX() {
		return mPositionX;
	}
	
	public float getPositionY() {
		return mPositionY;
	}

	/**
	 * アイテムの位置を更新する
	 */
	protected void updatePosition() {
		mPositionY += MOVE_SPEED;

		switch (mMovePattern) {
		case 0:
			break;
		case 1:
			mPositionX = 60 * FloatMath.cos(mPositionY / 60) + mCenterX;
			break;
		case 2:
			mDynamic += MOVE_SPEED * (float) Math.tan(Math.PI / 9);
			mPositionX = mDynamic + mCenterX;
			break;
		default:
			mDynamic += MOVE_SPEED * (float) Math.tan(Math.PI / 9);
			mPositionX = -mDynamic + mCenterX;
			break;
		}
	}
	
	/**
	 * 移動パターンを決定する
	 * 
	 * @return
	 */
	private int fixedMovePattern() {
		final Random ptn_rand = new Random();
		return ptn_rand.nextInt(MOVE_PATTERN);
	}

	/**
	 * アイテムの効果を適応する
	 */
	public abstract void adjustEffective();
	
	/**
	 * アイテムを描画する
	 * 
	 * @param canvas
	 * @param paint
	 */
	public void onDraw(Canvas canvas, Paint paint, Player player, int windowHeight) {
		if (!isInsideScreen(windowHeight)) {
			// 画面外なら終了処理
			remove();
			return;
		}
		
		if (isGetItem(player)) {
			adjustEffective();
			remove();
			return;
		}

		updatePosition();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawBitmap(mItemBitmap, mPositionX, mPositionY, paint);
	}
	
	/**
	 * アイテムの当たり判定
	 * 
	 * @param player
	 * @return
	 */
	private boolean isGetItem(Player player) {
		if ((player.getPlayerPosX() - player.getWidth() / 2 <= mPositionX && player.getPlayerPosX() + player.getWidth() / 2 >= mPositionX)
				&& (player.getPlayerPosY() - player.getHeight() <= mPositionY && player.getPlayerPosY() + player.getHeight() >= mPositionY)) {
			return true;
		}
		return false;
	}
	

	/**
	 * アイテムのリソースを返す
	 * 
	 * @return
	 */
	protected abstract int getItemResource();

	/**
	 * アイテムのリソースからBitmapを生成する
	 * 
	 * @param context
	 * @return
	 */
	private Bitmap createBitmap(Context context) {
		final Bitmap image = BitmapFactory.decodeResource(
				context.getResources(), getItemResource());
		return Bitmap.createScaledBitmap(image, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
	}

	/**
	 * アイテムを消す
	 */
	public void remove() {
		mPositionY = -1;
		mItemBitmap = null;
	}

	/**
	 * 画面内か判定する
	 * 
	 * @param windowHeight
	 * @return true:画面内 false:画面外
	 */
	private boolean isInsideScreen(int windowHeight) {
		if (0 <= mPositionY && mPositionY <= windowHeight) {
			return true;
		}

		return false;
	}

}
