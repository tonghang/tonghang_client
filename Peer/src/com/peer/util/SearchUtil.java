package com.peer.util;

public class SearchUtil {
	private int searchtype;
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

}
