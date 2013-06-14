package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Intent intent = getIntent();
		int score = intent.getIntExtra("score", 0);

		TextView resultScore = (TextView) findViewById(R.id.result_score);
		TextView resultTitle = (TextView) findViewById(R.id.result_title);
		Button topButton = (Button) findViewById(R.id.top_button);
		// Set Font
		Typeface face = Utils.getFonts(getApplicationContext());
		resultScore.setTypeface(face);
		resultTitle.setTypeface(face);
		topButton.setTypeface(face);

		topButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});

		resultScore.setText(Integer.toString(score));

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_result, menu);
		return true;
	}

}
