/**
 * 
 */
package com.zjp.zonlytools.sms;

import java.util.List;

import com.zjp.zonlytools.entity.SmsInfo;
import com.zjp.zonlytools.sms.utils.ServiceUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import zp.android.baseapp.net.HttpSenderImpl;
import zp.android.baseapp.net.XRequestCallBack;
import zp.baseandroid.common.utils.StringUtils;

/**
 * @author zjp
 * @date 2017年5月9日
 */
public class SmsService extends Service{
	public static final String ACTION_READ_ALL_SMS="com.zjp.zonlytools.sms.ACTION_READ_ALL_SMS"; 
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
			switch (action) {
			case ACTION_SMS_RECEIVED:
				List<SmsInfo> smss=(List<SmsInfo>) intent.getSerializableExtra("smss");
//				new HttpSenderImpl(this).sendRequest(serviceParameters, appendParams, new XRequestCallBack() {
//					
//				});
				System.out.println("SmsService.onStartCommand():"+smss);
				break;

			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
