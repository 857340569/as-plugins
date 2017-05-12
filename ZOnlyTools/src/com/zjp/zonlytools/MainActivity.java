package com.zjp.zonlytools;

import com.zjp.zonlytools.sms.SmsService;
import com.zjp.zonlytools.sms.utils.SmsUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		content=(TextView) findViewById(R.id.content);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//		registerReceiver(new BroadcastReceiver() {
//			
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				 StringBuilder sb = new StringBuilder();  
//		            Bundle bundle = arg1.getExtras();  
//		            if(bundle!=null){  
//		                Object[] pdus = (Object[])bundle.get("pdus");  
//		                SmsMessage[] msg = new SmsMessage[pdus.length];  
//		                for(int i = 0 ;i<pdus.length;i++){  
//		                    msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);  
//		                }  
//		                  
//		                for(SmsMessage curMsg:msg){  
//		                    sb.append("You got the message From:【");  
//		                    sb.append(curMsg.getDisplayOriginatingAddress());  
//		                    sb.append("】\nContent：");  
//		                    sb.append(curMsg.getDisplayMessageBody());  
//		                }  
//		                content.setText(sb.toString()); 
//		                    
//		            }  
//			}
//		}, intentFilter);
		System.out.println(SmsUtils.getSmsInfos(this));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("MainActivity.onDestroy()");
	}
}
