package com.peer.IMinterface;

public interface IM {
	
	public void login(String email,String password);
	
	public void setAppInited();
	
	public boolean register(String email,String password,String username);
	
	public void sendMessage(String content,int chattype,String targetId,String imageUrl);
	
	public void clearConversation(String targetname);
	
	public void loadConversationsandGroups();
	
	public int getUnreadMesTotal();
	
	public void joingroup(String groupid);
}
