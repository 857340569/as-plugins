package com.zjp.zonlytools.sms.receiver;

import com.zjp.zonlytools.sms.SmsService;
import com.zjp.zonlytools.sms.utils.ServiceUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsBroadcaseReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		 StringBuilder sb = new StringBuilder();  
         Bundle bundle = intent.getExtras();  
         if(bundle!=null){  
             Object[] pdus = (Object[])bundle.get("pdus");  
             SmsMessage[] msg = new SmsMessage[pdus.length];  
             for(int i = 0 ;i<pdus.length;i++){  
                 msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);  
             }  
               
             for(SmsMessage curMsg:msg){  
                 sb.append("You got the message From:【");  
                 sb.append(curMsg.getDisplayOriginatingAddress());  
                 sb.append("】\nContent：");  
                 sb.append(curMsg.getDisplayMessageBody());  
             } 
         }
         Intent mIntent = ServiceUtils.createServiceIntent(context,SmsService.ACTION_SMS_RECEIVED);
         context.startService(mIntent);
	}
	
	
}