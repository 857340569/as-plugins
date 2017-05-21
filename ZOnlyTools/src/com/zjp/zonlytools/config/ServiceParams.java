package com.zjp.zonlytools.config;

import zp.android.baseapp.setting.BaseSystemConfig;
import zp.android.baseapp.setting.IServiceParameters;
import zp.baseandroid.common.utils.StringUtils;

public enum ServiceParams implements IServiceParameters{
	/**
	 * 版本检查
	 */
	VERSION_CHECK("S000_GetSoftwareInfo","版本信息检测"),
	
	UPLOAD_ALL_SMS("uploadSms","上传所有的短信");
	//请求的action
	private final String action;
	//优先服务请求url
	private final String specialUrl;
	//备注
	private String remark;
	private ServiceParams(String action,String remark)
	{
		this(action, null, remark);
	}
	private ServiceParams(String action,String specialUrl,String remark)
	{
		this.action=action;
		this.specialUrl=specialUrl;
		this.remark=remark;
	}
	public String getRemark() {
		return remark;
	}
	
	public String toString()
	{
		String url=BaseSystemConfig.ServerUrl+action;
		if(!StringUtils.isEmpty(specialUrl))
		{
			url=specialUrl+action;
		}
		return url;
	}
}
