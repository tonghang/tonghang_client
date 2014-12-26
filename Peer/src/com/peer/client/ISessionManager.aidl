// ISessionManager.aidl
package com.peer.client;

// Declare any non-default types here with import statements
import com.peer.client.ISessionListener;
import com.peer.client.User;

interface ISessionManager {

	String getToken();
	
	String getHuanxingUser();
	
	String getUserId();
	
	List<String> getLabels();
	
    com.peer.client.User login(String username, String password, ISessionListener callback);    
    
    void register(String email, String password, String username, ISessionListener callback);
   
    void registerLabel(in List<String> labels);
 		
 	void profileUpdate(String nickName, String birthday, String city, String sex, String filename, in byte[] image, ISessionListener callback);
 	
 	void addFriends(String targetId,String reason,ISessionListener callback);
 	
 	void refuseAddFriends(String sourceId);
 	
 	void agreeAddFriends(String sourceId);
 	
 	void deleteFriends(String targetId);
 	
    List<String> search(String label,ISessionListener callback);
    
    List<com.peer.client.User> searchUserByLabel(String label,ISessionListener callback);
    
    List<com.peer.client.User> searchUsersByNickName(String username,ISessionListener callback);
 	
 //	recommendByPage(int page, ISessionListener callback);
 
 	List<com.peer.client.User> myFriends();
 	
 	List<com.peer.client.User> Invitations();
 	
 	com.peer.client.User personalPage(String targetId,ISessionListener callback);
 	
 	List topicHistory(String targetId,ISessionListener callback);
 	
 	void TopicchatHistory(String topicId,ISessionListener callback);
 	
 	void forgetPassword(String tagetId);
 	
 	void updatePassword(String newPassword);
 	
 	void creatTopic(String label,String topic);
 	
}
