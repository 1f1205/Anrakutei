package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
	@SuppressWarnings("unused")
	private static final String TAG = SplashActivity.class.getSimpleName();
	private static final int WAIT_TIME = 2000;
	private static final String STAGE_FORMAT = "STAGE %03d";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_stage_splash);
		TextView stageName = (TextView) findViewById(R.id.stage_name);
		int stageNum = 0;

		// ステージ名表示
		Intent intent = getIntent();

		if (intent.hasExtra(MainActivity.EXTRA_STAGE)) {
			stageNum = intent.getIntExtra(MainActivity.EXTRA_STAGE, 0);
		}
		
		stageName.setText(String.format(STAGE_FORMAT, stageNum));
		// Font
		Typeface face = Utils.getFonts(getApplicationContext());
		stageName.setTypeface(face);

		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), WAIT_TIME);
	}

	class splashHandler implements Runnable {

		@Override
		public void run() {
			// Splash完了後に実行するActivityを指定する。
			Intent intent = getIntent();

			intent.setClass(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			SplashActivity.this.finish();
		}

	}
}