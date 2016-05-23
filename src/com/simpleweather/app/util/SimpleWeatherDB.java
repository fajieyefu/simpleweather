package com.simpleweather.app.util;

import java.util.ArrayList;
import java.util.List;

import com.simpleweather.app.model.City;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SimpleWeatherDB {
	/*
	 * 数据库名
	 */
	public static final String DB_NAME="simple_weather";
	/*
	 * 数据库版本
	 */
	public static final int VERSION=1;
	public static SimpleWeatherDB simpleWeatherDB;
	public static SQLiteDatabase db;
	private  SimpleWeatherDB(Context context) {
		SimpleWeatherOpenHelper dbHelp= new SimpleWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelp.getWritableDatabase();
	}
	public synchronized static SimpleWeatherDB getInstance(Context context){
		if(simpleWeatherDB==null)
		simpleWeatherDB = new SimpleWeatherDB(context);
		return simpleWeatherDB;
	}
	/*
	 * 从和风天气的城市API中读取到的城市及对应省的数据存到数据库中
	 */
//	public void save(Province province){
//		if(province!=null){
//			ContentValues values = new ContentValues();
//			values.put("province_name", province.getProvinceName());
//			db.insert("Province", null, values);
//		}
//	}
//	public List<Province> loadProvinces(){
//		List<Province> list = new ArrayList<Province>();
//		Cursor cursor = db.query("Province", null, null, null, null, null, null);
//		if(cursor.moveToFirst()){
//			do{
//				Province province= new Province();
//				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
//				list.add(province);
//			}while(cursor.moveToNext());
//		}
//		if(cursor!=null){
//			cursor.close();
//		}
//		return list;
//	}
	
	public  void save(City city) {
		if(city!=null){
			ContentValues values = new ContentValues();
			values.put("province_name", city.getProvinceName());
			values.put("city_name",city.getCityName());
			db.insert("City", null, values);
		}
		
		
	}
	public List<City> loadAllCities(){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	public List<City> loadCities(String provincename){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_name=?", new String[]{provincename}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
}
	