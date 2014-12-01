package com.peer.client.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import android.os.RemoteException;
import com.peer.client.ISessionListener;
import com.peer.client.ISessionManager;
import com.peer.client.User;
import com.peer.client.util.DataUtil;
import com.peer.constant.Constant;

public class SessionManager extends ISessionManager.Stub {

	@Override
	public void login(String username, String password,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		MultiValueMap<String, String> parts = new LinkedMultiValueMap<String, String>();
		String message = null;
		int code=1;
		try {
			parts.add("username", username);
			parts.add("password", password);
			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "login.json", parts, Map.class);			
			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code=0;
				Map user=(Map) body.get("user");
				user.get("id");
				user.get("email");
				user.get("password");
				user.get("username");								
				message = (String) body.get("code");
			} else {
				message = (String) body.get("code");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
		
	}

	@Override
	public void register(String email, String username, String password,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		MultiValueMap<String, String> parts = new LinkedMultiValueMap<String, String>();
		String message = null;
		int code=1;
		try {
			parts.add("email", email);
			parts.add("username", username);
			parts.add("password", password);
			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "users.json", parts, Map.class);			
			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				code=0;
				Map user=(Map) body.get("user");
				user.get("id");
				user.get("email");
				user.get("password");
				user.get("username");								
				message = (String) body.get("code");
			} else {
				message = (String) body.get("code");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}

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

	@Override
	public void addFriends(String targetId, String reason,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFriends(String targetId, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> search(String label)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refuseAddFriends(String sourceId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agreeAddFriends(String sourceId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> searchUserByLabel(String label) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> searchUsersByNickName(String username)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> myFriends(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> newFriends(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User personalPage(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> topicHistory(String targetId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void chatHistory(String topicId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forgetPassword(String tagetId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
