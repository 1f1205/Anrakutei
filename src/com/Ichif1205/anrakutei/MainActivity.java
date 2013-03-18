package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	//private SurfaceView mSurfaceView;
	private FieldSurfaceView mFieldSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mFieldSurfaceView = (FieldSurfaceView)findViewById(R.id.FieldSurfaceView_id);
		//mFieldSurfaceView = new FieldSurfaceView(this, mSurfaceView);
//		mFieldSurfaceView = new FieldSurfaceView(this);
//		setContentView(mFieldSurfaceView);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		mFieldSurfaceView.endLoop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		mFieldSurfaceView.restartLoop();
	}
	
	@Override
	public void onBackPressed() {
		mFieldSurfaceView.endLoop();
		showDialog();
		super.onBackPressed();
	}
	
	private void showDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle("タイトル");
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage("メッセージ");
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("肯定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    }
                });
        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNegativeButton("否定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	finish();
                    }
                });
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        // アラートダイアログを表示します
        alertDialog.show();
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

}
