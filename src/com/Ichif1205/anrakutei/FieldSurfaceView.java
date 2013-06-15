package com.Ichif1205.anrakutei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
	private final Context mContext;
	private int MAX_INVADER_NUM;
	private int[] INVADER_TYPE;
	private SurfaceHolder mHolder;
	private Canvas mCanvas = null;
	private Paint mPaint;
	private Player mPlayer;
	private static double PLAYER_INIT_WIDTH_RATE = 0.5;
	private static double PLAYER_INIT_HEIGHT_RATE = 0.84375; // =27/32
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
	private Bitmap mInvImage6 = BitmapFactory.decodeResource(getResources(),
			R.drawable.invader6);
	public static int INV_IMAGE_WIDTH = 36;
	public static int INV_IMAGE_HEIGHT = 36;
	public static int INVBOSS_IMAGE_WIDTH = 72;
	public static int INVBOSS_IMAGE_HEIGHT = 72;
	private Bitmap mItemMImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item1);
	private Bitmap mItemBImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item2);
	private Bitmap mItemSImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item3);
	private Bitmap mItemGImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item4);
	private Bitmap mItemPImage = BitmapFactory.decodeResource(getResources(),
			R.drawable.item5);
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
	private int mDestoryInvaderCount = 0;
	private String item_pattern; // アイテムの種類
	HashMap<String, String> mItemInfo;
	private int mScore = 0;
	private Handler mHandler;
	private int mItemM = 0;
	public int mItemB = 0;
	public int mItemS = 0;
	public int mItemG = 0;
	
	//HP
	private Bitmap mItemHP5 = BitmapFactory.decodeResource(getResources(),
			R.drawable.hp5);
	private Bitmap mItemHP4 = BitmapFactory.decodeResource(getResources(),
			R.drawable.hp4);
	private Bitmap mItemHP3 = BitmapFactory.decodeResource(getResources(),
			R.drawable.hp3);
	private Bitmap mItemHP2 = BitmapFactory.decodeResource(getResources(),
			R.drawable.hp2);
	private Bitmap mItemHP1 = BitmapFactory.decodeResource(getResources(),
			R.drawable.hp1);
	private int BossHP5;

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

	/**
	 * ステージ情報をセット
	 * 
	 * @param info
	 */
	public void setStageInfo(StageInfo info) {
		MAX_INVADER_NUM = info.maxInvader;
		INVADER_TYPE = info.invTypeArray;
		//INVADER_TYPE = new int[] {0, 1, 2, 3, 4};
	}

	/**
	 * 前のステージのスコアを引き継ぐ
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		mScore = score;
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
		mPlayer = new Player((int) (getWidth() * PLAYER_INIT_WIDTH_RATE),
				(int) (getHeight() * PLAYER_INIT_HEIGHT_RATE));
		mInvBeamList = new ArrayList<InvaderBeam>();
		mShotList = new ArrayList<Shot>();
		mInvaderList = new ArrayList<Invader>();
		mItemList = new ArrayList<Item>();
		mItemInfo = new HashMap<String, String>();
		// 複数の敵を表示
		for (int i = 0; i < MAX_INVADER_NUM; i++) {
			Invader invader = new Invader(getWidth(), getHeight() / 2, INVADER_TYPE[i], this);
			mInvaderList.add(invader);
		}
		// 　敵の画像セット
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
		mInvImage6 = Bitmap.createScaledBitmap(mInvImage6, INVBOSS_IMAGE_WIDTH,
				INVBOSS_IMAGE_HEIGHT, true);
		// アイテム
		mItemMImage = Bitmap.createScaledBitmap(mItemMImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemBImage = Bitmap.createScaledBitmap(mItemBImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemSImage = Bitmap.createScaledBitmap(mItemSImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemGImage = Bitmap.createScaledBitmap(mItemGImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemPImage = Bitmap.createScaledBitmap(mItemPImage, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		bgm.setLooping(true);
		bgm.start();
		
		//HP
		mItemHP5 = Bitmap.createScaledBitmap(mItemHP5, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemHP4 = Bitmap.createScaledBitmap(mItemHP4, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemHP3 = Bitmap.createScaledBitmap(mItemHP3, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemHP2 = Bitmap.createScaledBitmap(mItemHP2, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);
		mItemHP1 = Bitmap.createScaledBitmap(mItemHP1, ITEM_IMAGE_WIDTH,
				ITEM_IMAGE_HEIGHT, true);

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
				for (final Invader invader : mInvaderList) {
					boolean invIsShooted = invader.isShooted(
							shot.getShotPosX(), shot.getShotPosY());
					// 弾が敵に当たったら消える
					if (invIsShooted) {
						int invType = invader.getInvType();
						Log.d("itemPattern", "itemnum"+invType);
						invader.ItemAdd();
						shot.remove();
						invader.remove();
						mDestoryInvaderCount++;
						mHandler.post(new Runnable() {
							public void run() {
								mGameListener.addScore(invader.getPoint());
							}
						});
						if (MAX_INVADER_NUM == mDestoryInvaderCount) {
							// 次のステージへ遷移
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									bgm.stop();
									mGameListener.nextStage(mScore);
								}
							});
						}

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
		for (int i = 0; i < mItemList.size(); i++) {
			Item item = mItemList.get(i);
			String itemPattern = mItemInfo.get(String.valueOf(i));
			boolean pIsShooted = mPlayer.isItemted(item.getItemPosX(),
					item.getItemPosY());
			// アイテムが自機に当たったら消える
			if (pIsShooted) {
				item.remove();
				if (itemPattern == "M") {
					mItemM = 1;
				} else if (itemPattern == "B") {
					mItemB = 1;
				} else if (itemPattern == "S") {
					mItemS = 1;
				} else if (itemPattern == "G") {
					mItemG = 1;
				} else if (itemPattern == "P") {
					Random ptn_rand = new Random();
					int plus_score = ptn_rand.nextInt(5) * 100;
					mScore += plus_score;
				}
			}
			// アイテムが画面上からはみ出るまで表示させ続ける
			if (item.isInsideScreen(getHeight())) {
				String num = String.valueOf(i);
				drawItem(item, num);
			}
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
				mPlayer.drawGurd(path, getWidth() / 2, getHeight() * 3 / 4),
				mPaint);
	}

	// 敵描画
	protected void drawInvader(Invader invader) {
		if (invader.isOverBoundaryWidth(getWidth())) {
			invader.reverseSpeedXDirection();
		}
		if (invader
				.isOverBoundaryHeight((int) (getHeight() * PLAYER_INIT_HEIGHT_RATE))) {
			invader.reverseSpeedYDirection();
		}
		invader.updatePosition();

		int invType = invader.getInvType();
		if (invType == Invader.INV_PURPLE) {
			mBitmap = mInvImage1;
		} else if (invType == Invader.INV_YELLOW) {
			mBitmap = mInvImage2;
		} else if (invType == Invader.INV_LIGHTBLUE) {
			mBitmap = mInvImage3;
		} else if (invType == Invader.INV_ORANGE) {
			mBitmap = mInvImage4;
		} else if (invType == Invader.INV_GREEN) {
			mBitmap = mInvImage5;
		} else {
			mBitmap = mInvImage6;
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
	protected void drawItem(Item item, String num) {
		item.updatePosition();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		String itemPattern = mItemInfo.get(num);
		if (itemPattern == "M") {
			mCanvas.drawBitmap(mItemMImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (itemPattern == "B") {
			mCanvas.drawBitmap(mItemBImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (itemPattern == "S") {
			mCanvas.drawBitmap(mItemSImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (itemPattern == "G") {
			mCanvas.drawBitmap(mItemGImage, item.getItemPosX(),
					item.getItemPosY(), mPaint);
		} else if (itemPattern == "P") {
			mCanvas.drawBitmap(mItemPImage, item.getItemPosX(),
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
	public void shootBeamEvent(float shotX, float shotY, int invType) {
		if (mThread == null) {
			return;
		}
		Log.d(TAG, "BEAM EVENT");
		if (invType == Invader.INV_BOSS) {
			InvaderBeam invBeam = new InvaderBeam(shotX, shotY
					+ INVBOSS_IMAGE_HEIGHT / 2, invType);
			mInvBeamList.add(invBeam);
			InvaderBeam invBeam2 = new InvaderBeam(shotX + INVBOSS_IMAGE_WIDTH
					/ 2, shotY + INVBOSS_IMAGE_HEIGHT / 2, invType);
			mInvBeamList.add(invBeam2);
			InvaderBeam invBeam3 = new InvaderBeam(shotX + INVBOSS_IMAGE_WIDTH,
					shotY + INVBOSS_IMAGE_HEIGHT / 2, invType);
			mInvBeamList.add(invBeam3);
		} else {
			InvaderBeam invBeam = new InvaderBeam(shotX + INV_IMAGE_WIDTH / 2,
					shotY + INV_IMAGE_HEIGHT, invType);
			mInvBeamList.add(invBeam);
		}
	}

	@Override
	public void Item(float shotX, float shotY) {
		Item item = new Item(shotX, shotY);
		mItemList.add(item);
		String num = String.valueOf(mItemList.size());
		if (!mItemInfo.containsKey(num)) {
			item_pattern = item.selectItem();
			mItemInfo.put(num, item_pattern);
		}
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

		/**
		 * 次のステージへのフラグ
		 */
		public void nextStage(int score);
	}

}
