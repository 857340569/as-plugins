package com.onlypxz.web.entity;

import java.io.Serializable;

/**
 * @author zjp
 * @date 2017年5月11日
 */
public class SmsInfo implements Serializable{
	
	private String id;
	private String msgType;//收发类型  1表示接收  2表示发送  
	private String msgNumber;
	private String msgName;
	private String msgTime;
	private String msgContent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public String getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(String msgNumber) {
		this.msgNumber = msgNumber;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Override
	public String toString() {
		return "SmsInfo [id=" + id + ", msgType=" + msgType + ", msgNumber=" + msgNumber + ", msgName=" + msgName
				+ ", msgTime=" + msgTime + ", msgContent=" + msgContent + ", getId()=" + getId() + ", getMsgType()="
				+ getMsgType() + ", getMsgNumber()=" + getMsgNumber() + ", getMsgName()=" + getMsgName()
				+ ", getMsgTime()=" + getMsgTime() + ", getMsgContent()=" + getMsgContent() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
