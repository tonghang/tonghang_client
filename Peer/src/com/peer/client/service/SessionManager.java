package com.peer.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import android.os.RemoteException;

import com.peer.IMimplements.RingLetterImp;
import com.peer.IMinterface.IM;
import com.peer.client.ISessionListener;
import com.peer.client.ISessionManager;
import com.peer.client.User;
import com.peer.client.util.DataUtil;
import com.peer.constant.Constant;

public class SessionManager extends ISessionManager.Stub {
	private String token=null;
	private String huanxin_user=null;
	private String userid=null;
	/*web login*/
	@Override
	public User login(String email, String password,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		User user=null;
		try {
			parts.put("email", email);
			parts.put("password", password);			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "login.json"
					,parts, Map.class);						
			Map body = result.getBody();			
			token=(String) body.get("token");
			huanxin_user=(String)body.get("huanxin_user");						
			
			if (result.getStatusCode() == HttpStatus.OK) {
				code=0;
				user=new User();
				Map u=(Map) body.get("user");
				String id=(String)u.get("id");
				userid=id;
				user.setId(id);
				user.setEmail((String)u.get("email"));
//				u.get("password");
				user.setUsername((String)u.get("username"));
				user.setLabels((List) u.get("labels"));				
				message = Constant.CALLBACKSUCCESS;
			} else {
				message = Constant.CALLBACKFAIL;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		callback.onCallBack(code, message);
		return user;
	}
	/*user register acount*/
	@Override
	public void register(String email, String password, String username,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		try {
			parts.put("email", email);
			parts.put("username", username);
			parts.put("password", password);
			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "users.json"
					, parts, Map.class);		
					
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code=0;
				Map user=(Map) body.get("user");
				user.get("id");
				user.get("email");
				user.get("password");
				user.get("username");								
//				message = (String) body.get("code");
			} else {
//				message = (String) body.get("code");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*register label*/
	@Override
	public void registerLabel(List<String> labels, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		String message = null;
		int code=1;
		try {
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "update_labels.json", labels, Map.class);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code = 0;
				
				List list=(List) body.get("labels");
				
				message = (String) result.getBody().get("message");
			} else {
				message = (String) result.getBody().get("message");
			}
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*commite personal message*/
	@Override
	public void profileUpdate(String nickName, String birthday, String city,
			String sex, String filename, byte[] image, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		try {
			parts.put("birth", birthday);
			parts.put("username", nickName);
			parts.put("sex", sex);
			
			DataUtil.putEntity(Constant.WEB_SERVER_ADDRESS + "users/3.json", parts, Map.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	/*To add someone as a friend*/
	@Override
	public void addFriends(String targetId, String reason,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		try {
			parts.put("invitee_id", targetId);
			parts.put("reason", reason);			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "invitations.json", parts, Map.class);			
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/* delete friend*/
	@Override
	public void deleteFriends(String targetId)
			throws RemoteException {
		// TODO Auto-generated method stub
		try {						
			DataUtil.deleteEntity(Constant.WEB_SERVER_ADDRESS + "friends/"+targetId+".json", Map.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	/*search label*/
	@Override
	public List<String> search(String label)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/*refuse someone add as friend*/
	@Override
	public void refuseAddFriends(String sourceId) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "invitations/"+sourceId+"/refuse.json", "", Map.class);			
			Map body = result.getBody();		
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
	}
	/*aggree someone add as friend*/
	@Override
	public void agreeAddFriends(String sourceId) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "invitations/"+sourceId+"/agree.json", "", Map.class);			
			Map body = result.getBody();		
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
	
		
		
	}
	/*search user by label*/
	@Override
	public List<User> searchUserByLabel(String label) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/*search user by nike*/
	@Override
	public List<User> searchUsersByNickName(String username)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/*get all friends*/
	@Override
	public List<User> myFriends() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "users/"+userid+"/friends.json", "", Map.class);			
			Map body = result.getBody();		
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
		return null;
	}
	/*All want to add me as a friend 好友请求列表*/
	@Override
	public List<User> Invitations() throws RemoteException {
		// TODO Auto-generated method stub
		DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "users/"+userid+"/invitations.json", "", Map.class);
		return null;
	}
	/*someone personal page*/
	@Override
	public User personalPage(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/*label topic history*/
	@Override
	public List<Map> topicHistory(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();			
		String message = null;
		int code=1;
		try {
			parts.put("user_id", userid);
			parts.put("page", "1");
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS + "topics.json", Map.class, parts);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code = 0;
				
				List list=(List) body.get("labels");
				
//				message = (String) result.getBody().get("message");
			} else {
//				message = (String) result.getBody().get("message");
			}
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}		
		return null;
	}
	/*label topic chat history*/
	@Override
	public void TopicchatHistory(String topicId) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();			
		String message = null;
		int code=1;
		try {
			parts.put("user_id", userid);
			parts.put("page", "1");
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS + "topics.json", Map.class, parts);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code = 0;
				
				List list=(List) body.get("labels");
				
//				message = (String) result.getBody().get("message");
			} else {
//				message = (String) result.getBody().get("message");
			}
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}		
	}
	/*forget password*/
	@Override
	public void forgetPassword(String tagetId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	/*update password*/
	@Override
	public void updatePassword(String tagetId, String newPassword)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	/*creat topic*/
	@Override
	public void creatTopic(String label, String topic, String userId)
			throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();			
		String message = null;
		int code=1;
		try {
			parts.put("body", topic);
			parts.put("label_name", label);			
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS + "topics.json", Map.class, parts);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code = 0;
				
				List list=(List) body.get("labels");
				
//				message = (String) result.getBody().get("message");
			} else {
//				message = (String) result.getBody().get("message");
			}
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}		
	}
	@Override
	public String getToken() throws RemoteException {
		// TODO Auto-generated method stub
		return token;
	}
	@Override
	public String getHuanxingUser() throws RemoteException {
		// TODO Auto-generated method stub
		return huanxin_user;
	}
	@Override
	public String getUserId() throws RemoteException {
		// TODO Auto-generated method stub
		return userid;
	}
}
