package com.zjp.zonlytools.sms.receiver;

import com.zjp.zonlytools.MainActivity;
import com.zjp.zonlytools.sms.SmsService;
import com.zjp.zonlytools.sms.utils.ServiceUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceUtils.startService(context, SmsService.ACTION_SMS);
		Intent intent2=new Intent(context, MainActivity.class);
		//必须要有
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent2);
	}
}