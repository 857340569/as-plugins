package com.onlypxz.web.action;

import java.io.IOException;

import com.onlypxz.web.base.BaseAction;

public class LoginAction extends BaseAction {
	private String name;
	public String login()
	{
		System.out.println(name);
		return "json";
	}
	
	public void showResult() throws IOException{  
	    //JSON�ڴ��ݹ���������ͨ�ַ�����ʽ���ݵģ������ƴ��һ��������  
	    String jsonString="{\"user\":{\"id\":\"123\",\"name\":\"����\",\"say\":\"Hello , i am a action to print a json!\",\"password\":\"JSON\"},\"success\":true}";  
	    println(jsonString);  
	}  
	public void test2() throws IOException{  
	    //JSON�ڴ��ݹ���������ͨ�ַ�����ʽ���ݵģ������ƴ��һ��������  
	    String jsonString="{\"user\":{\"id\":\"123\",\"name\":\"����\",\"say\":\"Hello , i am a action to print a json!\",\"password\":\"JSON\"}}";  
	    println(jsonString);  
	}  
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
