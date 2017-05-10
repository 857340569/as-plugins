package com.zjp.zonlytools.sms.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @author zjp
 * @date 2017年5月10日
 */
public class ServiceUtils {

	public static void startService(Context context, String action) {
		Intent mIntent = createServiceIntent(context, action);
		context.startService(mIntent);
	}

	public static Intent createServiceIntent(Context context, String action) {
		Intent mIntent = new Intent();
		mIntent.setAction(action);
		// 加此行，否则某些版本无法正常启动服务
		mIntent.setPackage(context.getPackageName());
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return mIntent;
	}
	
}
