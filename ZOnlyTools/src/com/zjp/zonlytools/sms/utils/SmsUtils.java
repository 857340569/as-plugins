package com.zjp.zonlytools.sms.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zjp.zonlytools.entity.SmsInfo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

/**
 * @author zjp
 * @date 2017年5月12日
 */
public class SmsUtils {
	// --------------------------------收到的短息信息----------------------------------
	public static List<SmsInfo> getSmsInfos(Context context) {
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		final String SMS_URI_INBOX = "content://sms/inbox";// 收信箱
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
		Uri uri = Uri.parse(SMS_URI_INBOX);
		Cursor cursor = cr.query(uri, projection, null, null, "date desc");
		while (cursor.moveToNext()) {
			SmsInfo messageInfo = new SmsInfo();
			// -----------------------信息----------------
			int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号
			int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号
			int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容
			int dateColumn = cursor.getColumnIndex("date");// 日期
			int typeColumn = cursor.getColumnIndex("type");// 收发类型
			String nameId = cursor.getString(nameColumn);
			String phoneNumber = cursor.getString(phoneNumberColumn);
			String smsbody = cursor.getString(smsbodyColumn);
			String msgType = cursor.getString(typeColumn);// 1表示接收 2表示发送

			Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = dateFormat.format(d);

			// --------------------------匹配联系人名字--------------------------

			Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
			Cursor localCursor = cr.query(personUri,
					new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup.PHOTO_ID, PhoneLookup._ID }, null, null, null);
			String name = "";
			if (localCursor != null && localCursor.getCount() != 0) {
				localCursor.moveToFirst();
				name = localCursor.getString(localCursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
				long photoid = localCursor.getLong(localCursor.getColumnIndex(PhoneLookup.PHOTO_ID));
				long contactid = localCursor.getLong(localCursor.getColumnIndex(PhoneLookup._ID));

				// 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
				if (photoid > 0) {
					Uri uri1 = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri1);
					// messageInfo.setContactPhoto(BitmapFactory.decodeStream(input));
				} else {
					// messageInfo.setContactPhoto(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher));
				}
			} else {// 没保存的联系人
				// messageInfo.setMsgFromNumber(phoneNumber);
				// messageInfo.setContactPhoto(BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.ic_launcher));
			}

			localCursor.close();
			if ("1".equals(msgType)) {
				messageInfo.setMsgFromName(name);
				messageInfo.setMsgFromNumber(phoneNumber);
			} else {
				messageInfo.setMsgToName(name);
				messageInfo.setMsgToNumber(phoneNumber);
			}
			messageInfo.setMsgType(msgType);
			messageInfo.setMsgContent(smsbody);
			messageInfo.setMsgTime(date);
			list.add(messageInfo);
		}

		return list;
	}

}
