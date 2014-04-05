package com.Ichif1205.anrakutei;

import jp.co.imobile.android.AdView;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Ichif1205.anrakutei.score.Score;
import com.Ichif1205.anrakutei.score.ScoreDBOpenHelper;

public class ResultActivity extends Activity {
	private Score mScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		mScore = Score.getInstance();
		
		setTitle("Result");

		Intent intent = getIntent();
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
			resultTitle = (TextView) findViewById(R.id.result_title);
		}
		resultTitle.setVisibility(View.VISIBLE);

		// Set Font
		Typeface face = Utils.getFonts(getApplicationContext());
		final TextView resultScore = (TextView) findViewById(R.id.result_score);
		resultScore.setTypeface(face);
		resultScore.setText(mScore.toString());

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

		saveScore(System.currentTimeMillis());
	}

	/**
	 * スコアをDBに保存
	 * 
	 * @param Date
	 */
	private void saveScore(long Date) {
		ScoreDBOpenHelper helper = new ScoreDBOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ScoreDBOpenHelper.COLUMN_SCORE, mScore.getScore());
		values.put(ScoreDBOpenHelper.COLUMN_DATE, Date);
		db.insert("score_table", null, values);

		db.close();
	}
}
