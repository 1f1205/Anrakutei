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
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.Ichif1205.anrakutei.Invader.InvarderListener;

public class FieldSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable, InvarderListener {
	private final String TAG = FieldSurfaceView.class.getSimpleName();
	private final int MAX_INVADER_NUM = 9;

	private SurfaceHolder mHolder;
	private Context mContext;
	private Canvas mCanvas = null;
	private Paint mPaint;
	private Player mPlayer;
	private static double PLAYER_INIT_WIDTH_RATE = 0.5;
	private static double PLAYER_INIT_HEIGHT_RATE = 0.84375;
	private Bitmap mBitmap;
	private Bitmap mInvImage1 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader);
	private Bitmap mInvImage2 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader2);
	private Bitmap mInvImage3 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader3);
	private Bitmap mInvImage4 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader4);
	private Bitmap mInvImage5 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader5);
	public static int INV_IMAGE_WIDTH = 36;
	public static int INV_IMAGE_HEIGHT = 36;
	private Bitmap mItemMImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item1);
	private Bitmap mItemBImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item2);
	private Bitmap mItemSImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item3);
	private Bitmap mItemGImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item4);
	public static int ITEM_IMAGE_WIDTH = 36;
	public static int ITEM_IMAGE_HEIGHT = 36;
	private Thread mThread;
	private ArrayList<Shot> mShotList;
	private ArrayList<InvaderBeam> mInvBeamList;
	private ArrayList<Invader> mInvaderList;
	private ArrayList<Item> mItemList;
	private Status mStatus = null; // ゲームの状態を保存
	private boolean mPauseFlg = false;
	private GameEventLiestener mGameListener = null;
	private int mItemFlg = 0;
	private String item_pattern; // アイテムの種類
	private TextView mScoreView;
	private int mScore;
	private Handler mHandler;
	private int mItemPos;
	private int mItemM = 0;
	public int mItemB = 0;
	public int mItemS = 0;
	public int mItemG = 0;

	MediaPlayer bgm = MediaPlayer.create(getContext(), R.raw.bgm);
	MediaPlayer se = MediaPlayer.create(getContext(), R.raw.shot);

	private boolean mExecFlg = true;

	public FieldSurfaceView(Context context) {
		super(context);
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setFixedSize(getWidth(), getHeight());
	}

	public FieldSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setFixedSize(getWidth(), getHeight());
		Log.d(TAG, "Constract");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Change Surface");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Create SurfaceView");

		// ゲーム状態を取得
		if (mStatus == null) {
			init();
		} else {
			setGameStates();
		}

		// 一時停止中か確認
		if (!mPauseFlg) {
			mThread.start();
		}
	}

	/**
	 * 初期化
	 */
	public void init() {
		mThread = new Thread(this);
		Log.d(TAG, "init");
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);
		mPlayer = new Player((int)(getWidth() * PLAYER_INIT_WIDTH_RATE),
				(int) (getHeight() * PLAYER_INIT_HEIGHT_RATE));
		mInvBeamList = new ArrayList<InvaderBeam>();
		mShotList = new ArrayList<Shot>();
		mInvaderList = new ArrayList<Invader>();
		mItemList = new ArrayList<Item>();
		// 複数の敵を表示
		for (int i = 2; i < MAX_INVADER_NUM; i++) {
			Invader invader = new Invader(getWidth(), getHeight() * 3 / 4, this);
			mInvaderList.add(invader);
		}
		// 　敵をランダムで決定
		mInvImage1 = Bitmap.createScaledBitmap(mInvImage1, INV_IMAGE_WIDTH,
				INV_IMAGE_HEIGHT, true);
		mInvImage2 = Bitmap.createScaledBitmap(mInvImage2, INV_IMAGE_WIDTH,
				INV_IMAGE_HEIGHT, true);
		mInvImage3 = Bitmap.createScaledBitmap(mInvImage3, INV_IMAGE_WIDTH,
				INV_IMAGE_HEIGHT, true);
		mInvImage4 = Bitmap.createScaledBitmap(mInvImage4, INV_IMAGE_WIDTH,
				INV_IMAGE_HEIGHT, true);
		mInvImage5 = Bitmap.createScaledBitmap(mInvImage5, INV_IMAGE_WIDTH,
				INV_IMAGE_HEIGHT, true);
		// アイテム
		mItemMImage = Bitmap.createScaledBitmap(mItemMImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemBImage = Bitmap.createScaledBitmap(mItemBImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemSImage = Bitmap.createScaledBitmap(mItemSImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemGImage = Bitmap.createScaledBitmap(mItemGImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		bgm.setLooping(true);
		bgm.start();

		mHandler = new Handler();
	}

	/**
	 * ゲームの状態をセット
	 */
	private void setGameStates() {
		Log.d(TAG, "setGameStates");
		mBitmap = mStatus.bitmap;
		mShotList = mStatus.shotList;
		mInvBeamList = mStatus.invBeamList;
		mInvaderList = mStatus.invaderList;
		mItemList = mStatus.item;
		mPauseFlg = mStatus.pauseFlg;
		mStatus = null;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mExecFlg = false;
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
		// ガードの描画
		if (mItemG == 1) {
			drawGurd();
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
						invader.ItemDrop();
						// mItemFlg = 1;
						mItemPos = 1;
						shot.remove();
						invader.remove();
						mHandler.post(new Runnable() {
							public void run() {
								mScore += 1000;
								// mGameListener.addScore(mScore);
								mScoreView.setText(Integer.toString(mScore));
							}
						});
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
			boolean pIsGurded = mPlayer.isGurded(invBeam.getInvBeamPosX(),
					invBeam.getInvBeamPosY());
			// ビームが自機に当たったら消える
			if (pIsShooted) {
				invBeam.remove();
				mPlayer.remove();
				bgm.stop();
				mGameListener.endGame(mScore);
			}
			// ガードにあたったら消える
			if (pIsGurded) {
				invBeam.remove();
			}
			// ビームが画面上からはみ出るまで表示させ続ける
			if (invBeam.isInsideScreen(getHeight())) {
				drawInvBeam(invBeam);
			}
		}
		// ItemMをとった場合
		if (mItemM == 1) {
			// 敵ビームの描画
			for (int i = 0; i < mInvBeamList.size(); i++) {
				InvaderBeam invBeam = mInvBeamList.get(i);
				invBeam.remove();
			}
			mItemM = 0;
		}
		// アイテムの描画
		// if (mItemFlg == 1) {
		for (int i = 0; i < mItemList.size(); i++) {
			if (mItemPos == 1) {
				Item item = mItemList.get(i);
				boolean pIsShooted = mPlayer.isItemted(item.getItemPosX(),
						item.getItemPosY());
				// アイテムが自機に当たったら消える
				if (pIsShooted) {
					item.remove();
					if (item_pattern == "M") {
						mItemM = 1;
						mItemPos = 0;
					} else if (item_pattern == "B") {
						mItemB = 1;
						mItemPos = 0;
					} else if (item_pattern == "S") {
						mItemS = 1;
						mItemPos = 0;
					} else if (item_pattern == "G") {
						mItemG = 1;
						mItemPos = 0;
					}
					mItemFlg = 0;
				}
				// ビームが画面上からはみ出るまで表示させ続ける
				if (item.isInsideScreen(getHeight())) {
					drawItem(item);
				}
			}
			// }
		}
		getHolder().unlockCanvasAndPost(mCanvas);
	}

	// 自機描画
	protected void drawPlayer() {
		Path path = new Path();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawPath(mPlayer.draw(path, mItemS), mPaint);
	}

	// ガード描画
	protected void drawGurd() {
		Path path = new Path();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCanvas.drawPath(
				mPlayer.drawGurd(path, getWidth() / 2, getHeight() * 6 / 8),
				mPaint);
	}

	// 敵描画
	protected void drawInvader(Invader invader) {
		if (invader.isOverBoundaryWidth(getWidth())) {
			invader.reverseSpeedXDirection();
		}
		if (invader.isOverBoundaryHeight(getHeight() * 27 / 32)) {
			invader.reverseSpeedYDirection();
		}
		invader.updatePosition();

		int invType = invader.getInvType();
		if (invType == 0) {
			mBitmap = mInvImage1;
		} else if (invType == 1) {
			mBitmap = mInvImage2;
		} else if (invType == 2) {
			mBitmap = mInvImage3;
		} else if (invType == 3){
			mBitmap = mInvImage4;
		} else {
			mBitmap = mInvImage5;
		}
		mCanvas.drawBitmap(mBitmap, invader.getInvaderPosX(),
				invader.getInvaderPosY(), mPaint);
	}

	// 自機の弾描画
	protected void drawShot(Shot shot) {
		shot.updatePosition();
		RectF rectf = shot.createRectangle(mItemB);
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
		item.updatePosition();
		item_pattern = item.selectItem();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		if (item_pattern == "M") {
			mCanvas.drawBitmap(mItemMImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (item_pattern == "B") {
			mCanvas.drawBitmap(mItemBImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (item_pattern == "S") {
			mCanvas.drawBitmap(mItemSImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (item_pattern == "G") {
			mCanvas.drawBitmap(mItemGImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (item_pattern == "G") {
			mCanvas.drawBitmap(mItemGImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			se.start();
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
		while (mExecFlg) {
			onDraw();
		}
	}

	/**
	 * スレッドを再起動
	 */
	public void restartLoop() {
		mPauseFlg = false;
		Log.d(TAG, mInvaderList.size() + "Restart Invader");
		Log.d(TAG, mInvBeamList.size() + "Restart InvBeam");
		mExecFlg = true;
		mThread = new Thread(this);
		mThread.start();
		bgm.start();
	}

	/**
	 * スレッドを一時停止 mThreadを一旦nullに
	 */
	public void endLoop() {
		mPauseFlg = true;
		Log.d(TAG, mInvaderList.size() + ":End Invader");
		Log.d(TAG, mInvBeamList.size() + ":End InvBeam");
		mExecFlg = false;
		mThread = null;
		bgm.pause();
	}

	@Override
	public void shootBeamEvent(float shotX, float shotY) {
		if (mThread == null) {
			return;
		}
		Log.d(TAG, "BEAM EVENT");
		InvaderBeam invBeam = new InvaderBeam(shotX, shotY);
		mInvBeamList.add(invBeam);
	}

	@Override
	public void Item(float shotX, float shotY) {
		Item item = new Item(shotX, shotY);
		mItemList.add(item);
	}

	/**
	 * ゲーム状態を保存する
	 * 
	 * @return
	 */
	public Status saveStatus() {
		mStatus = new Status();
		mStatus.bitmap = mBitmap;
		mStatus.shotList = mShotList;
		mStatus.invBeamList = mInvBeamList;
		mStatus.invaderList = mInvaderList;
		mStatus.item = mItemList;
		mStatus.pauseFlg = mPauseFlg;

		return mStatus;
	}

	/**
	 * Activityからゲーム状態をセットする
	 */
	public void setInstance(Status status) {
		mStatus = status;
	}

	public void setScoreView(TextView tv) {
		mScoreView = tv;
	}

	/**
	 * リスナーをセット
	 * 
	 * @param listener
	 */
	public void setEventListener(GameEventLiestener listener) {
		mGameListener = listener;
	}

	/**
	 * ゲームの各イベントリスナー
	 */
	public interface GameEventLiestener {
		/**
		 * ゲーム終了イベント
		 * 
		 * @param mScore
		 */
		public void endGame(int score);

		/**
		 * スコア増加イベント
		 * 
		 * @param mScore
		 */
		public void addScore(int score);
	}

}
