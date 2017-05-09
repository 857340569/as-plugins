/**
 * 
 */
package com.zjp.zonlytools.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author zjp
 * @date 2017年5月9日
 */
public class SmsService extends Service{
	public static final String ACTION_SMS="com.zjp.zonlytools.sms.ACTION_SMS"; 
	public static final String ACTION_SMS_RECEIVED="com.zjp.zonlytools.sms.ACTION_SMS_RECEIVED"; 
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("SmsService.onStartCommand()"+intent.getAction());
		return super.onStartCommand(intent, flags, startId);
	}
	
}
