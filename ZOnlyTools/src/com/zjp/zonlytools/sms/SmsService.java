/**
 * 
 */
package com.zjp.zonlytools.sms;

import com.zjp.zonlytools.MainActivity;
import com.zjp.zonlytools.sms.utils.ServiceUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import zp.baseandroid.common.utils.StringUtils;

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
		String action=intent.getAction();
		if(!StringUtils.isEmpty(action))
		{
			
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		ServiceUtils.startService(this, ACTION_SMS);
	}
}
