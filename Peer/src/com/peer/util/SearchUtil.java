package com.peer.util;

public class SearchUtil {
	private int searchtype;
	private String searchname;
	private static SearchUtil searchutil=null;
	private SearchUtil(){}
	public static SearchUtil getInstance(){
		if(searchutil==null){
			searchutil=new SearchUtil();
		}
		return searchutil;
	}
	public int getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(int searchtype) {
		this.searchtype = searchtype;
	}
	public String getSearchname() {
		return searchname;
	}
	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

}
