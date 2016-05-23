package com.simpleweather.app.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleWeatherOpenHelper extends SQLiteOpenHelper {
	/*
	 * Ω®±Ì”Ôæ‰
	 */
	public static final String CREATE_CITY="create table City("+
			"id integer primary key autoincrement,"
			+"province_name text,"
			+"city_name text)";
//	public static final String CREATE_PROVINCE="create table Province("+
//			"id integer primary key autoincrement,"
//			+"province_name)";

	public SimpleWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
