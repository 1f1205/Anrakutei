package com.Ichif1205.anrakutei;

import jp.beyond.bead.Bead;
import jp.beyond.bead.Bead.ContentsOrientation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	
	// Bead広告
	private Bead mBeadOptional = null;
	private Bead mBeadExit = null;
	private static final String BEAD_EXIT_SID = "47ec4bc31331a871e04e9fb57aa683cd3f75a53afd31b820";
	private static final String BEAD_HOME_SID = "47ec4bc31331a871a1a6ca67d264b1a4f7e3db816af9e295";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// 	BEAD広告読み込み
		mBeadOptional = Bead.createOptionalInstance(BEAD_HOME_SID, 3, ContentsOrientation.Portrait);
		mBeadExit = Bead.createExitInstance(BEAD_EXIT_SID, ContentsOrientation.Portrait);
		mBeadOptional.requestAd(this);
		mBeadExit.requestAd(this);

		// ステージ情報を読み込む
		final ReadStageInfoTask task = new ReadStageInfoTask(HomeActivity.this);
		task.execute();

		// init
		Button button_play = (Button) findViewById(R.id.play);
		Button button_rank = (Button) findViewById(R.id.ranking);

		// Setting Font
		Typeface face = Utils.getFonts(getApplicationContext());
		button_play.setTypeface(face);
		button_rank.setTypeface(face);

		// Setting Listener
		button_play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(HomeActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.putExtra("Count", 20);
				startActivity(intent);
			}
		});
		button_rank.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(HomeActivity.this,
						RankingActivity.class);
				intent.putExtra("Count", 20);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onUserLeaveHint() {
		mBeadOptional.showAd(this);
	}
	
	@Override
	protected void onDestroy() {
		mBeadOptional.endAd();
		mBeadExit.endAd();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		mBeadExit.showAd(this);
	}

	/**
	 * ステージ情報をバックグラウンドで読み込む
	 */
	private class ReadStageInfoTask extends AsyncTask<Void, Integer, Boolean> {
		private Activity mActivity;
		private ProgressDialog mProgress;

		public ReadStageInfoTask(Activity activity) {
			super();
			mActivity = activity;
		}

		@Override
		protected void onPreExecute() {
			// Progres barの初期設定
			mProgress = new ProgressDialog(mActivity);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setTitle("ステージ読込中");
			mProgress.setMessage("しばらくお待ち下さい");
			mProgress.setCancelable(false);
			mProgress.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// 処理する内容
			final StageXmlParser xmlParser = new StageXmlParser(
					getApplicationContext());
			xmlParser.parseStageXml();
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// プログレスの更新
			mProgress.incrementProgressBy(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// 実行終了時
			mProgress.dismiss();
		}
	}
}
