package com.peer.client.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.RemoteException;

import com.peer.client.ISessionListener;
import com.peer.client.ISessionManager;
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
			RestTemplate restTemplate=new RestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		    restTemplate.postForObject(Constant.WEB_SERVER_ADDRESS + "login.json", parts, Object.class);

//			ResponseEntity<Map> result = DataUtil.postForm(Constant.WEB_SERVER_ADDRESS + "login.json", parts, Map.class);
			
			
//			Map body = result.getBody();
//			if (result.getStatusCode() == HttpStatus.OK) {
//				code=0;
////				userId = (String) body.get("userId");
////				sessionId = (String) body.get("sessionId");	
////				nickName  = (String) body.get("nickName"); 
//				message = (String) body.get("message");
////				ok = true;
//			} else {
//				message = (String) body.get("message");
//			}
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
		
	}

	@Override
	public String getUserId() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSessionId() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
