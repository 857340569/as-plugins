package com.onlypxz.web.action;

import java.io.IOException;
import java.util.List;

import com.onlypxz.web.base.BaseAction;
import com.onlypxz.web.entity.SmsInfo;
import com.onlypxz.web.service.SmsService;

import zp.base.utils.JsonUtils;

public class SmsAction extends BaseAction {
	public void uploadSms() throws IOException{ 
		String datas=request.getParameter("datas");
		SmsService service=new SmsService();
		service.deleteAll();
		List<SmsInfo> smsInfos=
				//service.query();
		JsonUtils.getEntities(SmsInfo.class, datas);
		boolean result=service.addAll(smsInfos);
		println(String.valueOf(result));
		System.out.//println();
	    println(smsInfos.toString());  
	}  
	public void queryAllSms() throws IOException{ 
		String datas=request.getParameter("datas");
		SmsService service=new SmsService();
		List<SmsInfo> smsInfos=service.query();
	    println(smsInfos.toString());  
	}  
}
