package com.simpleweather.app.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.simpleweather.app.receiver.AutoUpdataReceiver;
import com.simpleweather.app.util.HttpCallBackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.Utility;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour =2*60*60*1000;
		long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
		Intent i= new Intent(this,AutoUpdataReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeather() {
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
		String cityName = pref.getString("city_name", "");
		String utfName=null;
		try {
			utfName = URLEncoder.encode(cityName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String address = "http://op.juhe.cn/onebox/weather/query?cityname="
				+ utfName + "&key=b4514646f8ba60b73c7dbc8804110df0";
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String string) {
				Utility.handleWeatherResponse(AutoUpdateService.this, string);
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}

}
