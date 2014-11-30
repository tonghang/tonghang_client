package com.peer.entity;

/**
 * �?��消息的JavaBean
 * 
 * @author way
 * 
 */
public class ChatMsgEntity {
	private String userId;
	private String image;
	private String name;//消息来自
	private String date;//消息日期
	private String message;//消息内容
	//private boolean isComMeg = true;// 是否为收到的消息
	private int isComMeg;//收到哪种消息�?为我�?为房主，2为其他人

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMsgType() {
		return isComMeg;
	}

	public void setMsgType(int isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatMsgEntity() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ChatMsgEntity(String name, String date, String text, int isComMsg) {
		super();
		this.name = name;
		this.date = date;
		this.message = text;
		this.isComMeg = isComMsg;
	}

}
