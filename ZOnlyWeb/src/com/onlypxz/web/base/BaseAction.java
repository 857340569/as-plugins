package com.onlypxz.web.base;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import zp.base.utils.SqlHelper;
import zp.base.utils.StringUtils;

public class BaseAction extends ActionSupport {
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	public BaseAction() {
		init();
		
	}
	private void init()
	{
		SqlHelper.db_config_name="db_onlyweb";
		response=ServletActionContext.getResponse();  
		request=ServletActionContext.getRequest();
		 /* 
	     * 在调用getWriter之前未设置编码(既调用setContentType或者setCharacterEncoding方法设置编码), 
	     * HttpServletResponse则会返回一个用默认的编码(既ISO-8859-1)编码的PrintWriter实例。这样就会 
	     * 造成中文乱码。而且设置编码时必须在调用getWriter之前设置,不然是无效的。 
	     * */  
		try {
			response.setContentType("text/html;charset=utf-8");  
			response.setCharacterEncoding("UTF-8");  
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
	}
	
	protected void println(String data) {  
		try {
			   
			    PrintWriter out = response.getWriter();  
			    out.println(data);  
			    out.flush();  
			    out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}  
//	protected void onServletAction()
	/**
	 * 获取数据
	 * @param tClass
	 * @return
	 */
	protected <T> T onRequest(Class<T> tClass) {
		T t=null;
		try {
			t = tClass.newInstance();
			Field[] fields=tClass.getDeclaredFields();
			for(Field field:fields)
			{
				field.setAccessible(true);
				try{
					String fielName=field.getName();
					String val=request.getParameter(fielName);
					if(StringUtils.isEmpty(val))continue;
					field.set(t,val);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}
}
