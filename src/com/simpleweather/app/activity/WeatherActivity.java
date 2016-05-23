package com.simpleweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simpleweather.R;
import com.simpleweather.app.service.AutoUpdateService;
import com.simpleweather.app.util.HttpCallBackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.Utility;

public class WeatherActivity extends Activity implements
		OnClickListener {

	private Button refreshWeather;
	private Button selectCity;
	private TextView week;
	private TextView updataTime;
	private TextView cityName;
	private TextView temp;
	private TextView weatherInfo;
	private LinearLayout weatherLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		selectCity = (Button) findViewById(R.id.select_city);
		refreshWeather = (Button) findViewById(R.id.refresh);
		selectCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		week = (TextView) findViewById(R.id.week);
		updataTime = (TextView) findViewById(R.id.updata_time);
		cityName = (TextView) findViewById(R.id.city_name);
		temp = (TextView) findViewById(R.id.temperature);
		weatherInfo = (TextView) findViewById(R.id.weather_info);
		weatherLayout = (LinearLayout) findViewById(R.id.weather_layout);
		String cityNameFromChoose = getIntent().getStringExtra("cityName");
		if (!TextUtils.isEmpty(cityNameFromChoose)) {
			updataTime.setText("同步中...");
			weatherLayout.setVisibility(View.INVISIBLE);
			week.setVisibility(View.INVISIBLE);
			queryWeather(cityNameFromChoose);
		} else {
			showWeather();
		}
	}

	private void showWeather() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		cityName.setText(pref.getString("city_name", ""));
		week.setText("星期" + (pref.getInt("week", 0)));
		updataTime.setText("今天" + pref.getString("updata_time", "") + "发布");
		temp.setText(pref.getString("temp", "") + "°");
		weatherInfo.setText(pref.getString("info", ""));
		weatherLayout.setVisibility(View.VISIBLE);
		week.setVisibility(View.VISIBLE);
		Intent intent= new Intent(this,AutoUpdateService.class);
		startService(intent);

	}

	private void queryWeather(String cityNameFromChoose) {
		String utfName = null;
		try {
			utfName = URLEncoder.encode(cityNameFromChoose, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String address = "http://op.juhe.cn/onebox/weather/query?cityname="
				+ utfName + "&key=b4514646f8ba60b73c7dbc8804110df0";
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						showWeather();
					}
				});
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						updataTime.setText("同步失败");
					}
				});
			}
		});
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.select_city:
			Intent intent = new Intent(this, ChooseActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh:
			updataTime.setText("同步中......");
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(this);
			String cityNameString = pref.getString("city_name", "");
			if (!TextUtils.isEmpty(cityNameString)) {
				queryWeather(cityNameString);
			}
			break;
		default:
			break;
		}
	}

}
