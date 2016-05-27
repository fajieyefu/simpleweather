package com.simpleweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.simpleweather.R;

public class LoadingActivity extends Activity {
	private final long SPLASH_LENGTH=2000;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loding_layout);
		handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				Intent i = new Intent(LoadingActivity.this,ChooseActivity.class);
				startActivity(i);
				finish();
			}}, SPLASH_LENGTH);
	}
	

}
