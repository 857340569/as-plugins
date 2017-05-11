package com.onlypxz.web.base;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	
	protected void println(String data) {  
		try {
			 HttpServletResponse response=ServletActionContext.getResponse();  
			    /* 
			     * 在调用getWriter之前未设置编码(既调用setContentType或者setCharacterEncoding方法设置编码), 
			     * HttpServletResponse则会返回一个用默认的编码(既ISO-8859-1)编码的PrintWriter实例。这样就会 
			     * 造成中文乱码。而且设置编码时必须在调用getWriter之前设置,不然是无效的。 
			     * */  
			    response.setContentType("text/html;charset=utf-8");  
			    response.setCharacterEncoding("UTF-8");  
			    PrintWriter out = response.getWriter();  
			    out.println(data);  
			    out.flush();  
			    out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}  
}
