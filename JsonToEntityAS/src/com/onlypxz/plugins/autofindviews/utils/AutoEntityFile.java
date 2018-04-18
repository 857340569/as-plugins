package com.onlypxz.plugins.autofindviews.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 自动生成entity 文件，只支持jsonobject
 *
 * 增加生成kotlin 代码
 * @author zhang
 * @date 2015-7-13
 */
public class AutoEntityFile {

	/**
	 * json 生成实体类内容 for java
	 * @param json
	 * @param className
	 * @return
	 */
	public static String getEntityContent(String json,String className)
	{
		if(StringUtils.isEmpty(json))return "";
		JsonCheck.JSON_TYPE jsonType= JsonCheck.getJSONType(json);
		String entityContent="";
		className=StringUtils.isEmpty(className)?"EntityClass":className;
		entityContent+="public class "+className+"{\n";
		List<String> keys=new ArrayList<String>();
		if(jsonType== JsonCheck.JSON_TYPE.JSON_TYPE_OBJECT)
		{
			JsonObject jsonObject=new JsonParser().parse(json).getAsJsonObject();
			Set<Entry<String, JsonElement>>	fieldSet=jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator= fieldSet.iterator();
			while (iterator.hasNext()) {
				Entry<String, JsonElement> entry = iterator.next();
				String key=entry.getKey();
				JsonElement element=entry.getValue();
				if(element.isJsonPrimitive()||element.isJsonNull())
				{
					keys.add(key);
					entityContent+="    private String "+key+";\n";
				}else if(element.isJsonObject())
				{
					String innerClassName=(key.charAt(0)+"").toUpperCase()+key.substring(1);
					entityContent+="    private "+innerClassName+" "+key+";\n";
					entityContent+=getSetterGetterStrs(innerClassName,key);
					entityContent+=getEntityContent(element.toString(),innerClassName);
				}
				else if(element.isJsonArray())
				{
					JsonArray jsonArray=element.getAsJsonArray();
					entityContent+=dealJsonArray(jsonArray,key);
				}
			}
		}else if(jsonType== JsonCheck.JSON_TYPE.JSON_TYPE_ARRAY)
		{
			JsonArray jsonArray=new JsonParser().parse(json).getAsJsonArray();
			//entityContent=dealJsonArray(jsonArray,className);
			if(jsonArray.size()>0)
			{
				JsonElement temp=jsonArray.get(0);
				if(temp.isJsonPrimitive()||temp.isJsonNull())
				{
					//只有一个字符串，忽略了
				}else
				{
					return getEntityContent(temp.toString(),className);
				}
			}
		}
		entityContent+="\n";
		for(String fieldName:keys)
		{
			entityContent+=getSetterGetterStrs("String",fieldName);
		}
		entityContent+="}";
		return entityContent;
	}
	/**
	 * 处理jsonArray for java
	 * @param jsonArray
	 * @param key
	 * @return
	 */
	private static String dealJsonArray(JsonArray jsonArray,String key)
	{
		String entityContent="";
		if(jsonArray.size()>0)
		{
			JsonElement temp=jsonArray.get(0);
			if(temp.isJsonPrimitive()||temp.isJsonNull())
			{
				entityContent+="    private List<String> "+key+";\n";
				entityContent+=getSetterGetterStrs("List<String>",key);
			}else
			{
				String innerClassName=(key.charAt(0)+"").toUpperCase()+key.substring(1);
				entityContent+="    private List<"+innerClassName+"> "+key+";\n";
				entityContent+=getSetterGetterStrs("List<"+innerClassName+">",key);
				entityContent+=getEntityContent(temp.toString(),innerClassName);
			}
		}
		return entityContent;
	}

	/**
	 * 获取getter setter 方法 for java
	 * @param returnTypeStr
	 * @param fieldName
	 * @return
	 */
	private static String getSetterGetterStrs(String returnTypeStr,String fieldName)
	{
		String content="";
		content+="    public void set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"("+returnTypeStr+" "+fieldName+"){\n";
		content+="        this."+fieldName+"="+fieldName+";\n    }\n";
		content+="\n";
		content+="    public "+returnTypeStr+" get"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"(){\n";
		content+="        return "+fieldName+";\n    }\n";
		return content;
	}
//=========================kotlin====================

	public static Boolean isInnerClass;
	/**
	 * json 生成实体类内容 for kotlin
	 * @param json
	 * @param className
	 * @return
	 */
	public static String getEntityContentKotlin(String json,String className)
	{
		if(StringUtils.isEmpty(json))return "";
		if(isInnerClass==null)
		{
			isInnerClass=false;
		}else if(!isInnerClass)
		{
			isInnerClass=true;
		}
		JsonCheck.JSON_TYPE jsonType= JsonCheck.getJSONType(json);
		String entityContent="";
		className=StringUtils.isEmpty(className)?"EntityClass":className;
		entityContent+=(isInnerClass?"inner class ":"class ")+className+"{\n";
		List<String> keys=new ArrayList<String>();
		if(jsonType== JsonCheck.JSON_TYPE.JSON_TYPE_OBJECT)
		{
			JsonObject jsonObject=new JsonParser().parse(json).getAsJsonObject();
			Set<Entry<String, JsonElement>>	fieldSet=jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator= fieldSet.iterator();
			while (iterator.hasNext()) {
				Entry<String, JsonElement> entry = iterator.next();
				String key=entry.getKey();
				JsonElement element=entry.getValue();
				if(element.isJsonPrimitive()||element.isJsonNull())
				{
					keys.add(key);
					entityContent+=declareKotlinField("String?",key);//"    private var "+key+":String?=null\n";
				}else if(element.isJsonObject())
				{
					String innerClassName=(key.charAt(0)+"").toUpperCase()+key.substring(1);
					entityContent+=declareKotlinField(innerClassName+"?",key);//"    private var"+key+":"+innerClassName+"?=null\n";
					entityContent+=getSetterGetterStrsKotlin(innerClassName,key);
					entityContent+=getEntityContentKotlin(element.toString(),innerClassName);
				}
				else if(element.isJsonArray())
				{
					JsonArray jsonArray=element.getAsJsonArray();
					entityContent+=dealJsonArrayKotlin(jsonArray,key);
				}
			}
		}else if(jsonType== JsonCheck.JSON_TYPE.JSON_TYPE_ARRAY)
		{
			JsonArray jsonArray=new JsonParser().parse(json).getAsJsonArray();
			//entityContent=dealJsonArray(jsonArray,className);
			if(jsonArray.size()>0)
			{
				JsonElement temp=jsonArray.get(0);
				if(temp.isJsonPrimitive()||temp.isJsonNull())
				{
					//只有一个字符串，忽略了
				}else
				{
					return getEntityContentKotlin(temp.toString(),className);
				}
			}
		}
		entityContent+="\n";
		for(String fieldName:keys)
		{
			entityContent+=getSetterGetterStrsKotlin("String?",fieldName);
		}
		entityContent+="}";
		return entityContent;
	}
	/**
	 * 处理jsonArray for kotlin
	 * @param jsonArray
	 * @param key
	 * @return
	 */
	private static String dealJsonArrayKotlin(JsonArray jsonArray,String key)
	{
		String entityContent="";
		if(jsonArray.size()>0)
		{
			JsonElement temp=jsonArray.get(0);
			if(temp.isJsonPrimitive()||temp.isJsonNull())
			{
				entityContent+=declareKotlinField("List<String>?",key);//"    private List<String> "+key+";\n";
				entityContent+=getSetterGetterStrsKotlin("List<String>?",key);
			}else
			{
				String innerClassName=(key.charAt(0)+"").toUpperCase()+key.substring(1);
				entityContent+=declareKotlinField("List<"+innerClassName+">?",key);//"    private List<"+innerClassName+"> "+key+";\n";
				entityContent+=getSetterGetterStrsKotlin("List<"+innerClassName+">?",key);
				entityContent+=getEntityContentKotlin(temp.toString(),innerClassName);
			}
		}
		return entityContent;
	}


	/**
	 * 获取getter setter 方法 for kotlin
	 * @param returnTypeStr
	 * @param fieldName
	 * @return
	 */

	private static String getSetterGetterStrsKotlin(String returnTypeStr,String fieldName)
	{
		String content="";
		content+="     fun set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"("+fieldName+":"+returnTypeStr+"){\n";
		content+="        this."+fieldName+"="+fieldName+"\n    }\n";
		content+="\n";
		content+="    fun get"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"():"+returnTypeStr+"{\n";
		content+="        return "+fieldName+"\n    }\n";
		return content;
	}
	private static String declareKotlinField(String typeStr,String nameStr)
	{
		return "    private var "+nameStr+":"+typeStr+"=null\n";
	}
}
