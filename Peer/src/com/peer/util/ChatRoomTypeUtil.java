package com.peer.util;
/**
 * chatroom type util 
 * @author Concoon-break
 *
 */
public class ChatRoomTypeUtil {
	private int chatroomtype;
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

}
