package com.Ichif1205.anrakutei;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class RankingActivity extends Activity {
	private Typeface mFace;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		setFonts();

		ScoreDBOpenHelper helper = new ScoreDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "SELECT " + ScoreDBOpenHelper.COLUMN_KEY + " as _id, "
				+ ScoreDBOpenHelper.COLUMN_SCORE + ", "
				+ ScoreDBOpenHelper.COLUMN_DATE
				+ " FROM score_table ORDER BY score DESC limit 10";
		mCursor = db.rawQuery(sql, null);
		
		showListView();
		
		db.close();
	}
	
	
	/**
	 * Fontを設定
	 */
	private void setFonts() {
		mFace = Utils.getFonts(getApplicationContext());
		
		// Header
		((TextView) findViewById(R.id.header_rank)).setTypeface(mFace);
		((TextView) findViewById(R.id.header_time)).setTypeface(mFace);
		((TextView) findViewById(R.id.header_date)).setTypeface(mFace);
	}

	/**
	 * リストビューの準備
	 */
	private void showListView() {
		ListAdapter adapter = new RankCursorAdapter(this, R.layout.rank_item, mCursor,
				new String[] { "_id", 
						ScoreDBOpenHelper.COLUMN_SCORE,
						ScoreDBOpenHelper.COLUMN_DATE }, new int[] {
						R.id.rank, R.id.average_text, R.id.date_text }, 0);
		
		ListView lv = (ListView) findViewById(R.id.rank_list);
		lv.setHeaderDividersEnabled(true);
		lv.setAdapter(adapter);

	}

	class RankCursorAdapter extends SimpleCursorAdapter {
		public RankCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flg) {
			super(context, layout, c, from, to, flg);
			setTitle("Ranking");
			setViewBinder(new RankListViewBinder());
		}

		// 途中
		class RankListViewBinder implements SimpleCursorAdapter.ViewBinder {
			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				// 順位をセット
				if (columnIndex == cursor
						.getColumnIndex("_id")) {
					int rank = cursor.getPosition() + 1;
					((TextView) view).setText(String.valueOf(rank));
					((TextView) view).setTypeface(mFace);
					return true;
				}

				// スコアをセット
				if (columnIndex == cursor
						.getColumnIndex(ScoreDBOpenHelper.COLUMN_SCORE)) {
					int score = cursor.getInt(columnIndex);
					((TextView) view).setText(String.valueOf(score));
					((TextView) view).setTypeface(mFace);
					return true;
				}

				// 日付をセット
				if (columnIndex == cursor
						.getColumnIndex(ScoreDBOpenHelper.COLUMN_DATE)) {
					long unixtime = cursor.getLong(columnIndex);
					Date date = new Date(unixtime);
					SimpleDateFormat format = new SimpleDateFormat(
							"yy/MM/dd HH:mm");
					((TextView) view).setText(format.format(date));
					((TextView) view).setTypeface(mFace);
					return true;
				}

				return false;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ranking, menu);
		return true;
	}

}
