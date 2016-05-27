package com.simpleweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.simpleweather.R;
import com.simpleweather.app.service.AutoUpdateService;
import com.simpleweather.app.util.HttpCallBackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener,
		OnRefreshListener {

	private TextView chuanyiTitle;
	private TextView chuanyiContent;
	private TextView doctorTitle;
	private TextView doctorContent;
	private TextView ziwaixianTitle;
	private TextView ziwaixianContent;
	private TextView sportTitle;
	private TextView sportContent;
	private Button selectCity;
	private TextView pm25;
	private ImageView weather_picture;
	private TextView updataTime;
	private TextView cityName;
	private TextView temp;
	private TextView weatherInfo;
	private TextView day_1;
	private TextView day_2;
	private TextView day_3;
	private TextView day_4;
	private TextView day_5;
	private TextView info_1;
	private TextView info_2;
	private TextView info_3;
	private TextView info_4;
	private TextView info_5;
	private TextView temp_1;
	private TextView temp_2;
	private TextView temp_3;
	private TextView temp_4;
	private TextView temp_5;
	private TextView wind_1;
	private TextView wind_2;
	private TextView wind_3;
	private TextView wind_4;
	private TextView wind_5;

	private LinearLayout weatherLayout;
	private RelativeLayout day_night;

	private SwipeRefreshLayout swipeRefreshLayout;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weatherlayout);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(this);
		chuanyiTitle = (TextView) findViewById(R.id.clothes_text_title);
		chuanyiContent = (TextView) findViewById(R.id.clothes_text_content);
		doctorTitle = (TextView) findViewById(R.id.doctor_text_title);
		doctorContent = (TextView) findViewById(R.id.doctor_text_content);
		ziwaixianTitle = (TextView) findViewById(R.id.ziwaixian_text_title);
		ziwaixianContent = (TextView) findViewById(R.id.ziwaixian_text_content);
		sportTitle = (TextView) findViewById(R.id.sport_text_title);
		sportContent = (TextView) findViewById(R.id.sport_text_content);

		pm25 = (TextView) findViewById(R.id.pm25);
		weather_picture = (ImageView) findViewById(R.id.weather_picture);
		weatherLayout = (LinearLayout) findViewById(R.id.weatherlayout);
		day_night=(RelativeLayout) findViewById(R.id.day_night);
		selectCity = (Button) findViewById(R.id.xuanzechengshi);
		selectCity.setOnClickListener(this);
		day_1 = (TextView) findViewById(R.id.day_1);
		day_2 = (TextView) findViewById(R.id.day_2);
		day_3 = (TextView) findViewById(R.id.day_3);
		day_4 = (TextView) findViewById(R.id.day_4);
		day_5 = (TextView) findViewById(R.id.day_5);
		info_1 = (TextView) findViewById(R.id.day_1_info);
		info_2 = (TextView) findViewById(R.id.day_2_info);
		info_3 = (TextView) findViewById(R.id.day_3_info);
		info_4 = (TextView) findViewById(R.id.day_4_info);
		info_5 = (TextView) findViewById(R.id.day_5_info);
		temp_1 = (TextView) findViewById(R.id.day1_temp);
		temp_2 = (TextView) findViewById(R.id.day2_temp);
		temp_3 = (TextView) findViewById(R.id.day3_temp);
		temp_4 = (TextView) findViewById(R.id.day4_temp);
		temp_5 = (TextView) findViewById(R.id.day5_temp);
		wind_1 = (TextView) findViewById(R.id.wind_1);
		wind_2 = (TextView) findViewById(R.id.wind_2);
		wind_3 = (TextView) findViewById(R.id.wind_3);
		wind_4 = (TextView) findViewById(R.id.wind_4);
		wind_5 = (TextView) findViewById(R.id.wind_5);

		cityName = (TextView) findViewById(R.id.city_name);
		temp = (TextView) findViewById(R.id.temperature);
		weatherInfo = (TextView) findViewById(R.id.weather_info);

		String cityNameFromChoose = getIntent().getStringExtra("cityName");
		if (!TextUtils.isEmpty(cityNameFromChoose)) {

			weatherLayout.setVisibility(View.INVISIBLE);
			queryWeather(cityNameFromChoose);
		} else {
			showWeather();
		}
	}

	private void showWeather() {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String dateTime = pref.getString("updata_time", "");
		String[] timeString = dateTime.split(":");
		int time = Integer.parseInt(timeString[0]);
		if ((6 > time) || time > 19) {
			day_night.setBackgroundResource(R.drawable.topnight);

		}
		int imgInt = Integer.parseInt(pref.getString("img", "100"));
		switch (imgInt) {
		case 0:
			weather_picture.setImageResource(R.drawable.p00);
			break;
		case 1:
			weather_picture.setImageResource(R.drawable.p_01);
			break;
		case 2:
			weather_picture.setImageResource(R.drawable.p02);
			break;
		case 3:
			weather_picture.setImageResource(R.drawable.p03);
			break;
		case 4:
			weather_picture.setImageResource(R.drawable.p04);
			break;
		case 5:
			weather_picture.setImageResource(R.drawable.p05);
			break;
		case 6:
			weather_picture.setImageResource(R.drawable.p06);
			break;
		case 7:
			weather_picture.setImageResource(R.drawable.p07);
			break;
		case 8:
			weather_picture.setImageResource(R.drawable.p08);
			break;
		case 9:
			weather_picture.setImageResource(R.drawable.p09);
			break;
		case 10:
			weather_picture.setImageResource(R.drawable.p10);
			break;
		case 11:
			weather_picture.setImageResource(R.drawable.p11);
			break;
		case 12:
			weather_picture.setImageResource(R.drawable.p12);
			break;
		case 13:
			weather_picture.setImageResource(R.drawable.p13);
			break;
		case 14:
			weather_picture.setImageResource(R.drawable.p14);
			break;
		case 15:
			weather_picture.setImageResource(R.drawable.p15);
			break;
		case 16:
			weather_picture.setImageResource(R.drawable.p16);
			break;
		case 17:
			weather_picture.setImageResource(R.drawable.p17);
			break;
		case 18:
			weather_picture.setImageResource(R.drawable.p18);
			break;
		case 19:
			weather_picture.setImageResource(R.drawable.p19);
			break;
		case 20:
			weather_picture.setImageResource(R.drawable.p20);
			break;
		case 21:
			weather_picture.setImageResource(R.drawable.p21);
			break;
		case 22:
			weather_picture.setImageResource(R.drawable.p22);
			break;
		case 23:
			weather_picture.setImageResource(R.drawable.p23);
			break;
		case 24:
			weather_picture.setImageResource(R.drawable.p24);
			break;
		case 25:
			weather_picture.setImageResource(R.drawable.p25);
			break;
		case 26:
			weather_picture.setImageResource(R.drawable.p26);
			break;
		case 27:
			weather_picture.setImageResource(R.drawable.p27);
			break;
		case 28:
			weather_picture.setImageResource(R.drawable.p28);
			break;
		case 29:
			weather_picture.setImageResource(R.drawable.p29);
			break;
		case 30:
			weather_picture.setImageResource(R.drawable.p30);
			break;
		case 31:
			weather_picture.setImageResource(R.drawable.p31);
			break;
		case 53:
			weather_picture.setImageResource(R.drawable.p53);
			break;
		default:
			break;
		}
		ziwaixianTitle.setText("紫外线指数---"
				+ pref.getString("ziwaixian_title", ""));
		ziwaixianContent.setText(pref.getString("ziwaixian_content", ""));
		sportTitle.setText("运动指数---" + pref.getString("yundong_title", ""));
		sportContent.setText(pref.getString("yundong_content", ""));
		chuanyiTitle.setText("穿衣指数---" + pref.getString("chuanyi_title", ""));
		chuanyiContent.setText(pref.getString("chuanyi_content", ""));
		doctorTitle.setText("感冒指数---" + pref.getString("ganmao_title", ""));
		doctorContent.setText(pref.getString("ganmao_content", ""));
		pm25.setText("pm25: " + pref.getString("pm25", "") + "\n"+"空气质量: "
				+ pref.getString("quality", ""));
		String[] date1Array = pref.getString("date1", "").split("-");
		String date1 = date1Array[1] + "/" + date1Array[2];
		String[] date2Array = pref.getString("date2", "").split("-");
		String date2 = date2Array[1] + "/" + date2Array[2];
		String[] date3Array = pref.getString("date3", "").split("-");
		String date3 = date3Array[1] + "/" + date3Array[2];
		String[] date4Array = pref.getString("date4", "").split("-");
		String date4 = date4Array[1] + "/" + date4Array[2];
		String[] date5Array = pref.getString("date5", "").split("-");
		String date5 = date5Array[1] + "/" + date5Array[2];
		day_1.setText(date1);
		day_2.setText(date2);
		day_3.setText(date3);
		day_4.setText(date4);
		day_5.setText(date5);
		info_1.setText(pref.getString("info1", ""));
		info_2.setText(pref.getString("info2", ""));
		info_3.setText(pref.getString("info3", ""));
		info_4.setText(pref.getString("info4", ""));
		info_5.setText(pref.getString("info5", ""));
		temp_1.setText(pref.getString("temp1", "")+"°");
		temp_2.setText(pref.getString("temp2", "")+"°");
		temp_3.setText(pref.getString("temp3", "")+"°");
		temp_4.setText(pref.getString("temp4", "")+"°");
		temp_5.setText(pref.getString("temp5", "")+"°");
		wind_1.setText(pref.getString("wind1", ""));
		wind_2.setText(pref.getString("wind2", ""));
		wind_3.setText(pref.getString("wind3", ""));
		wind_4.setText(pref.getString("wind4", ""));
		wind_5.setText(pref.getString("wind5", ""));
		cityName.setText(pref.getString("city_name", ""));
		temp.setText(pref.getString("temp", "") + "°C");
		weatherInfo.setText(pref.getString("info", ""));
		weatherLayout.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);

	}

	private void queryWeather(String cityNameFromChoose) {
		String utfName = null;
		try {
			utfName = URLEncoder.encode(cityNameFromChoose, "utf-8");
		} catch (UnsupportedEncodingException e1) {
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
		case R.id.xuanzechengshi:
			Intent intent = new Intent(this, ChooseActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(WeatherActivity.this);
		String cityNameString = pref.getString("city_name", "");
		if (!TextUtils.isEmpty(cityNameString)) {
			queryWeather(cityNameString);

		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
			}
		},4000);


	}

}
