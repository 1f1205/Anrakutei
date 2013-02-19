package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectPlayer extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_player);
		
		setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_player, menu);
		return true;
	}
	
	/**
	 *  set Listener
	 */
	private void setListener() {
		Button player1 = (Button) findViewById(R.id.player1);
		Button player2 = (Button) findViewById(R.id.player2);
		Button player3 = (Button) findViewById(R.id.player3);
		
		player1.setOnClickListener(this);
		player2.setOnClickListener(this);
		player3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int player;
		Intent intent = new Intent(SelectPlayer.this, MainActivity.class);
		
		if (v.getId() == R.id.player1) {
			player = 0;
		} else if (v.getId() == R.id.player2) {
			player = 1;
		} else {
			player = 2;
		}
		intent.putExtra("extra_select_player", player);
		startActivity(intent);
		
	}

}
