package com.peer.IMimplements;

public interface IM {
	
	public void login(String email,String password);
	
	public void register(String email,String password,String username);
	
	public void sendMessage(String content,int chattype,String targetId);
	
}
