package com.Ichif1205.anrakutei;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class FieldSurfaceView extends SurfaceView 
		implements SurfaceHolder.Callback, Runnable{

	private boolean isAttached;
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private Paint mPaint;
	//private Path mPath;
	private Player mPlayer;
	private Shot mShot;
	private Thread mThread;
	
	public FieldSurfaceView(Context context) {
		super(context);
		isAttached = true;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setFixedSize(getWidth(), getHeight());
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		mThread = new Thread(this);
		mThread.start();
		
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);
		
		mPlayer = new Player();
		mPlayer.setPlayerPosX(getWidth()/2);		
		mPlayer.setPlayerPosY(getHeight()*7/8);		
		
		mShot = new Shot();
		mShot.setShotPosX(getWidth()/2);		
		mShot.setShotPosY(getHeight()*6/8);		
		
		onDraw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		isAttached = false;
		mThread = null;
	}
	
	protected void onDraw() {
		mCanvas = getHolder().lockCanvas();
		mCanvas.drawColor(Color.BLACK);
		drawPlayer();
		float shotPosY = mShot.getShotPosY();
		int shotVy = mShot.getShotSpeed();
		if (shotPosY > 0) {
			shotPosY -= shotVy;
			drawShot();
			mShot.setShotPosY(shotPosY);
		}
		getHolder().unlockCanvasAndPost(mCanvas);
	}
	
	//自機描画
	protected void drawPlayer() {
		float posX = mPlayer.getPlayerPosX();
		float posY = mPlayer.getPlayerPosY();
		int width  = mPlayer.getPlayerWidth();
		int height = mPlayer.getPlayerHeight();
		

		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		Path path = new Path();
		path.moveTo(posX, posY);
		//左下から反時計回りに描画
		path.lineTo(posX-width/2, posY+height/4);
		path.lineTo(posX+width/2, posY+height/4);
		path.lineTo(posX+width/2, posY-height/4);
		path.lineTo(posX+width/6, posY-height/4);
		path.lineTo(posX+width/6, posY-height*3/4);
		path.lineTo(posX-width/6, posY-height*3/4);
		path.lineTo(posX-width/6, posY-height/4);
		path.lineTo(posX-width/2, posY-height/4);
		path.lineTo(posX-width/2, posY+height/4);
		mCanvas.drawPath(path, mPaint);
	}
	
	//弾描画
	protected void drawShot() {
		float shotPosX = mShot.getShotPosX(); 
		float shotPosY = mShot.getShotPosY(); 
		int shotW = mShot.getShotWidth(); 
		int shotH = mShot.getShotHeight();

		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		RectF rectf = new RectF(shotPosX-shotW/2, shotPosY-shotH/2, 
				shotPosX+shotW/2, shotPosY+shotH/2);
		mCanvas.drawRect(rectf, mPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//mPlayer.setPlayerPosX(event.getX());
			//弾発射
			mShot = new Shot();
			mShot.setShotPosX(mPlayer.getPlayerPosX());
			mShot.setShotPosY(mPlayer.getPlayerPosY());
			
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mPlayer.setPlayerPosX(event.getX());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//mPlayer.setPlayerPosX(event.getX());
		} 

		return true;
	}
	
	@Override
	public void run() {			
		while (isAttached) {
			onDraw();
		}
	}

}
