// ISessionManager.aidl
package com.peer.client;

// Declare any non-default types here with import statements
import com.peer.client.ISessionListener;
import com.peer.client.User;

interface ISessionManager {

    void login(String username, String password, ISessionListener callback);    
    
    void register(String email, String password, String username, ISessionListener callback);
   
    void registerLabel(in List<String> labels, ISessionListener callback);
 	
 	void addFriends(String targetId,String reason,ISessionListener callback);
 	
 	void refuseAddFriends(String sourceId);
 	
 	void agreeAddFriends(String sourceId);
 	
 	void deleteFriends(String targetId,ISessionListener callback);
 	
    List<String> search(String label);
    
    List<com.peer.client.User> searchUserByLabel(String label);
    
    List<com.peer.client.User> searchUsersByNickName(String username);
 	
 //	recommendByPage(int page, ISessionListener callback);
 
 	List<com.peer.client.User> myFriends(String targetId);
 	
 	List<com.peer.client.User> newFriends(String targetId);
 	
 	com.peer.client.User personalPage(String targetId);
 	
 	List topicHistory(String targetId);
 	
 	void chatHistory(String topicId);
 	
 	void forgetPassword(String tagetId);
 	
 	void updatePassword(String tagetId,String newPassword);
 	
}
