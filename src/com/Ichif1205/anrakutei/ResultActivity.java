package com.Ichif1205.anrakutei;

import jp.co.imobile.android.AdView;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		setTitle("Result");

		Intent intent = getIntent();
		final int score = intent.getIntExtra("score", 0);
		final boolean clearflg = intent.getBooleanExtra("clearflg", false);

		TextView resultTitle = null;
		if (clearflg) {
			resultTitle = (TextView) findViewById(R.id.clear_title);
			// ad
			LinearLayout footerAd = (LinearLayout) findViewById(R.id.footer_ad);
			AdView adView = AdView.create(this, 59161, 125290);
			footerAd.addView(adView);
			adView.start();
		} else {
			Log.d("itemPattern", "clear" + clearflg);
			resultTitle = (TextView) findViewById(R.id.result_title);
		}
		resultTitle.setVisibility(View.VISIBLE);

		// Set Font
		Typeface face = Utils.getFonts(getApplicationContext());
		final TextView resultScore = (TextView) findViewById(R.id.result_score);
		resultScore.setTypeface(face);
		resultScore.setText(Integer.toString(score));

		resultTitle.setTypeface(face);

		final Button topButton = (Button) findViewById(R.id.top_button);
		topButton.setTypeface(face);
		topButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResultActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});

		saveScore(score, System.currentTimeMillis());
	}

	/**
	 * スコアをDBに保存
	 * 
	 * @param Score
	 * @param Date
	 */
	private void saveScore(int Score, long Date) {
		ScoreDBOpenHelper helper = new ScoreDBOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ScoreDBOpenHelper.COLUMN_SCORE, Score);
		values.put(ScoreDBOpenHelper.COLUMN_DATE, Date);
		db.insert("score_table", null, values);

		db.close();
	}
}
