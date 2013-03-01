package com.Ichif1205.anrakutei;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	//private SurfaceView mSurfaceView;
	private FieldSurfaceView mFieldSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		mFieldSurfaceView = (FieldSurfaceView)findViewById(R.id.FieldSurfaceView_id);
		//mFieldSurfaceView = new FieldSurfaceView(this, mSurfaceView);
//		mFieldSurfaceView = new FieldSurfaceView(this);
//		setContentView(mFieldSurfaceView);
		
	}

	
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

}
