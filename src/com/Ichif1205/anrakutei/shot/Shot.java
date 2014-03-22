package com.Ichif1205.anrakutei.shot;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Shot {
	private static final int MOVE_SPEED = 20;
	protected static final int SHOT_WIDTH = 5;
	protected static final int SHOT_HEIGHT = 20;

	private float posX;
	private float posY;

	public Shot(float x, float y) {
		posX = x;
		posY = y;
	}

	public float getShotPosX() {
		return posX;
	}

	public float getShotPosY() {
		return posY;
	}
	
	/**
	 * 弾を描画する 
	 * 
	 * @param canvas
	 * @param paint
	 * @param isItemB
	 */
	public void onDraw(Canvas canvas, Paint paint) {
		if (getShotPosY() <= 0) {
			return;
		}

		updatePosition();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(createRectangle(), paint);
	}

	/**
	 * 弾のスピード
	 */
	protected void updatePosition() {
		posY -= MOVE_SPEED;
	}

	/**
	 * 弾の図形を返す
	 * 
	 * @param isItemb
	 * @return
	 */
	protected RectF createRectangle() {

		final RectF rectf = new RectF(posX - SHOT_WIDTH/ 2, posY - SHOT_HEIGHT/ 2, posX
				+ SHOT_WIDTH / 2, posY + SHOT_HEIGHT / 2);
		return rectf;
	}

	public void remove() {
		posY = 0;
	}

}
