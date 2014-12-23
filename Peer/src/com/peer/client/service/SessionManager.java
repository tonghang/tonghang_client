package com.peer.client.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.RemoteException;
import com.peer.client.ISessionListener;
import com.peer.client.ISessionManager;
import com.peer.client.User;
import com.peer.client.util.DataUtil;
import com.peer.constant.Constant;

public class SessionManager extends ISessionManager.Stub {
	private String token=null;
	private String huanxin_user=null;
	private String userid=null;
	/*web login 通过测试*/
	@Override
	public User login(String email, String password,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		User user=null;
		try {
			parts.put("email", "wu@gmail.com");
			parts.put("password", "aaaaaa");			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "login.json"
					,parts, Map.class);						
			Map body = result.getBody();			
			token=(String) body.get("token");
			huanxin_user=(String)body.get("huanxin_user");						
			
			if (result.getStatusCode() == HttpStatus.OK) {
				code=0;
				user=new User();
				Map u=(Map) body.get("user");				
				String id=String.valueOf(u.get("id"));
				userid=id;
				user.setId(id);
				user.setEmail((String)u.get("email"));
//				user.setPassword((String)u.get("password"));
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
				String id=String.valueOf(body.get("id"));
				userid=id;				
				message = Constant.CALLBACKSUCCESS;
			} else {
				message = Constant.CALLBACKFAIL;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*register label 通过测试*/
	@Override
	public void registerLabel(List<String> labels, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, List> parts=new HashMap<String, List>();
		String message = null;
		int code=1;
		try {
			parts.put("labels", labels);
			DataUtil.putEntity(Constant.WEB_SERVER_ADDRESS +"users/"+userid+"/update_labels.json", parts, Map.class);			
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*commite personal message*/
	@Override
	public void profileUpdate(String nickName, String birthday, String city,
			String sex, final String filename, byte[] image, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		ByteArrayResource imageFile = new ByteArrayResource(image){
			public String getFilename() throws IllegalStateException {
				return "sign.jpg";
			}
		};	
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		String message = null;
		int code=1;
		try {
			parts.add("birth", birthday);
			parts.add("username", nickName);
			parts.add("sex", sex);
			parts.add("image", imageFile);
			parts.add("filename", "sign.jpg");
			
			HttpHeaders headers=new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);			
			RestTemplate restTemplate=new RestTemplate(true);			
			List<HttpMessageConverter<?>> formPartConverters = new ArrayList<HttpMessageConverter<?>>();
			formPartConverters.add(new ByteArrayHttpMessageConverter());
			StringHttpMessageConverter formStringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
			formStringHttpMessageConverter.setWriteAcceptCharset(false);
			formPartConverters.add(formStringHttpMessageConverter);
			formPartConverters.add(new ResourceHttpMessageConverter());
			
			List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
			partConverters.addAll(formPartConverters);
			
			FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
			formConverter.setCharset(Charset.forName("UTF-8"));
		    formConverter.setPartConverters(formPartConverters);
		    
		    partConverters.add(formConverter);
		    partConverters.add(new GsonHttpMessageConverter());
		    
		    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		    restTemplate.setMessageConverters(partConverters);
						
			ResponseEntity<Map> response=restTemplate.exchange(Constant.WEB_SERVER_ADDRESS + "users/"+userid+".json", HttpMethod.PUT, requestEntity, Map.class);
			response.getBody();			
			
	//		DataUtil.putEntity(Constant.WEB_SERVER_ADDRESS + "users/"+userid+".json", parts, Map.class);
			code=0;
			message=Constant.CALLBACKSUCCESS;
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
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
	/*search label 通过测试*/
	@Override
	public List<String> search(String label)
			throws RemoteException {
		// TODO Auto-generated method stub
		List<String> mlist=new ArrayList<String>();
		try {
			ResponseEntity<List> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS+"labels.json?q="+label,List.class);
			List list= result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {
				for(int i=0;i<list.size();i++){
					Map m=(Map) list.get(i);
					m.get("label_name");
					mlist.add((String)m.get("label_name"));
				}
			}
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
		return mlist;
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
		ResponseEntity<List> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS+"users.json?q="+label,List.class);
		List list= result.getBody();
		
		
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
		List<String> mlist=new ArrayList<String>();
		User user=new User();
		try {
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS+"users/"+targetId+".json",Map.class);
			Map u= result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {				
				String id=String.valueOf(u.get("id"));
				userid=id;
				user.setId(id);
				user.setEmail((String)u.get("email"));
				user.setBirthday("birth");
				user.setImage("image");
//				u.get("password");
				user.setUsername((String)u.get("username"));
				user.setLabels((List) u.get("labels"));	
			}
			
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}		
		return user;
	}
	/*label topic history*/
	@Override
	public List<Map> topicHistory(String targetId) throws RemoteException {
		// TODO Auto-generated method stub		
		try {
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS + "topics.json?user_id="+targetId+"&page=1", Map.class);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {				
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
		try {
			ResponseEntity<Map> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS + "topics/"+topicId+ ".json", Map.class);			
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {				
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
//		HttpHeaders headers = new HttpHeaders();  
//		headers.set("x-token", token);
//		RestTemplate test=new RestTemplate();
		
		Map<String, String> parts = new HashMap<String, String>();			
		String message = null;
		int code=1;
		try {
			parts.put("body", topic);
			parts.put("label_name", label);			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "topics.json", parts, Map.class);			
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
