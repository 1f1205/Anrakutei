package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static String TAG = MainActivity.class.getSimpleName();
	private FieldSurfaceView mFieldSurfaceView;
	private boolean mPauseFlg = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView scoreView = (TextView)findViewById(R.id.scoreView_id);
		scoreView.setText("SCORE");
		Log.d(TAG, "Start FindView");
		mFieldSurfaceView = (FieldSurfaceView)findViewById(R.id.FieldSurfaceView_id);
		mFieldSurfaceView.saveInstance();
		Log.d(TAG, "End FindView");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	public void onBackPressed() {
		// mPauseFlg が trueの時はダイアログが表示されている
		if (!mPauseFlg) {
			// ダイアログ表示
			mPauseFlg = true;
			mFieldSurfaceView.endLoop();
			showDialog();
		} else {
			// ダイアログで終了を選択された時
			mPauseFlg = false;
			mFieldSurfaceView.surfaceDestroyed(null);
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onUserLeaveHint() {
		// mPauseFlg が trueの時はダイアログが表示されている
		if (!mPauseFlg) {
			// ダイアログ表示
			mPauseFlg = true;
			mFieldSurfaceView.endLoop();
			showDialog();
		} else {
			// ダイアログで終了を選択された時
			mPauseFlg = false;
			mFieldSurfaceView.surfaceDestroyed(null);
			super.onUserLeaveHint();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (mFieldSurfaceView != null) {
		}
	}
	
	/**
	 * ダイアログ表示
	 */
	private void showDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle("終了");
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage("ゲームを終了しますか？");
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	onBackPressed();
                    }
                });
        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNegativeButton("いいえ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	mPauseFlg = false;
                    	mFieldSurfaceView.restartLoop();
                    }
                });
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        // アラートダイアログを表示します
        alertDialog.show();
	}
}
