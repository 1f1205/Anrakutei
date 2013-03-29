package com.Ichif1205.anrakutei;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingActivity extends Activity {
	private Typeface mFace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);

		mFace = Utils.getFonts(getApplicationContext());

		ScoreDBOpenHelper helper = new ScoreDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "SELECT " + ScoreDBOpenHelper.COLUMN_KEY + ", "
				+ ScoreDBOpenHelper.COLUMN_SCORE + ", "
				+ ScoreDBOpenHelper.COLUMN_DATE
				+ " FROM score_table ORDER BY score DESC limit 10";
		Cursor cursor = db.rawQuery(sql, null);
		// Cursor c = db.query("score_table", new String[] { "score", "date" },
		// null, null, null, null, null);
		// int count = c.getCount(); //record数
		boolean isEof = cursor.moveToFirst();
		int i = 1;
		while (isEof) {
			TextView tv = new TextView(this);
			tv.setText(String.format("%d : %d : %d", i, cursor.getInt(1),
					cursor.getInt(2)));
			tv.setTypeface(mFace);
			isEof = cursor.moveToNext();
			layout.addView(tv);
			i++;
		}
		cursor.close();

		db.close();
	}

	// 途中
	class RankListViewBinder implements SimpleCursorAdapter.ViewBinder {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			// 順位をセット
			if (columnIndex == cursor
					.getColumnIndex(ScoreDBOpenHelper.COLUMN_KEY)) {
				int rank = cursor.getPosition() + 1;
				((TextView) view).setText(String.valueOf(rank));
				((TextView) view).setTypeface(mFace);
				return true;
			}

			// 時間をセット
			if (columnIndex == cursor
					.getColumnIndex(ScoreDBOpenHelper.COLUMN_SCORE)) {
				double score = cursor.getDouble(columnIndex);
				((TextView) view).setText(String.valueOf(score));
				((TextView) view).setTypeface(mFace);
				return true;
			}

			// 日付をセット
			if (columnIndex == cursor
					.getColumnIndex(ScoreDBOpenHelper.COLUMN_DATE)) {
				long unixtime = cursor.getLong(columnIndex);
				Date date = new Date(unixtime);
				SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
				((TextView) view).setText(format.format(date));
				((TextView) view).setTypeface(mFace);
				return true;
			}

			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ranking, menu);
		return true;
	}

}
