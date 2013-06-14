package com.Ichif1205.anrakutei;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		final ReadStageInfoTask task = new ReadStageInfoTask(HomeActivity.this);
				task.execute();

		// init
		Button button_play = (Button) findViewById(R.id.play);
		Button button_rank = (Button) findViewById(R.id.ranking);
		Button button_result = (Button) findViewById(R.id.result);

		// Setting Font
		Typeface face = Utils.getFonts(getApplicationContext());
		button_play.setTypeface(face);
		button_rank.setTypeface(face);
		button_result.setTypeface(face);

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
		button_result.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(HomeActivity.this,
						ResultActivity.class);
				intent.putExtra("score", 30);
				intent.putExtra("date", 20120301);
				startActivity(intent);
			}
		});

	}

	private class ReadStageInfoTask extends AsyncTask<Void, Integer, Boolean>{
//		private Context mContext;
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
			final StageXmlParser xmlParser = new StageXmlParser(getApplicationContext());
			xmlParser.parseStageXml();
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
			mProgress.incrementProgressBy(values[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
//			super.onPostExecute(result);
			mProgress.dismiss();
			
		}
		
	}

}
