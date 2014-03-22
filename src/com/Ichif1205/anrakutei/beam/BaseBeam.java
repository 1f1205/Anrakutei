package com.Ichif1205.anrakutei.beam;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class BaseBeam {

	/**
	 * ビームの横幅 TODO staticにすると継承先で値を変更できない・・・？
	 */
	protected int WIDTH = 5;

	/**
	 * ビームの縦幅 TODO staticにすると継承先で値を変更できない・・・？
	 */
	protected int HEIGHT = 20;

	/**
	 * X座標
	 */
	protected float mPositionX;

	/**
	 * Y座標
	 */
	protected float mPositionY;

	/**
	 * ビームの速度
	 */
	protected int mSpeed;

	public BaseBeam(float x, float y) {
		mPositionX = x;
		mPositionY = y;
		mSpeed = fixSpeed();
	}
	
	/**
	 * @param x X座標
	 * @param y Y座業
	 * @param speed 速度
	 */
	public BaseBeam(float x, float y, int speed) {
		mPositionX = x;
		mPositionY = y;
		mSpeed = speed;
	}
	
	/**
	 * X座標を取得する　
	 * @return
	 */
	public float getPositionX() {
		return mPositionX;
	}
	
	/**
	 * Y座標を取得する
	 * @return
	 */
	public float getPositionY() {
		return mPositionY;
	}
	
	/**
	 * ビームの速度を決定する
	 * @return ビームのスピード
	 */
	protected int fixSpeed() {
		final Random rand = new Random();
		return rand.nextInt(3) + 3;
	}

	/**
	 * ビームの位置を更新する
	 */
	protected abstract void updatePosition();

	/**
	 * ビームを描画する
	 * 
	 * @param canvas
	 * @param paint
	 */
	public void onDraw(Canvas canvas, Paint paint, int windowHeight) {
		if (!isInsideScreen(windowHeight)) {
			// ビームがウィンドウ内になかったら描画しない
			return;
		}

		updatePosition();
		final RectF rectf = createRectangle();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(rectf, paint);
	}
	
	/**
	 * ビームがウィンドウ内部にいるか判定する
	 * 
	 * @param windowHeight
	 * @return true:画面内 false:画面外
	 */
	private boolean isInsideScreen(int windowHeight) {
		if (mPositionY <= windowHeight && mPositionY >= 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * ビームを消す
	 */
	public void remove() {
		mPositionY = -1;
	}

	/**
	 * ビームの図形を返す
	 * 
	 * @return
	 */
	private RectF createRectangle() {
		return new RectF(mPositionX - WIDTH / 2, mPositionY - HEIGHT / 2,
				mPositionX + WIDTH / 2, mPositionY + HEIGHT / 2);
	}
}
