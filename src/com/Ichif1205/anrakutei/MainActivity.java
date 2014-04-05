package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;

import com.Ichif1205.anrakutei.FieldSurfaceView.GameEventLiestener;
import com.Ichif1205.anrakutei.score.Score;

public class MainActivity extends Activity implements GameEventLiestener {
	@SuppressWarnings("unused")
	private static String TAG = MainActivity.class.getSimpleName();

	private FieldSurfaceView mFieldSurfaceView;
	private TextView mScoreView = null;
	private boolean mPauseFlg = false;
	private boolean mGameEndFlg = false;
	private boolean mNextStageFlg = false;
	private int mStageId = 1;
	private Score mScore;
	private Status mStatus;
	private SparseArray<StageInfo> mStageInfos = null;

	private final String STAGE_FORMAT = "STAGE%03d";

	/**
	 * Extra
	 */
	public static final String EXTRA_SCORE = "EXTRA_SCORE";
	public static final String EXTRA_STAGE = "EXTRA_STAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mScore = Score.getInstance();

		// ステージIDやスコアのViewを設定
		setView();
		mFieldSurfaceView = (FieldSurfaceView) findViewById(R.id.FieldSurfaceView_id);
		mFieldSurfaceView.setEventListener(this);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				mStageInfos = StageInfos.getInstance();

				mFieldSurfaceView.setStageInfo(mStageInfos.get(mStageId));
			}
		});
		thread.start();
	}

	/**
	 * FieldSurfaceView以外のViewの設定
	 */
	private void setView() {
		Intent intent = getIntent();

		// ステージIDを設定
		if (intent.hasExtra(EXTRA_STAGE)) {
			mStageId = intent.getIntExtra(EXTRA_STAGE, 1);
		}

		Typeface face = Utils.getFonts(getApplicationContext());
		TextView stageView = (TextView) findViewById(R.id.stageView_id);
		mScoreView = (TextView) findViewById(R.id.scoreView_id);
		mScoreView.setTypeface(face);
		mScoreView.setText(mScore.toString());
		stageView.setTypeface(face);
		stageView.setText(String.format(STAGE_FORMAT, mStageId));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void onBackPressed() {
		// mPauseFlg が trueの時はダイアログが表示されている
		if (!mPauseFlg) {
			// ダイアログ表示
			mPauseFlg = true;
			mFieldSurfaceView.endLoop();
			mStatus = mFieldSurfaceView.saveStatus();
			showDialog();
		} else {
			// ダイアログで終了を選択された時
			mPauseFlg = false;
			mFieldSurfaceView.surfaceDestroyed(null);
			super.onBackPressed();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		// ステージ遷移時は即return
		if (mNextStageFlg) {
			return;
		}

		// ゲーム終了時はActivityを終了する
		if (mGameEndFlg) {
			finish();
			return;
		}
		// mPauseFlg が trueの時はダイアログが表示されている
		if (!mPauseFlg) {
			// ダイアログ表示
			mPauseFlg = true;
			mFieldSurfaceView.endLoop();
			mStatus = mFieldSurfaceView.saveStatus();
			showDialog();
		} else {
			// ダイアログで終了を選択された時
			mPauseFlg = false;
			mFieldSurfaceView.surfaceDestroyed(null);
			super.onBackPressed();
		}
	}

	/**
	 * ダイアログ表示
	 */
	private void showDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// アラートダイアログのタイトルを設定します
		alertDialogBuilder.setTitle("終了");
		// アラートダイアログのメッセージを設定します
		alertDialogBuilder.setMessage("ゲームを終了しますか？");
		// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
		alertDialogBuilder.setPositiveButton("はい",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onBackPressed();
					}
				});
		// アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
		alertDialogBuilder.setNegativeButton("いいえ",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mFieldSurfaceView.setInstance(mStatus);
						mPauseFlg = false;
						mFieldSurfaceView.restartLoop();
					}
				});
		// アラートダイアログのキャンセルが可能かどうかを設定します
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		// アラートダイアログを表示します
		alertDialog.show();
	}

	@Override
	public void endGame(boolean clearflg) {
		// ゲーム終了
		mGameEndFlg = true;
		
		final Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("clearflg", clearflg);
		startActivity(intent);
	}

	@Override
	public void addScore(int score) {
		mScore.addScore(score);
		mScoreView.setText(mScore.toString());
	}

	@Override
	public void nextStage() {
		mNextStageFlg = true;
		// ステージを繰り上げる
		mStageId++;

		// 最終ステージまでクリアしたら結果画面に遷移
		if (mStageInfos.indexOfKey(mStageId) < 0) {
			endGame(true);
			return;
		}

		// ステージ遷移用のINTENT
		Intent stageIntent = new Intent(getApplicationContext(),
				SplashActivity.class);

		stageIntent.putExtra(EXTRA_STAGE, mStageId);

		startActivity(stageIntent);
	}
}