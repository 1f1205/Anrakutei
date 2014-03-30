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
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.Ichif1205.anrakutei.Player.PlayerEventListener;
import com.Ichif1205.anrakutei.beam.BaseBeam;
import com.Ichif1205.anrakutei.invader.BaseInvader;
import com.Ichif1205.anrakutei.invader.BaseInvader.InvarderListener;
import com.Ichif1205.anrakutei.invader.InvaderFactory;
import com.Ichif1205.anrakutei.item.BaseItem;
import com.Ichif1205.anrakutei.item.ItemFactory;
import com.Ichif1205.anrakutei.item.ItemMediator;
import com.Ichif1205.anrakutei.shot.Shot;

public class FieldSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable, InvarderListener {
	@SuppressWarnings("unused")
	private final String TAG = FieldSurfaceView.class.getSimpleName();
	private final Context mContext;
	private int MAX_INVADER_NUM;
	private ArrayList<Integer> invaderType;
	private SurfaceHolder mHolder;
	private Canvas mCanvas = null;
	private Paint mPaint;
	private Player mPlayer;

	private Thread mThread;
	private ArrayList<Shot> mShotList;
	private ArrayList<BaseBeam> mInvBeamList;
	private ArrayList<BaseInvader> mInvaderList;

	private Status mStatus = null; // ゲームの状態を保存
	private boolean mPauseFlg = false;
	private GameEventLiestener mGameListener = null;
	private int mDestoryInvaderCount = 0;
	
	private ArrayList<BaseItem> mItemList;
	private ItemMediator mMediator;
	
	private int mScore = 0;
	private Handler mHandler;

	MediaPlayer bgm = MediaPlayer.create(getContext(), R.raw.bgm);

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
	}

	/**
	 * ステージ情報をセット
	 * 
	 * @param info
	 */
	public void setStageInfo(StageInfo info) {
		MAX_INVADER_NUM = info.maxInvader;
		invaderType = info.invTypeArray;
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
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

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
	 * 弾を発射した時の動作
	 */
	PlayerEventListener mPlayerEvent = new PlayerEventListener() {

		@Override
		public void addShot(Shot shot) {
			mShotList.add(shot);
		}
	};

	/**
	 * 初期化
	 */
	public void init() {
		mThread = new Thread(this);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);
		mPlayer = new Player(
				(int) (getWidth() * Player.PLAYER_INIT_WIDTH_RATE),
				(int) (getHeight() * Player.PLAYER_INIT_HEIGHT_RATE),
				getWidth(),
				getHeight()
				);
		setOnTouchListener(mPlayer);
		mPlayer.setPlayerListener(mPlayerEvent);

		mInvBeamList = new ArrayList<BaseBeam>();
		mShotList = new ArrayList<Shot>();
		mInvaderList = new ArrayList<BaseInvader>();

		mItemList = new ArrayList<BaseItem>();
		mMediator = new ItemMediator(mPlayer, mInvBeamList);

		// 複数の敵を表示
		for (int i = 0; i < MAX_INVADER_NUM; i++) {
			int invType = invaderType.get(i);
			mInvaderList.add(InvaderFactory.create(mContext, getWidth(),
					getHeight(), this, invType));
		}

		bgm.setLooping(true);
		if (Utils.isRingerEnable(mContext)) {
			// BGMを流す
			bgm.start();
		}

		mHandler = new Handler();
	}

	/**
	 * ゲームの状態をセット
	 */
	private void setGameStates() {
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
		mPlayer.onDraw(mCanvas, mPaint);

		// 自機の弾の描画
		for (int i = 0; i < mShotList.size(); i++) {
			final Shot shot = mShotList.get(i);
			synchronized (mInvaderList) {
				for (final BaseInvader invader : mInvaderList) {
					final boolean invIsShooted = invader.isShooted(shot);

					if (!invIsShooted) {
						continue;
					}

					// 弾が敵に当たったら消える
					shot.remove();

					invader.ItemAdd();
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
			// 弾を描画する
			shot.onDraw(mCanvas, mPaint);
		}
		synchronized (mInvaderList) {
			for (BaseInvader invader : mInvaderList) {
				invader.onDraw(mCanvas, mPaint, mPlayer, getWidth(),
						getHeight());
			}
		}

		// 敵ビームの描画
		for (int i = 0; i < mInvBeamList.size(); i++) {
			BaseBeam invBeam = mInvBeamList.get(i);
			boolean pIsShooted = mPlayer.isShooted(invBeam.getPositionX(),
					invBeam.getPositionY());
			boolean pIsGurded = mPlayer.isGurded(invBeam.getPositionX(),
					invBeam.getPositionY());

			// ビームが自機に当たったら消える
			if (pIsShooted) {
				invBeam.remove();
				mPlayer.remove();
				bgm.stop();
				mGameListener.endGame(mScore, false);
			}
			// ガードにあたったら消える
			if (pIsGurded) {
				invBeam.remove();
			}

			invBeam.onDraw(mCanvas, mPaint, getHeight());
		}

		// アイテムの描画
		for (int i = 0; i < mItemList.size(); i++) {
			final BaseItem item = mItemList.get(i);
			item.onDraw(mCanvas, mPaint, mPlayer, getHeight());
		}

//		for (int i = 0; i < mItemList.size(); i++) {
//			Item item = mItemList.get(i);
//			String itemPattern = mItemInfo.get(String.valueOf(i));
//			boolean pIsShooted = mPlayer.isItemted(item.getItemPosX(),
//					item.getItemPosY());
//			// アイテムが自機に当たったら消える
//			if (pIsShooted) {
//				item.remove();
//				if (itemPattern == "M") {
//					mItemM = 1;
//				} else if (itemPattern == "B") {
//					mPlayer.mItemB = true;
//				} else if (itemPattern == "S") {
//					mItemS = 1;
//				} else if (itemPattern == "G") {
//				} else if (itemPattern == "P") {
//					mHandler.post(new Runnable() {
//						public void run() {
//							Random ptn_rand = new Random();
//							Log.d("itemPattern", "itam" + ptn_rand.nextInt(4));
//							int plus_score = (ptn_rand.nextInt(4) + 1) * 100;
//							mGameListener.addScore(plus_score);
//						}
//					});
//				}
//			}
			// アイテムが画面上からはみ出るまで表示させ続ける
//			if (item.isInsideScreen(getHeight())) {
//				String num = String.valueOf(i);
//				drawItem(item, num);
//			}
//		}
		getHolder().unlockCanvasAndPost(mCanvas);
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
		mExecFlg = true;
		mThread = new Thread(this);
		mThread.start();
		if (Utils.isRingerEnable(mContext)) {
			bgm.start();
		}
	}

	/**
	 * スレッドを一時停止 mThreadを一旦nullに
	 */
	public void endLoop() {
		mPauseFlg = true;
		mExecFlg = false;
		mThread = null;
		bgm.pause();
	}

	@Override
	public void shootBeam(BaseBeam... beams) {
		for (BaseBeam beam : beams) {
			mInvBeamList.add(beam);
		}
	}

	@Override
	public void Item(float shotX, float shotY) {
		BaseItem item = ItemFactory.create(mContext, mMediator, shotX, shotY);
		if (item == null) {
			return;
		}
		mItemList.add(item);
	}

	/**
	 * ゲーム状態を保存する
	 * 
	 * @return
	 */
	public Status saveStatus() {
		mStatus = new Status();
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
		public void endGame(int score, boolean clearflg);

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
