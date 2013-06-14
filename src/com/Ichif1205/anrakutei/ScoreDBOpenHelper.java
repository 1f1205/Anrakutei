package com.Ichif1205.anrakutei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDBOpenHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "anrakutei.db";
	public static final String COLUMN_KEY = "_key";
	public static final String COLUMN_SCORE = "score";
	public static final String COLUMN_DATE = "date";

	final static private int DB_VERSION = 1;
	private static final String CREATE_TABLE = "CREATE TABLE score_table ("
			+ COLUMN_KEY + " INTEGER PRIMARY KEY,"
			+ COLUMN_SCORE + " INTEGER,"
			+ COLUMN_DATE + " INTEGER" + ")";

	public ScoreDBOpenHelper(Context context) {
		super(context, "anrakutei.db", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// table create
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// データベースの変更が生じた場合は、ここに処理を記述する。
	}

}
