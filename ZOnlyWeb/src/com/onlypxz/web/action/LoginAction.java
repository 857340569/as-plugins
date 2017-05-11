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
	    //JSON在传递过程中是普通字符串形式传递的，这里简单拼接一个做测试  
	    String jsonString="{\"user\":{\"id\":\"123\",\"name\":\"张三\",\"say\":\"Hello , i am a action to print a json!\",\"password\":\"JSON\"},\"success\":true}";  
	    println(jsonString);  
	}  
	public void test2() throws IOException{  
	    //JSON在传递过程中是普通字符串形式传递的，这里简单拼接一个做测试  
	    String jsonString="{\"user\":{\"id\":\"123\",\"name\":\"张三\",\"say\":\"Hello , i am a action to print a json!\",\"password\":\"JSON\"}}";  
	    println(jsonString);  
	}  
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
