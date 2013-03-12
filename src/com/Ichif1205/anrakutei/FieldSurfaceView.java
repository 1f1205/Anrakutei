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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.Ichif1205.anrakutei.Invader.InvarderListener;

public class FieldSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable, InvarderListener {
	private final String TAG = FieldSurfaceView.class.getSimpleName();
	private final int MAX_INVADER_NUM = 9;

	private SurfaceHolder mHolder;
	private Canvas mCanvas = null;
	private Paint mPaint;
	private Player mPlayer;
	private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader);
	private Thread mThread;
	private ArrayList<Shot> mShotList;
	private ArrayList<InvaderBeam> mInvBeamList;
	private ArrayList<Invader> mInvaderList;
	private ArrayList<Item> mItem;

	public FieldSurfaceView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setFixedSize(getWidth(), getHeight());
	}

	public FieldSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		mThread = new Thread(this);

		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);

		mPlayer = new Player(getWidth() / 2, getHeight() * 7 / 8);
		mInvBeamList = new ArrayList<InvaderBeam>();
		mShotList = new ArrayList<Shot>();
		mInvaderList = new ArrayList<Invader>();
		mItem = new ArrayList<Item>();
		// 複数の敵を表示
		for (int i = 2; i < MAX_INVADER_NUM; i++) {
			Invader invader = new Invader(getWidth(), getHeight() * 3 / 4, this);
			mInvaderList.add(invader);
		}
		mBitmap = Bitmap.createScaledBitmap(mBitmap, 36, 36, true);

		mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		mThread = null;
	}

	protected void onDraw() {
		mCanvas = getHolder().lockCanvas();
		if (mCanvas == null) {
			return;
		}
		mCanvas.drawColor(Color.BLACK);
		// 自機の描画
		if (mPlayer.getPlayerExistFlag()) {
			drawPlayer();
		}
		// 自機の弾の描画
		for (int i = 0; i < mShotList.size(); i++) {
			Shot shot = mShotList.get(i);
			synchronized (mInvaderList) {
				for (Invader invader : mInvaderList) {
					boolean invIsShooted = invader.isShooted(
							shot.getShotPosX(), shot.getShotPosY());
					// 弾が敵に当たったら消える
					if (invIsShooted) {
						shot.remove();
						invader.remove();
					}
				}
			}
			// 弾が画面上からはみ出るまで表示させ続ける
			if (shot.getShotPosY() > 0) {
				drawShot(shot);
			}
		}
		synchronized (mInvaderList) {
			for (Invader invader : mInvaderList) {
				// 敵の描画
				if (invader.isExisted()) {
					drawInvader(invader);
				}
			}
		}
		// 敵ビームの描画
		for (int i = 0; i < mInvBeamList.size(); i++) {
			InvaderBeam invBeam = mInvBeamList.get(i);
			boolean pIsShooted = mPlayer.isShooted(invBeam.getInvBeamPosX(),
					invBeam.getInvBeamPosY());
			// ビームが自機に当たったら消える
			if (pIsShooted) {
				invBeam.remove();
				mPlayer.remove();
			}
			// ビームが画面上からはみ出るまで表示させ続ける
			if (invBeam.isInsideScreen(getHeight())) {
				drawInvBeam(invBeam);
			}
		}
		getHolder().unlockCanvasAndPost(mCanvas);
	}

	// 自機描画
	protected void drawPlayer() {
		Path path = new Path();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawPath(mPlayer.draw(path), mPaint);
	}

	// 敵描画
	protected void drawInvader(Invader invader) {
		if (invader.isOverBoundaryWidth(getWidth())) {
			invader.reverseSpeedXDirection();
		}
		if (invader.isOverBoundaryHeight(getHeight())) {
			invader.reverseSpeedYDirection();
		}
		invader.updatePosition();

		mCanvas.drawBitmap(mBitmap, invader.getInvaderPosX(),
				invader.getInvaderPosY(), mPaint);
	}

	// 自機の弾描画
	protected void drawShot(Shot shot) {
		shot.updatePosition();
		RectF rectf = shot.createRectangle();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawRect(rectf, mPaint);
	}

	// 敵のビーム描画
	protected void drawInvBeam(InvaderBeam invBeam) {
		invBeam.updatePosition();
		RectF rectf = invBeam.createRectangle();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawRect(rectf, mPaint);
	}

	// Item生成
	protected void drawItem(Item item) {
		RectF rectf = item.createRectangle();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawRect(rectf, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 弾発射
			Shot shot = new Shot(mPlayer.getPlayerPosX(),
					mPlayer.getPlayerPosY());
			mShotList.add(shot);
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mPlayer.setPlayerPosX(event.getX());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// mPlayer.setPlayerPosX(event.getX());
		}

		return true;
	}

	@Override
	public void run() {
		while (mThread != null) {
			onDraw();
		}
	}

	@Override
	public void shootBeamEvent(float shotX, float shotY) {
		InvaderBeam invBeam = new InvaderBeam(shotX, shotY);
		mInvBeamList.add(invBeam);
	}

}
