package com.simpleweather.app.model;

public class Weather {
	private String tempture;
	private String wind;
	private String info;
	private String date;
	
	public Weather(String tempture, String wind, String info, String date) {
		super();
		this.tempture = tempture;
		this.wind = wind;
		this.info = info;
		this.date = date;
	}
	public String getTempture() {
		return tempture;
	}
	public void setTempture(String tempture) {
		this.tempture = tempture;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
