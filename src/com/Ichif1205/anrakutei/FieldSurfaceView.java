package com.Ichif1205.anrakutei;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private Invader mInvader;
	private Bitmap mBitmap;
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
		
		mInvader = new Invader();
		mInvader.setInvaderPosX(getWidth()/8);		
		mInvader.setInvaderPosY(getHeight()/8);		
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.suraimu);
		
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
		drawInvader();
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
		Path path = new Path();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawPath(mPlayer.draw(path), mPaint);
	}
	//敵描画
	protected void drawInvader() {
		float invPosX = mInvader.getInvaderPosX();
		float invPosY = mInvader.getInvaderPosY();
		int invSpeed = mInvader.getInvaderSpeed();
		invPosX += invSpeed;
		mInvader.setInvaderPosX(invPosX);
		if (invPosX > getWidth() || invPosX < 0) {
			invSpeed = -invSpeed;
			mInvader.setInvaderSpeed(invSpeed);
		}
		mCanvas.drawBitmap(mBitmap, invPosX, invPosY, mPaint);
	}
	//弾描画
	protected void drawShot(Shot shot) {
		float shotPosX = shot.getShotPosX(); 
		float shotPosY = shot.getShotPosY(); 
		int shotW = shot.getShotWidth(); 
		int shotH = shot.getShotHeight();

		RectF rectf = new RectF(shotPosX-shotW/2, shotPosY-shotH/2, 
				shotPosX+shotW/2, shotPosY+shotH/2);;
		
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
