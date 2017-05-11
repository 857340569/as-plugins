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
			     * �ڵ���getWriter֮ǰδ���ñ���(�ȵ���setContentType����setCharacterEncoding�������ñ���), 
			     * HttpServletResponse��᷵��һ����Ĭ�ϵı���(��ISO-8859-1)�����PrintWriterʵ���������ͻ� 
			     * ����������롣�������ñ���ʱ�����ڵ���getWriter֮ǰ����,��Ȼ����Ч�ġ� 
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
