package com.zjp.zonlytools.config;

import java.util.Map;

import android.content.Context;
import zp.android.baseapp.setting.BaseSystemConfig;
import zp.baseandroid.debug.DebugUtils;

public class SystemConfig extends BaseSystemConfig{
	static{
//		SoftName="ZOnlyTools";
//		ServerUrl="http://192.168.2.142/ZOnlyWeb/";
		SoftName="SFLSMobile";
		ServerUrl="http://www.sfls.com.cn:9994/service/api/";
		
		// 开启日志调试
		DebugUtils.isLogOpen = true;
		DebugUtils.debugIp = "192.168.2.142";
	}
	public static Map<String, String> createUploadSmsParamMap(Context context,String smsJson) {
		Map<String, String> datas = createBaseParamMap(context);
		datas.put("datas", smsJson);
		return datas;
	}
}
