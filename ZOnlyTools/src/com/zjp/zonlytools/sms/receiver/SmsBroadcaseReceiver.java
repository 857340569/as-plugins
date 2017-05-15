package com.zjp.zonlytools.sms.receiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zjp.zonlytools.entity.SmsInfo;
import com.zjp.zonlytools.sms.SmsService;
import com.zjp.zonlytools.sms.utils.ServiceUtils;
import com.zjp.zonlytools.sms.utils.SmsUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import zp.baseandroid.common.utils.DateUtils;

public class SmsBroadcaseReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
         Bundle bundle = intent.getExtras(); 
         List<SmsInfo> smss=new ArrayList<SmsInfo>();
         if(bundle!=null){  
             Object[] pdus = (Object[])bundle.get("pdus");  
             SmsMessage[] msg = new SmsMessage[pdus.length];  
             for(int i = 0 ;i<pdus.length;i++){  
                 msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);  
             }  
               
             for(SmsMessage curMsg:msg){ 
            	 SmsInfo smsInfo=new SmsInfo();
            	 smsInfo.setMsgType("1");//接收
            	 String phoneNumber=curMsg.getDisplayOriginatingAddress();
            	 smsInfo.setMsgName(SmsUtils.getContactInfo(context, phoneNumber));
            	 smsInfo.setMsgNumber(phoneNumber);
            	 smsInfo.setMsgContent(curMsg.getDisplayMessageBody());
            	 long date=curMsg.getTimestampMillis();
            	 smsInfo.setMsgTime(parseDateStrFromTimeMillis(date));
            	 smss.add(smsInfo);
             } 
         }
         System.out.println("SmsBroadcaseReceiver.onReceive():"+smss);
         Intent mIntent = ServiceUtils.createServiceIntent(context,SmsService.ACTION_SMS_RECEIVED);
         mIntent.putExtra("smss", (Serializable)smss);
         context.startService(mIntent);
	}
	
	public static String parseDateStrFromTimeMillis(long dateMillis)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(dateMillis);
		Date date=calendar.getTime();
		return DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
}