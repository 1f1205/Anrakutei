package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private static final String TAG = SplashActivity.class.getSimpleName();
	private static final int WAIT_TIME = 2000;
	private static final String STAGE_FORMAT = "STAGE %03d";
	private int stageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_stage_splash);
		TextView stageName = (TextView) findViewById(R.id.stage_name);

		// ステージ名表示
		Intent intent = getIntent();
		if (intent.hasExtra(MainActivity.EXTRA_SCORE)) {
			Log.d(TAG,
					"Splash_Score:"
							+ intent.getIntExtra(MainActivity.EXTRA_SCORE, 0));
		}

		if (intent.hasExtra(MainActivity.EXTRA_STAGE)) {
			stageNum = intent.getIntExtra(MainActivity.EXTRA_STAGE, 0);
			stageNum++;
		}
		stageName.setText(String.format(STAGE_FORMAT, stageNum));

		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), WAIT_TIME);
	}

	class splashHandler implements Runnable {

		@Override
		public void run() {
			// Splash完了後に実行するActivityを指定する。
			Intent intent = getIntent();

			if (intent.hasExtra(MainActivity.EXTRA_SCORE)) {
				Log.d(TAG,
						"Splash_Score:"
								+ intent.getIntExtra(MainActivity.EXTRA_SCORE,
										0));
			}

			intent.setClass(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			SplashActivity.this.finish();
		}

	}
}