package com.zjp.zonlytools.sms.receiver;

import com.zjp.zonlytools.sms.SmsService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		startService(context,SmsService.ACTION_SMS);
	}

	private void startService(Context context, String action) {
		Intent mIntent = new Intent();
		mIntent.setAction(action);// 你定义的service的action
		mIntent.setPackage(context.getPackageName());
		context.startService(mIntent);

	}
}