package com.simpleweather.app.receiver;

import com.simpleweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AutoUpdataReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Intent intent = new Intent(arg0,AutoUpdateService.class);
		arg0.startService(intent);
		
		
		
	}

}
