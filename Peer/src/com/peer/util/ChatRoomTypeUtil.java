package com.peer.util;
/**
 * chatroom type util 
 * @author Concoon-break
 *
 */
public class ChatRoomTypeUtil {
	private int chatroomtype;
	private String userId;
	private String name;
	private String Image;
	private String theme;
	private String nike;
	private static ChatRoomTypeUtil chatroomtypeutil;
	private ChatRoomTypeUtil(){}
	public static ChatRoomTypeUtil getInstance(){
		if(chatroomtypeutil==null){
			chatroomtypeutil=new ChatRoomTypeUtil();
		}
		return chatroomtypeutil;
	}
	public int getChatroomtype() {
		return chatroomtype;
	}
	public void setChatroomtype(int chatroomtype) {
		this.chatroomtype = chatroomtype;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getNike() {
		return nike;
	}
	public void setNike(String nike) {
		this.nike = nike;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
