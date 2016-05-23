package com.simpleweather.app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.simpleweather.app.model.City;

public class Utility {
	/*
	 * 解析从和风天气的城市API读取的数据
	 */
	public synchronized static boolean handleCitiesReponse(
			SimpleWeatherDB simpleWeatherDB, String response) {

		try {
			// JSONArray jsonArray = new JSONArray(response);
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("city_info");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject subObject = jsonArray.getJSONObject(i);
				String city_name = subObject.getString("city");
				String prov = subObject.getString("prov");
				City city = new City();
				city.setCityName(city_name);
				city.setProvinceName(prov);
				simpleWeatherDB.save(city);
			}
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	/*
	 * 解析服务器返回的天气情况的JSON数据，并将解析出的数据存储到本地；
	 */
	public static void handleWeatherResponse(Context context, String response) {
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject resultJSONObject = jsonObject.getJSONObject("result");
			JSONObject dataJSONObject = resultJSONObject.getJSONObject("data");
			JSONObject realTimeJSONObject = dataJSONObject
					.getJSONObject("realtime");
			String time = realTimeJSONObject.getString("time");
			String cityName = realTimeJSONObject.getString("city_name");
			int week = realTimeJSONObject.getInt("week");
			JSONObject weatherJSONObject = realTimeJSONObject
					.getJSONObject("weather");
			String info = weatherJSONObject.getString("info");
			String temp = weatherJSONObject.getString("temperature");
			saveWeatherInfo(context, time, week, cityName, info, temp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
	 */
	public static void saveWeatherInfo(Context context, String time,
			int week, String cityName, String info, String temp) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("temp", temp);
		editor.putInt("week", week);
		editor.putString("info", info);
		editor.putString("updata_time", time);
		editor.commit();

	}
}
