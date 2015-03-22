// ISessionManager.aidl
package com.peer.client;

// Declare any non-default types here with import statements
import com.peer.client.ISessionListener;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.client.easemobchatUser;

interface ISessionManager {

	String getHuanxingUser();
	
	String getUserId();
	
	String getImagUrL();
	
	String getUserName();
	
	List<String> getLabels();
	
    com.peer.client.User login(String username, String password, ISessionListener callback);    
    
    void register(String email, String password, String username, ISessionListener callback);
   
    void registerLabel(in List<String> labels,ISessionListener callback);
 		
 	com.peer.client.User profileUpdate(String nickName, String birthday, String city, String sex, String filename, in byte[] image, ISessionListener callback);
 	
 	void addFriends(String targetId,String reason,ISessionListener callback);
 	
 	void refuseAddFriends(String sourceId,ISessionListener callback);
 	
 	void agreeAddFriends(String sourceId,ISessionListener callback);
 	
    List<String> search(String label,ISessionListener callback);
    
    List<com.peer.client.Topic> searchTopicByLabel(String label,int page,ISessionListener callback);
    
    List<com.peer.client.Topic> searchTopicBykey(String key,int page,ISessionListener callback);
    
    List<com.peer.client.User> searchUsersByNickName(String username,int page,ISessionListener callback);
    
    List<com.peer.client.User> searchUsersByLabel(String labename,int page,ISessionListener callback);
 	
 	List recommendByPage(ISessionListener callback);
 	
 	List recommend(int page, ISessionListener callback);
 	
 	List convertToUser(in com.peer.client.easemobchatUser users, ISessionListener callback);
 
 	List<com.peer.client.User> myFriends();
 	
 	List<com.peer.client.User> Invitations(ISessionListener callback);
 	
 	com.peer.client.User personalPage(String targetId,ISessionListener callback);
 	
 	List<com.peer.client.Topic> topicHistory(String targetId,ISessionListener callback);
 	
 	void TopicchatHistory(String topicId,ISessionListener callback);
 	
 	void forgetPassword(String tagetId,ISessionListener callback);
 	
 	void updatePassword(String newPassword, ISessionListener callback);
 	
 	Topic creatTopic(String label,String topic);
 	
 	void setLabels(in List<String> labels);
 	
 	List<com.peer.client.User> inTopicUser(String topicid,ISessionListener callback);
 	
 	void feedback(String content,ISessionListener callback);
 	
 	List TopicReplies(String topicId,ISessionListener callback);
 	
 	void deleteFriend(String targetId,ISessionListener callback);
 	
 	void replyTopic(String topicId,String content, ISessionListener callback);
}
