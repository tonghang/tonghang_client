package com.peer.util;

public class SearchUtil {
	private String searchtype;
	private String searchname;
	private static SearchUtil searchutil=null;
	private SearchUtil(){}
	public static SearchUtil getInstance(){
		if(searchutil==null){
			searchutil=new SearchUtil();
		}
		return searchutil;
	}
	
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getSearchname() {
		return searchname;
	}
	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

}
