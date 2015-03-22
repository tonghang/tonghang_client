package com.peer.util;

import com.peer.client.User;

/**
 * Personpage type Util
 * @author Cocoon-break
 *
 */
public class PersonpageUtil {
//	private int personpagetype;	
//	private String personid;
//	private String huanxinId;
//	private String personname;
//	private String personImg;
	private boolean shouldRefresh;
	private User user;
	private static PersonpageUtil personpageutil;
	private PersonpageUtil(){}
	public static PersonpageUtil getInstance(){
		if(personpageutil==null){
			personpageutil=new PersonpageUtil();
		}		
		return personpageutil;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isShouldRefresh() {
		return shouldRefresh;
	}
	public void setShouldRefresh(boolean shouldRefresh) {
		this.shouldRefresh = shouldRefresh;
	}
	
	/*
	public int getPersonpagetype() {
		return personpagetype;
	}
	public void setPersonpagetype(int personpagetype) {
		this.personpagetype = personpagetype;
	}
	
	public String getPersonid() {
		return personid;
	}
	public void setPersonid(String personid) {
		this.personid = personid;
	}
	public String getHuanxinId() {
		return huanxinId;
	}
	public void setHuanxinId(String huanxinId) {
		this.huanxinId = huanxinId;
	}
	public String getPersonname() {
		return personname;
	}
	public void setPersonname(String personname) {
		this.personname = personname;
	}
	public String getPersonImg() {
		return personImg;
	}
	public void setPersonImg(String personImg) {
		this.personImg = personImg;
	}	
	*/
}
