package com.peer.client.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.RemoteException;

import com.peer.client.ISessionListener;
import com.peer.client.ISessionManager;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.client.easemobchatUser;
import com.peer.client.util.DataUtil;
import com.peer.client.util.DataUtilHeader;
import com.peer.constant.Constant;

public class SessionManager extends ISessionManager.Stub {
	private String token=null;
	private String huanxin_user=null;
	private String userid=null;
	private String imageURL=null;
	private String username=null;
	private int page;
	private List<String> labels=null;
	private HttpHeaders headers=new HttpHeaders();
	/*web login 通过测试*/
	@Override
	public User login(String email, String password,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		String message = null;
		int code=1;
		User user=null;
		ResponseEntity<Map> result=null;
		try {
			parts.put("email", email);
			parts.put("password", password);			
			result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "/login.json"
					,parts, Map.class);						
			Map body = result.getBody();			
			
			if (result.getStatusCode() == HttpStatus.OK) {
				if(body.get("code").equals("fail")){
					code=-1;
					message=(String)body.get("message");
				}else{
					token=(String) body.get("token");
					headers.add("x-token", token);
					code=0;
					user=new User();
					Map u=(Map) body.get("user");	
					huanxin_user=(String)u.get("huanxin_username");
					String id=String.valueOf(u.get("id"));
					userid=id;
					user.setUserid(id);
					imageURL=Constant.WEB_SERVER_ADDRESS+(String)u.get("image");
					user.setHuangxin_username(huanxin_user);
					user.setEmail(imageURL);
					user.setCity((String)u.get("city"));
					user.setBirthday((String)u.get("birth"));
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)u.get("image"));
					user.setSex((String)u.get("sex"));
					username=(String)u.get("username");
					user.setUsername(username);
					labels=(List) u.get("labels");
					user.setLabels(labels);				
					message = Constant.CALLBACKSUCCESS;
				}				
			} else {
				message = Constant.CALLBACKFAIL;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}		
		callback.onCallBack(code, message);
		return user;
	}
	/*user register acount 通过测试*/
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
			
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "/users.json"
					, parts, Map.class);							
			Map body = result.getBody();
			if (result.getStatusCode() == HttpStatus.OK) {				
				if(body.get("code")!=null&&body.get("code").equals("error")){
					message = (String) body.get("message");
				}else{
					code=0;	
					String id=String.valueOf(body.get("id"));
					userid=id;
					huanxin_user=(String) body.get("huanxin_username");
					message = Constant.CALLBACKSUCCESS;
				}				
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*register label 通过测试*/
	@Override
	public void registerLabel(List<String> labels,ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, List> parts=new HashMap<String, List>();
		int code=1;
		String message=null;
		try {
			parts.put("labels", labels);
			DataUtil.putEntity(Constant.WEB_SERVER_ADDRESS +"/users/"+userid+"/update_labels.json", parts, Map.class);			
			message=Constant.CALLBACKSUCCESS;
			code=0;
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*commite personal message 通过测试*/
	@Override
	public User profileUpdate(String nickName, String birthday, String city,
			String sex, final String filename, byte[] image, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		User user=null;
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
			if(!nickName.equals("")){
				parts.add("username", nickName);
			}	
			parts.add("city", city);
			parts.add("sex", sex);
			parts.add("image", imageFile);
			parts.add("filename", "sign.png");
			
			HttpHeaders headers=new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);					
			ResponseEntity<Map> result=DataUtilHeader.putJson(Constant.WEB_SERVER_ADDRESS + "/users/"+userid+".json",requestEntity, Map.class);			
			Map u= result.getBody();
			if (result.getStatusCode() == HttpStatus.OK){
				user=new User();
				String id=String.valueOf(u.get("id"));
				user.setUserid(id);
				user.setEmail((String)u.get("email"));
				user.setBirthday((String)u.get("birth"));
				user.setImage(Constant.WEB_SERVER_ADDRESS+(String)u.get("image"));
				user.setSex((String)u.get("sex"));
				user.setCity((String)u.get("city"));
				user.setUsername((String)u.get("username"));
			}
			code=0;
			message=Constant.CALLBACKSUCCESS;
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
		return user;
	}
	/*To add someone as a friend 通过测试*/
	@Override
	public void addFriends(String targetId, String reason,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();			
		String message = null;
		int code=1;
		try {
			parts.add("invitee_id", targetId);
			parts.add("reason", reason);				
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);			
			
			RestTemplate restTemplate=new RestTemplate(true);				
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());						
			Map result=restTemplate.postForObject(Constant.WEB_SERVER_ADDRESS + "/invitations.json", requestEntity, Map.class);
			message=Constant.CALLBACKSUCCESS;
			code=0;
			result.get("last_insert_rowid()");
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*search label 通过测试*/
	@Override
	public List<String> search(String label,ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List<String> mlist=new ArrayList<String>();
		String message = null;
		int code=1;	
		try {
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/labels.json?q="+label, 
					HttpMethod.GET, 
					new HttpEntity(headers), 
					List.class);			
			List list= result.getBody();					
			if (result.getStatusCode() == HttpStatus.OK) {
				for(int i=0;i<list.size();i++){
					Map m=(Map) list.get(i);
					m.get("label_name");
					mlist.add((String)m.get("label_name"));
				}
				code=0;
				message=Constant.CALLBACKSUCCESS;
			}else{
				message=Constant.CALLBACKFAIL;
			}
		} catch (Exception e) {
			// TODO log exception
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
		return mlist;
	}
	/*refuse someone add as friend*/
	@Override
	public void refuseAddFriends(String sourceId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		String message=null;
		int code=1;
		try {
//			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "/invitations/"+sourceId+"/refuse.json", "", Map.class);			
			ResponseEntity<Map> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/invitations/"+sourceId+"/refuse.json", 
					HttpMethod.POST, 
					new HttpEntity(headers), Map.class);
			
			Map body = result.getBody();
			message=Constant.CALLBACKSUCCESS;
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*aggree someone add as friend*/
	@Override
	public void agreeAddFriends(String sourceId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		String message=null;
		int code=1;
		try {
//			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS + "/invitations/"+sourceId+"/agree.json", "", Map.class);			
			ResponseEntity<Map> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/invitations/"+sourceId+"/agree.json", 
					HttpMethod.POST, 
					new HttpEntity(headers), 
					Map.class);
			Map body = result.getBody();
			message=Constant.CALLBACKSUCCESS;
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}	
		callback.onCallBack(code, message);
	}
	/*search topic by label 通过测试*/
	@Override
	public List<Topic> searchTopicByLabel(String label,int page,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		List<Topic> mlist=new ArrayList<Topic>();
		String message = null;
		int code=1;
		try {
//			ResponseEntity<List> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS+"/topics.json?label_name="+label+"&page="+page,List.class);
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/topics.json?label_name="+label+"&page="+page, 
					HttpMethod.GET, 
					new HttpEntity(headers), List.class);
			List<Map> list= result.getBody();
			if(result.getStatusCode()==HttpStatus.OK){
				for(int i=0;i<list.size();i++){
					Topic topic=new Topic();
					Map usermap=(Map) list.get(i).get("user");
					User user=new User();
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)usermap.get("image"));
					user.setUserid(String.valueOf(usermap.get("id")));
					user.setUsername((String)usermap.get("username"));
					user.setLabels((List<String>)usermap.get("labels"));
					user.setHuangxin_username((String)usermap.get("huanxin_username"));
					
					topic.setUser(user);					
					topic.setLabel_name((String)list.get(i).get("label_name"));
					topic.setSubject((String)list.get(i).get("subject"));
					topic.setTopicid(String.valueOf(list.get(i).get("id")));
					topic.setHuangxin_group_id(String.valueOf(list.get(i).get("huanxin_group_id")));
					topic.setCreate_time((String)list.get(i).get("created_at"));
					topic.setBody((String)list.get(i).get("body"));
					mlist.add(topic);
				}
				
				code=0;
				message=Constant.CALLBACKSUCCESS;
			}else{
				message=Constant.CALLBACKFAIL;
			}
		}catch(Exception e){
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);	
		return mlist;
	}
	@Override
	public List<Topic> searchTopicBykey(String key,int page, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List<Topic> mlist=new ArrayList<Topic>();
		String message = null;
		int code=1;
		try {
//			ResponseEntity<List> result =DataUtil.getJson(Constant.WEB_SERVER_ADDRESS+"/topics.json?q="+key+"&page="+page,List.class);
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/topics.json?q="+key+"&page="+page, HttpMethod.GET, 
					new HttpEntity(headers), List.class);
			List<Map> list= result.getBody();
			if(result.getStatusCode()==HttpStatus.OK){
				for(int i=0;i<list.size();i++){
					Topic topic=new Topic();
					Map usermap=(Map) list.get(i).get("user");
					User user=new User();
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)usermap.get("image"));
					user.setUserid(String.valueOf(usermap.get("id")));
					user.setUsername((String)usermap.get("username"));
					user.setLabels((List<String>)usermap.get("labels"));
					user.setHuangxin_username((String)usermap.get("huanxin_username"));
					topic.setUser(user);
					topic.setLabel_name((String)list.get(i).get("label_name"));
					topic.setSubject((String)list.get(i).get("subject"));
					topic.setTopicid(String.valueOf(list.get(i).get("id")));
					topic.setHuangxin_group_id(String.valueOf(list.get(i).get("huanxin_group_id")));
					
					String created_at=(String)list.get(i).get("created_at");
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
					Date date=sdf.parse(created_at);
					SimpleDateFormat timeminu = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String time=timeminu.format(date);
					
					topic.setCreate_time(time);
					topic.setBody((String)list.get(i).get("body"));
					mlist.add(topic);
				}
				
				code=0;
				message=Constant.CALLBACKSUCCESS;
			}else{
				message=Constant.CALLBACKFAIL;
			}
		}catch(Exception e){
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);	
		return mlist;
	}	
	@Override
	public List<User> searchUsersByLabel(String labename, int page,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		List<User> mlist=new ArrayList<User>();
		String message = null;
		int code=1;
		try{
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/users.json?label_name="+labename+"&page="+page,
					HttpMethod.GET, 
					new HttpEntity(headers),
					List.class);
			List<Map> list= result.getBody();			
			List<User> friendslist=myFriends();
			if(result.getStatusCode()==HttpStatus.OK){
				for(Map m:list){
					User user=new User();
					user.setEmail((String)m.get("email"));
					user.setUsername((String)m.get("username"));
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
					user.setUserid(String.valueOf(m.get("id")));
					user.setBirthday((String)m.get("birth"));					
					user.setLabels((List)m.get("labels"));
					user.setCity((String)m.get("city"));
					user.setSex((String)m.get("sex"));
					user.setHuangxin_username((String)m.get("huanxin_username"));
					for(User u:friendslist){
						if(u.getUserid().equals(String.valueOf(m.get("id")))){
							user.setIs_friends(true);
							break;
						}else{
							user.setIs_friends(false);
						}						
					}	
					mlist.add(user);
				}
				code=0;
				message=Constant.CALLBACKSUCCESS;
			}else{
				message=Constant.CALLBACKFAIL;
			}
		}catch(Exception e){
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
		return mlist;
	}
	
	/*search user by nike 通过测试*/
	@Override
	public List<User> searchUsersByNickName(String username,int page,ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List<User> mlist=new ArrayList<User>();
		String message = null;
		int code=1;
		try{
			//Constant.WEB_SERVER_ADDRESS+"/users.json?label_name="+username+"&page="+page
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/users.json?q="+username+"&page="+page,
					HttpMethod.GET, 
					new HttpEntity(headers),
					List.class);
			List<Map> list= result.getBody();			
			List<User> friendslist=myFriends();
			if(result.getStatusCode()==HttpStatus.OK){
				for(Map m:list){
					User user=new User();
					user.setEmail((String)m.get("email"));
					user.setUsername((String)m.get("username"));
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
					user.setUserid(String.valueOf(m.get("id")));
					user.setBirthday((String)m.get("birth"));
					user.setLabels((List)m.get("labels"));
					user.setCity((String)m.get("city"));
					user.setSex((String)m.get("sex"));
					user.setHuangxin_username((String)m.get("huanxin_username"));
					for(User u:friendslist){
						if(u.getUserid().equals(String.valueOf(m.get("id")))){
							user.setIs_friends(true);
							break;
						}else{
							user.setIs_friends(false);
						}						
					}	
					mlist.add(user);
				}
				code=0;
				message=Constant.CALLBACKSUCCESS;
			}else{
				message=Constant.CALLBACKFAIL;
			}
		}catch(Exception e){
			message = Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
		return mlist;
	}
	/*get all friends */
	@Override
	public List<User> myFriends() throws RemoteException {
		// TODO Auto-generated method stub
		List<User> list=new ArrayList<User>();			
		List result=null;		
		try {					
			ResponseEntity<List> response =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/friends.json",  
				      HttpMethod.GET,new HttpEntity(headers),List.class);			  		
			if(response.getStatusCode()==HttpStatus.OK){
				result=response.getBody();
				for(int i=0;i<result.size();i++){
					User u=new User();
					Map m=(Map) result.get(i);
					u.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
					u.setIs_friends(true);
					u.setLabels((List)m.get("labels"));
					u.setUsername((String)m.get("username"));
					u.setEmail((String)m.get("email"));
					u.setHuangxin_username((String)m.get("huanxin_username"));
					u.setUserid(String.valueOf(m.get("id")));
					u.setCity((String)m.get("city"));
					u.setBirthday((String)m.get("birth"));
					u.setSex((String)m.get("sex"));
					list.add(u);
				}							
			}
					
		} catch (Exception e) {
			// TODO log exception			
			e.printStackTrace();
		}
		return list;
	}
	
	/*All want to add me as a friend 好友请求列表*/
	@Override
	public List<User> Invitations(ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		List<User> list=null;	
		String message=null;
		int code=1;
		try {		
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/users/"+userid+"/invitations.json", 
					HttpMethod.GET, 
					new HttpEntity(headers),List.class);
			if(result.getStatusCode()==HttpStatus.OK){
				message=Constant.CALLBACKSUCCESS;
				code=0;
				list=new ArrayList<User>();
				List resultlist=result.getBody();
				for(int i=0;i<resultlist.size();i++){
					User u=new User();
					Map m=(Map) resultlist.get(i);
					if(((String)m.get("status")).equals(Constant.PENDING)){
						Map inviter=(Map)m.get("inviter");						
						u.setImage(Constant.WEB_SERVER_ADDRESS+(String)inviter.get("image"));
						u.setUsername((String)inviter.get("username"));
						u.setUserid(String.valueOf(inviter.get("id")));
						u.setReason((String)m.get("reason"));
						u.setInvitionid(String.valueOf(m.get("id")));
						u.setIs_friends(false);
						u.setHuangxin_username((String)m.get("huanxin_username"));
						u.setCity((String)m.get("city"));
						u.setBirthday((String)m.get("birth"));
						u.setSex((String)m.get("sex"));					
						list.add(u);
					}					
				}
				
			}		
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}	
		callback.onCallBack(code, message);
		return list;
	}
	/*someone personal page 通过测试*/
	@Override
	public User personalPage(String targetId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		List<String> mlist=new ArrayList<String>();
		User user=null;
		try {
			ResponseEntity<Map> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/users/"+targetId+".json", 
					HttpMethod.GET, 
					new HttpEntity(headers), 
					Map.class);
			
			Map u= result.getBody();
			if (result.getStatusCode() == HttpStatus.OK &&result.getBody()!=null) {	
				user=new User();
				String id=String.valueOf(u.get("id"));
				user.setUserid(id);
				user.setEmail((String)u.get("email"));
				user.setBirthday((String)u.get("birth"));
				user.setImage(Constant.WEB_SERVER_ADDRESS+(String)u.get("image"));
				user.setSex((String)u.get("sex"));
				user.setCity((String)u.get("city"));
				user.setUsername((String)u.get("username"));
				user.setLabels((List) u.get("labels"));	
			}
			
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}		
		return user;
	}
	/*label topic history 测试通过*/
	@Override
	public List<Topic> topicHistory(String targetId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub				
		List<Topic>  topiclist=new ArrayList<Topic>();
		String message=null;
		int code=1;
		try {
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/topics.json?user_id="+targetId+"&page=1", 
					HttpMethod.GET, 
					new HttpEntity(headers), 
					List.class);
			if (result.getStatusCode() == HttpStatus.OK) {	
				code=0;
				List<Map>	body = result.getBody();
				for(int i=0;i<body.size();i++){
					Topic topic=new Topic();
					
					Map usermap=(Map) body.get(i).get("user");
					
					User user=new User();
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)usermap.get("image"));
					user.setUsername((String)usermap.get("username"));
					user.setUserid(String.valueOf(usermap.get("id")));
					user.setHuangxin_username((String)usermap.get("huanxin_username"));
					topic.setUser(user);
					
					topic.setCreate_time((String)body.get(i).get("created_at"));
					topic.setLabel_name((String)body.get(i).get("label_name"));
					topic.setSubject((String)body.get(i).get("subject"));
					topic.setTopicid(String.valueOf(body.get(i).get("id")));
					topic.setHuangxin_group_id(String.valueOf(body.get(i).get("huanxin_group_id")));
					topiclist.add(topic);
				}			
				message=Constant.CALLBACKSUCCESS;
			} else{
				message=Constant.CALLBACKFAIL;
			}
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}	
		callback.onCallBack(code, message);
		
		return topiclist;
	}
	/*label topic chat history*/
	@Override
	public void TopicchatHistory(String topicId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			ResponseEntity<Map> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS + "/topics/"+topicId+ ".json",
					HttpMethod.GET, 
					new HttpEntity(headers), 
					Map.class);
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
	public void forgetPassword(String tagetId,ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> parts = new HashMap<String, String>();
		parts.put("email", tagetId);
		String message=null;
		int code=1;
		try {
			ResponseEntity<Map> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS+"/forget_password.json" ,parts, Map.class);			
			message=Constant.CALLBACKSUCCESS;
			code=0;
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	/*update password 通过测试*/
	@Override
	public void updatePassword(String newPassword , ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		String message=null;
		int code=1;
		try{
			parts.add("password", newPassword);
			HttpHeaders headers=new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);			
			ResponseEntity<Map> response=DataUtilHeader.putJson(Constant.WEB_SERVER_ADDRESS + "/users/"+userid+".json", requestEntity, Map.class);
			message=Constant.CALLBACKSUCCESS;
			code=0;
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);					
	}
	
	/*creat topic  测试通过*/
	@Override
	public Topic creatTopic(String label, String topiccontent)
			throws RemoteException {
		// TODO Auto-generated method stub		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();			
		Map result=null;
		Topic topic=new Topic();
		try {
			parts.add("subject", topiccontent);
			parts.add("label_name", label);			
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);
			RestTemplate restTemplate=new RestTemplate(true);	
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());			
			
			result= restTemplate.postForObject(Constant.WEB_SERVER_ADDRESS + "/topics.json", requestEntity, Map.class);
			topic.setHuangxin_group_id((String)result.get("huanxin_group_id"));
			topic.setTopicid(String.valueOf(result.get("id")));
			topic.setLabel_name((String)result.get("label_name"));
			topic.setSubject((String)result.get("subject"));
		} catch (Exception e) {
			// TODO log exception
			e.printStackTrace();
		}
		return topic;
	}
	/*用户首页推荐 通过测试*/
	@Override
	public List recommend(int page, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		this.page=page;
		List mlist=new ArrayList();
		String message=null;
		int code=1;
		try{					
			ResponseEntity<List> result =DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/recommendations.json?page="+page,
					HttpMethod.GET,new HttpEntity(headers), List.class);
			if(result.getStatusCode()==HttpStatus.OK){
				List<Map> recomend=result.getBody();				
				for(int i=0;i<recomend.size();i++){
					if(recomend.get(i).get("type").equals(Constant.USER)){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("type", Constant.USER);
						User user=new User();
						user.setImage(Constant.WEB_SERVER_ADDRESS+(String)recomend.get(i).get("image"));
						user.setUserid(String.valueOf(recomend.get(i).get("id")));
						user.setUsername((String)recomend.get(i).get("username"));					
						user.setLabels((List<String>)recomend.get(i).get("labels"));
						user.setHuangxin_username(String.valueOf(recomend.get(i).get("huanxin_username")));
						user.setIs_friends((Boolean)recomend.get(i).get("is_friend"));
						user.setSex((String)recomend.get(i).get("sex"));
						user.setCity((String)recomend.get(i).get("city"));
						user.setBirthday((String)recomend.get(i).get("birth"));
						user.setEmail((String)recomend.get(i).get("email"));
						map.put(Constant.USER, user);
						mlist.add(map);
					}else if(recomend.get(i).get("type").equals(Constant.TOPIC)){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("type", Constant.TOPIC);
						//这里的m 没有is_friend键 是否为好友需要自己判断
						Map m=(Map)recomend.get(i).get("user");						
						User user=new User();
//						user.setIs_friends((Boolean)recomend.get(i).get("is_friend"));						
						user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
						user.setUserid(String.valueOf(m.get("id")));
						user.setUsername((String)m.get("username"));					
						user.setLabels((List<String>)m.get("labels"));
						user.setHuangxin_username((String)m.get("huanxin_username"));
						user.setSex((String)m.get("sex"));
						user.setCity((String)m.get("city"));
						user.setBirthday((String)m.get("birth"));						
						map.put(Constant.USER, user);
						
						Topic topic=new Topic();
						String created_at=(String)recomend.get(i).get("created_at");
						Date d = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
						Date date=sdf.parse(created_at);
						SimpleDateFormat timeminu = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String time=timeminu.format(date);
					
						topic.setLabel_name((String)recomend.get(i).get("label_name"));
						topic.setCreate_time(time);
						topic.setHuangxin_group_id(String.valueOf(recomend.get(i).get("huanxin_group_id")));
						topic.setBody((String)recomend.get(i).get("body"));
						topic.setSubject((String)recomend.get(i).get("subject"));
						topic.setTopicid(String.valueOf(recomend.get(i).get("id")));
						map.put(Constant.TOPIC, topic);
						mlist.add(map);
					}					
				}	
				message=Constant.CALLBACKSUCCESS;	
				code=0;
			}else
				message=Constant.CALLBACKFAIL;
			
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();			
		}
		callback.onCallBack(code, message);
		return mlist;
	}
	@Override
	public List<User> inTopicUser(String topicid, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List<User> list=null;	
		String message=null;
		int code=1;
		try {		
			ResponseEntity<List> result =DataUtil.getJson(
					Constant.WEB_SERVER_ADDRESS + "/users.json?topic_id="+topicid, List.class);
			if(result.getStatusCode()==HttpStatus.OK){
				message=Constant.CALLBACKSUCCESS;
				code=0;
				list=new ArrayList<User>();
				List resultlist=result.getBody();
				List<User> friendslist=myFriends();
				for(int i=0;i<resultlist.size();i++){
					User user=new User();
					Map m=(Map) resultlist.get(i);
					user.setUserid(String.valueOf(m.get("id")));
					for(User u:friendslist){
						if(u.getUserid().equals(String.valueOf(m.get("id")))){
							user.setIs_friends(true);
							break;
						}else{
							user.setIs_friends(false);
						}						
					}
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
					user.setLabels((List)m.get("labels"));
					user.setUsername((String)m.get("username"));
					user.setEmail((String)m.get("email"));
					list.add(user);
				}							
			}
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);		
		return list;
	}
	@Override
	public List convertToUser(easemobchatUser users, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List mlist=new ArrayList();
		Map<String, List> parts=new HashMap<String, List>();
		String message=null;
		int code=1;
		try{
			parts.put("entries", users.getEasemobchatusers());
			ResponseEntity<List> result =DataUtil.postEntity(Constant.WEB_SERVER_ADDRESS+"/huanxin/hid2sids.json", parts,  List.class);
			if(result.getStatusCode()==HttpStatus.OK){
				message=Constant.CALLBACKSUCCESS;
				code=0;
				List<Map> recomend=result.getBody();				
				for(int i=0;i<recomend.size();i++){
					if(recomend.get(i).get("type").equals(Constant.USER)){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("type", Constant.USER);
						User user=new User();
						user.setImage(Constant.WEB_SERVER_ADDRESS+(String)recomend.get(i).get("image"));
						user.setUserid(String.valueOf(recomend.get(i).get("id")));
						user.setUsername((String)recomend.get(i).get("username"));					
						user.setLabels((List<String>)recomend.get(i).get("labels"));
						user.setHuangxin_username((String)recomend.get(i).get("huanxin_username"));
						user.setSex((String)recomend.get(i).get("sex"));
						user.setCity((String)recomend.get(i).get("city"));
						user.setBirthday((String)recomend.get(i).get("birth"));
						//这里 没有is_friend键 是否为好友需要自己判断
						//user.setIs_friends((Boolean)recomend.get(i).get("is_friend"));
						user.setEmail((String)recomend.get(i).get("email"));
						map.put(Constant.USER, user);
						mlist.add(map);
					}else if(recomend.get(i).get("type").equals(Constant.TOPIC)){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("type", Constant.TOPIC);
						
						Map m=(Map)recomend.get(i).get("user");						
						User user=new User();
						user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
						user.setUserid(String.valueOf(m.get("id")));
						user.setUsername((String)m.get("username"));					
						user.setLabels((List<String>)m.get("labels"));
						user.setHuangxin_username((String)m.get("huanxin_username"));
						map.put(Constant.USER, user);
						
						Topic topic=new Topic();
						String created_at=(String)recomend.get(i).get("created_at");
						Date d = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
						Date date=sdf.parse(created_at);
						SimpleDateFormat timeminu = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String time=timeminu.format(date);
						
						topic.setLabel_name((String)recomend.get(i).get("label_name"));
						topic.setCreate_time((String)recomend.get(i).get("time"));
						topic.setHuangxin_group_id(String.valueOf(recomend.get(i).get("huanxin_group_id")));
						topic.setBody((String)recomend.get(i).get("body"));
						topic.setSubject((String)recomend.get(i).get("subject"));
						topic.setTopicid(String.valueOf(recomend.get(i).get("id")));
						map.put(Constant.TOPIC, topic);
						mlist.add(map);
					}						
				}	
			}
		
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}		
		callback.onCallBack(code, message);		
		return mlist;
	}
	@Override
	public void feedback(String content, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();			
		String message = null;
		int code=1;
		Map result=null;
		String id=null;
		try {	
			parts.add("content", content);					
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);			
			RestTemplate restTemplate=new RestTemplate(true);	
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());			
			
			result= restTemplate.postForObject(Constant.WEB_SERVER_ADDRESS + "/feedbacks.json", requestEntity, Map.class);
			
			id=String.valueOf(result.get("id"));	
			message=Constant.CALLBACKSUCCESS;
			code=0;
		} catch (Exception e) {
			// TODO log exception
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	@Override
	public List TopicReplies(String topicId, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		List list=null;
		String message=null;
		int code=1;	
		Map map=null;
		try{
			ResponseEntity<Map> result=DataUtil.templateExchange(Constant.WEB_SERVER_ADDRESS+"/topics/"+topicId+".json", 
					HttpMethod.GET, 
					new HttpEntity(headers),Map.class);
			if(result.getStatusCode()==HttpStatus.OK){
				list=new ArrayList();
				message=Constant.CALLBACKSUCCESS;
				code=0;
				map=result.getBody();
				List<Map> mlist=(List) map.get("replies");
				for(int i=0;i<mlist.size();i++){
					Map<String, Object> replies=new HashMap<String, Object>();
					
					Map usermap=(Map) mlist.get(i).get("user");
					User user=new User();
					user.setUserid(String.valueOf(usermap.get("id")));
					user.setUsername((String)usermap.get("username"));
					user.setImage(Constant.WEB_SERVER_ADDRESS+(String)usermap.get("image"));
					replies.put(Constant.USER, user);
					String created_at=(String)mlist.get(i).get("created_at");
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
					Date date=sdf.parse(created_at);
					SimpleDateFormat timeminu = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String time=timeminu.format(date);
					
					replies.put(Constant.TIME, time);
					replies.put("replybody", (String)mlist.get(i).get("body"));
					list.add(replies);
				}
			}
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();			
		}
		callback.onCallBack(code, message);
		
		return list;
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
	@Override
	public List<String> getLabels() throws RemoteException {
		// TODO Auto-generated method stub
		return labels;
	}
	@Override
	public void setLabels(List<String> labels) throws RemoteException {
		// TODO Auto-generated method stub
		this.labels=labels;
	}
	@Override
	public String getImagUrL() throws RemoteException {
		// TODO Auto-generated method stub
		return imageURL;
	}
	@Override
	public String getUserName() throws RemoteException {
		// TODO Auto-generated method stub
		return username;
	}
	
	@Override
	public void deleteFriend(String targetId, ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		String message = null;
		int code=1;
		try {								
			RestTemplate restTemplate=new RestTemplate(true);				
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());					
			
			ResponseEntity<Map> response = restTemplate.exchange(Constant.WEB_SERVER_ADDRESS + "/friends/"+targetId+".json",  
				      HttpMethod.DELETE,  
				      new HttpEntity(headers),  
				      Map.class); 	
			
			message=Constant.CALLBACKSUCCESS;
			code=0;
			
		}catch(Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	@Override
	public void replyTopic(String topicId, String content,
			ISessionListener callback) throws RemoteException {
		// TODO Auto-generated method stub
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();						
		String message = null;
		int code=1;
		String id=null;
		try {	
			parts.add("body", content);		
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity=
					new HttpEntity<MultiValueMap<String,Object>>(parts,headers);
			RestTemplate restTemplate=new RestTemplate(true);	
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());			
			
			ResponseEntity<Map> response =restTemplate.postForEntity(Constant.WEB_SERVER_ADDRESS+"/topics/"+topicId+"/replies.json", 
					requestEntity, Map.class, parts);
			if(response.getStatusCode()==HttpStatus.OK){
				message=Constant.CALLBACKSUCCESS;
				code=0;
				response.getBody();
			}
		}catch (Exception e){
			message=Constant.CALLBACKFAIL;
			e.printStackTrace();
		}
		callback.onCallBack(code, message);
	}
	@Override
	public List recommendByPage( ISessionListener callback)
			throws RemoteException {
		// TODO Auto-generated method stub
		this.page=page+1;
		return recommend(page, callback);
	}
	
	
}
