package com.simpleweather.app.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.simpleweather.app.model.City;
import com.simpleweather.app.model.Tips;
import com.simpleweather.app.model.Weather;

public class Utility {
	/*
	 * �����Ӻͷ������ĳ���API��ȡ������
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
	 * �������������ص����������JSON���ݣ����������������ݴ洢�����أ�
	 */
	public static void handleWeatherResponse(Context context, String response) {
			List<Weather> list = new ArrayList<Weather>();//����һ������
			List<Tips> list2= new ArrayList<Tips>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject resultJSONObject = jsonObject.getJSONObject("result");
			JSONObject dataJSONObject = resultJSONObject.getJSONObject("data");
			JSONObject pm25JSONObject = dataJSONObject.getJSONObject("pm25");
			JSONObject pm25JsonObject2=pm25JSONObject.getJSONObject("pm25");
			String pm25=pm25JsonObject2.getString("pm25");
			String quality = pm25JsonObject2.getString("quality");
			/*
			 * 
			 */
			JSONArray weatherJSONArray= dataJSONObject.getJSONArray("weather");
			for(int i=1;i<weatherJSONArray.length()-1;i++){
				JSONObject jsonObject1= weatherJSONArray.getJSONObject(i);
				String date=jsonObject1.getString("date");//���ĳ�������
				JSONObject jsonObject2=jsonObject1.getJSONObject("info");
				JSONArray jsonArray_1=jsonObject2.getJSONArray("day");
				String day_info=jsonArray_1.get(1).toString();//���ĳ�������
				
				String day_temp=jsonArray_1.get(2).toString();//���ĳ����¶�
				String day_wind=jsonArray_1.get(4).toString();//���ĳ��ķ���
				list.add(new Weather(day_temp,day_wind,day_info,date));
			}
			/*
			 * ʹ��list���ϻ�ȡ�����������
			 */
			JSONObject lifeJSONObject= dataJSONObject.getJSONObject("life");
			JSONObject infoJSONObject=lifeJSONObject.getJSONObject("info");
			JSONArray chuanyi=infoJSONObject.getJSONArray("chuanyi");
			list2.add(new Tips(chuanyi.get(0).toString(),chuanyi.get(1).toString()));
			JSONArray ganmao=infoJSONObject.getJSONArray("ganmao");
			list2.add(new Tips(ganmao.get(0).toString(),ganmao.get(1).toString()));
			JSONArray ziwaixian=infoJSONObject.getJSONArray("ziwaixian");
			list2.add(new Tips(ziwaixian.get(0).toString(),ziwaixian.get(1).toString()));
			JSONArray yundong=infoJSONObject.getJSONArray("yundong");
			list2.add(new Tips(yundong.get(0).toString(),yundong.get(1).toString()));
			
			
			
			JSONObject realTimeJSONObject = dataJSONObject
					.getJSONObject("realtime");
			String time = realTimeJSONObject.getString("time");//����ʱ��
			String cityName = realTimeJSONObject.getString("city_name");//����
//			int week = realTimeJSONObject.getInt("week");
			JSONObject weatherJSONObject = realTimeJSONObject
					.getJSONObject("weather");
			String imgString=weatherJSONObject.getString("img");
			String info = weatherJSONObject.getString("info");//��������
			String temp = weatherJSONObject.getString("temperature");//�����¶�
			saveWeatherInfo(context, time, cityName, info, temp, pm25,quality,imgString,list,list2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ���
	 */
	public static void saveWeatherInfo(Context context, String time,
			 String cityName, String info, String temp,String pm25,String quality,String imgString,List<Weather> list,List<Tips> list2) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("temp", temp);
		editor.putString("info", info);
		editor.putString("updata_time", time);
		editor.putString("img", imgString);
		for(int i=0;i<list.size();i++){
			int t=i+1;
			editor.putString("date"+t, list.get(i).getDate());
			editor.putString("info"+t, list.get(i).getInfo());
			editor.putString("temp"+t, list.get(i).getTempture());
			editor.putString("wind"+t, list.get(i).getWind());
		}
		editor.putString("chuanyi_title", list2.get(0).getTitle());
		editor.putString("chuanyi_content", list2.get(0).getContent());
		editor.putString("ganmao_title", list2.get(1).getTitle());
		editor.putString("ganmao_content", list2.get(1).getContent());
		editor.putString("ziwaixian_title", list2.get(2).getTitle());
		editor.putString("ziwaixian_content", list2.get(2).getContent());
		editor.putString("yundong_title", list2.get(3).getTitle());
		editor.putString("yundong_content", list2.get(3).getContent());
		editor.putString("pm25", pm25);
		editor.putString("quality", quality);
		
		editor.commit();

	}
}
