package com.peer.util;
/**
 * Personpage type Util
 * @author Cocoon-break
 *
 */
public class PersonpageUtil {
	private int personpagetype;	
	private String personid;
	private static PersonpageUtil personpageutil;
	private PersonpageUtil(){}
	public static PersonpageUtil getInstance(){
		if(personpageutil==null){
			personpageutil=new PersonpageUtil();
		}		
		return personpageutil;
	}
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
	
}
