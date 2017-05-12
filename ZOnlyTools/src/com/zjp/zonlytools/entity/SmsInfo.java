package com.zjp.zonlytools.entity;

/**
 * @author zjp
 * @date 2017年5月11日
 */
public class SmsInfo {
	
	private int id;
	private String msgType;//收发类型  1表示接收  2表示发送  
	private String msgFromNumber;
	private String msgFromName;
	private String msgToNumber;
	private String msgToName;
	private String msgTime;
	private String msgContent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgFromNumber() {
		return msgFromNumber;
	}

	public void setMsgFromNumber(String msgFromNumber) {
		this.msgFromNumber = msgFromNumber;
	}

	public String getMsgFromName() {
		return msgFromName;
	}

	public void setMsgFromName(String msgFromName) {
		this.msgFromName = msgFromName;
	}

	public String getMsgToNumber() {
		return msgToNumber;
	}

	public void setMsgToNumber(String msgToNumber) {
		this.msgToNumber = msgToNumber;
	}

	public String getMsgToName() {
		return msgToName;
	}

	public void setMsgToName(String msgToName) {
		this.msgToName = msgToName;
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
		return "SmsInfo [id=" + id + ", msgType=" + msgType + ", msgFromNumber=" + msgFromNumber + ", msgFromName="
				+ msgFromName + ", msgToNumber=" + msgToNumber + ", msgToName=" + msgToName + ", msgTime=" + msgTime
				+ ", msgContent=" + msgContent + "]";
	}
}
