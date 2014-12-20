package com.peer.constant;
/**
 * Some constants
 * @author Concoon-break
 *
 */
public interface Constant {
	/*personalpage type*/
	public int OWNPAGE=1;
	public int FRIENDSPAGE=2;
	public int UNFRIENDSPAGE=3;
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
	public int SEARCHSKILL=1;
	public int SEARCHUSER=2;
	/*web server*/
	 String SERVER_ADDRESS = "10.0.2.2:3000";
	 int WEB_SERVER_PORT = 3000;
	 String WEB_SERVER_ADDRESS="http://" + SERVER_ADDRESS+"/";
	 /*connection status*/
	 String CONNECTION="connection";
	 String DISCONNECTION="disconnection";
	 /*imageurl*/
	 String IMAGEURL="imageurl";
	 /*callback status*/
	 String CALLBACKSUCCESS="success";
	 String CALLBACKFAIL="fail";
	 
	 
}
