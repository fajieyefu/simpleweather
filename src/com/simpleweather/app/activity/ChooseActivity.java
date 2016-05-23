package com.simpleweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleweather.R;
import com.simpleweather.app.model.City;
import com.simpleweather.app.util.HttpCallBackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.SimpleWeatherDB;
import com.simpleweather.app.util.Utility;

public class ChooseActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private SimpleWeatherDB simpleWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	private ProgressDialog progressDialog;
	private List<City> cityList;
	private List<City> allCityList;
	private int currentLevel;
	private String selectedProvince;
	private boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("city_selected", false)&&!isFromWeatherActivity){
			Intent intent= new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		simpleWeatherDB = SimpleWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = dataList.get(arg2);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					String cityName= dataList.get(arg2);
					Intent intent = new Intent(ChooseActivity.this,WeatherActivity.class);
					intent.putExtra("cityName", cityName);
					startActivity(intent);
					finish();
				}
			}

		});
		queryProvince();

	}

	private void queryProvince() {
		cityList = simpleWeatherDB.loadAllCities();
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				String temp = city.getProvinceName();
				if (!dataList.contains(temp))
					dataList.add(temp);
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer("province");
		}

	}

	private void queryCities() {
		allCityList = simpleWeatherDB.loadCities(selectedProvince);
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : allCityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince);
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer("city");

		}

	}

	private void queryFromServer(final String type) {
		String address = "https://api.heweather.com/x3/citylist?search=allchina&key=118ad8f6ec7a4fa9a9c1049e96e8d341";
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

			@Override
			public void onFinish(String reponse) {
				boolean result = false;
				result = Utility.handleCitiesReponse(simpleWeatherDB, reponse);
				if (result) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvince();
							}
							if ("city".equals(type)) {
								queryCities();
							}
						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}

				});
			}
		});

	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if (progressDialog!=null) {
			progressDialog.dismiss();
		}
		
	}

	@Override
	public void onBackPressed() {
		if(currentLevel==LEVEL_CITY)
			queryProvince();
		if(isFromWeatherActivity){
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
		}else
			finish();
	}
	
}
