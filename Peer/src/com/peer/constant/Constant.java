package com.peer.constant;
/**
 * Some constants
 * @author Concoon-break
 *
 */
public interface Constant {
	public int REFRESHHANDLE=321;
	
	public String AGREED="agreed";
	
	public String PENDING="pending";
	
	public String ISFLOAT="isfloat";
		
	/*LocalStorage key*/
	public String EMAIL="email";	
	
	public String LOGINED="logined";
	public String LOGOUT="logout";
	
	
	/*homepage recommend user or topic*/
	public String USER="user";
	public String TOPIC="topic";
	public String TIME="time";
	
	/*chatroom type*/
	public int SINGLECHAT=1;
	public int MULTICHAT=2;
	/*msg from*/
	public int OTHER=0;
	public int SELF=1;
	/*login status*/
	public String LOGINSUCCESS="success";
	public String LOGINFAIL="fail";
	/*search type*/
	public String LABELTOPIC="topicuser";
	public String TOPICBYTOPIC="topicbytopic";
	public String LABELUSER="labeluser";
	public String USERBYNIKE="userbynike";
	public String USERBYLABEL="userbylabel";
	public String TOPICLIST="topiclist";
	/*search int type*/
	public int TOPIC_LABEL=1;
	public int TOPIC_TOPICKEY=2;
	public int USER_LABEL=3;
	public int USER_NICK=4;
	
	/*web server*/
	//	 String SERVER_ADDRESS = "10.0.2.2:3000";
	String SERVER_ADDRESS = "114.215.143.83:3000";
	
	 int WEB_SERVER_PORT = 3000;
	 String WEB_SERVER_ADDRESS="http://" + SERVER_ADDRESS;
	 /*connection status*/
	 String CONNECTION="connection";
	 String DISCONNECTION="disconnection";
	 /*imageurl*/
	 String IMAGEURL="imageurl";
	 String USERID="userid";
	 /*callback status*/
	 String CALLBACKSUCCESS="success";
	 String CALLBACKFAIL="fail"; 
	 /*restart app*/
	 String RELOGIN="relogin";
	 
	//用于进入聊天室intent.putExtras的键
	    String IMAGE="image";
	    String OWNERNIKE="ownernike";
	    String THEME="theme";
	    String TAGNAME="tagname";
	    String ROOMID="roomid";
	    String TOPICID="topicid";
	    String FROMFLOAT="float";
}
