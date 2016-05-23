package com.simpleweather.app.util;

public interface HttpCallBackListener {
	public void onFinish(String string);
	public void onError(Exception e);

}
