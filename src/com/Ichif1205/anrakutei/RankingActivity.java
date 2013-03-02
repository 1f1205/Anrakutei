package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);

		ScoreDBOpenHelper helper = new ScoreDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "SELECT "+ ScoreDBOpenHelper.COLUMN_KEY+", "+ScoreDBOpenHelper.COLUMN_SCORE+", "+ScoreDBOpenHelper.COLUMN_DATE+" FROM score_table ORDER BY score DESC limit 10";
        Cursor cursor = db.rawQuery(sql, null);
		//Cursor c = db.query("score_table", new String[] { "score", "date" },
		//		null, null, null, null, null);
		// int count = c.getCount(); //record数
		boolean isEof = cursor.moveToFirst();
		int i = 1;
		while (isEof) {
			TextView tv = new TextView(this);
			tv.setText(String.format("%d : %d : %d", i, cursor.getInt(1), cursor.getInt(2)));
			isEof = cursor.moveToNext();
			layout.addView(tv);
			i++;
		}
		cursor.close();

		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ranking, menu);
		return true;
	}

}
