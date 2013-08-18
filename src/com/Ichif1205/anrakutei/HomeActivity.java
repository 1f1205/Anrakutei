package com.Ichif1205.anrakutei;

import jp.maru.mrd.IconCell;
import jp.maru.mrd.IconLoader;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends Activity {
	private static final String TAG = HomeActivity.class.getSimpleName();
	private static final String ASTRSK_MEDIA_CODE = "Ichif1205.anrakutei";
	IconLoader<Integer> mIconLoader = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// アスタ広告読み込み
		if (mIconLoader == null) {
			mIconLoader = new IconLoader<Integer>(ASTRSK_MEDIA_CODE, this);
			((IconCell) findViewById(R.id.iconAd1)).addToIconLoader(mIconLoader);
			((IconCell) findViewById(R.id.iconAd2)).addToIconLoader(mIconLoader);
			((IconCell) findViewById(R.id.iconAd3)).addToIconLoader(mIconLoader);
			((IconCell) findViewById(R.id.iconAd4)).addToIconLoader(mIconLoader);
			mIconLoader.setRefreshInterval(60);
		}

		// ステージ情報を読み込む
		final ReadStageInfoTask task = new ReadStageInfoTask(HomeActivity.this);
		task.execute();

		// init
		ImageView image = (ImageView)findViewById(R.id.title_icon_id);
        image.setImageResource(R.drawable.title_icon);
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
	protected void onResume() {
		super.onResume();
		mIconLoader.startLoading();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mIconLoader.stopLoading();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
