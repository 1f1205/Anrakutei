package com.Ichif1205.anrakutei;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
	private Player mPlayer;
	private Thread mThread;
	private ArrayList <Shot> mShotList;
	
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
		
		mShotList = new ArrayList<Shot>();
		
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
		for (int i=0; i<mShotList.size(); i++) {
			Shot shot = mShotList.get(i);
			float shotPosY = shot.getShotPosY();
			int shotVy = shot.getShotSpeed();
			if (shotPosY > 0) {
				shotPosY -= shotVy;
				drawShot(shot);
				shot.setShotPosY(shotPosY);
			}
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
	protected void drawShot(Shot shot) {
		float shotPosX = shot.getShotPosX(); 
		float shotPosY = shot.getShotPosY(); 
		int shotW = shot.getShotWidth(); 
		int shotH = shot.getShotHeight();

		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		RectF rectf = new RectF(shotPosX-shotW/2, shotPosY-shotH/2, 
				shotPosX+shotW/2, shotPosY+shotH/2);
		mCanvas.drawRect(rectf, mPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//弾発射
			Shot shot = new Shot();
			shot.setShotPosX(mPlayer.getPlayerPosX());
			shot.setShotPosY(mPlayer.getPlayerPosY());
			mShotList.add(shot);
			
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
