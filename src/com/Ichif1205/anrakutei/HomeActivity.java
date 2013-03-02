package com.Ichif1205.anrakutei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Button button_play = (Button)findViewById(R.id.play);
        Button button_rank = (Button)findViewById(R.id.ranking);
        Button button_result = (Button)findViewById(R.id.result);
        
        button_play.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent intent = new Intent(HomeActivity.this, SelectPlayer.class);
        		intent.putExtra("Count", 20);
        		startActivity(intent);
        	}
        });  
        button_rank.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
        		intent.putExtra("Count", 20);
        		startActivity(intent);
        	}
        }); 
        button_result.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent intent = new Intent(HomeActivity.this, ResultActivity.class);
        		intent.putExtra("score", 30);
        		intent.putExtra("date", 20120301);
        		startActivity(intent);
        	}
        }); 
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

}
