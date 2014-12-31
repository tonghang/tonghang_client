package com.peer.IMimplements;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.peer.IMinterface.IM;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;


/**
 * Encapsulation ring letter implementation method
 * 封装环信通信
 * */
public class RingLetterImp implements IM{
	private static RingLetterImp ringletter=null;
	private RingLetterImp(){
		
	}
	public static RingLetterImp getInstance(){
		if(ringletter==null){
			synchronized (RingLetterImp.class) {
				if(ringletter==null){
					ringletter=new RingLetterImp();
				}
			 }
		}		 
		return ringletter;
	}

	@Override
	public void login(String email, String password) {
		// TODO Auto-generated method stub
		EMChatManager.getInstance().login(email,password,new EMCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				 System.out.println("环信登录成功");
				
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("环信登录错误---->");
			}
		});
	}

	@Override
	public boolean register(String email, String password, String username) {
		// TODO Auto-generated method stub
		try {
			EMChatManager.getInstance().createAccountOnServer(email, password);
			System.out.println("注册成功");
			return true;
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block			
			int errorCode=e.getErrorCode();
			if(errorCode==EMError.NONETWORK_ERROR){
				System.out.println("网络异常，请检查网络！");				
			}else if(errorCode==EMError.USER_ALREADY_EXISTS){				
				System.out.println("用户已存在！");				
			}else if(errorCode==EMError.UNAUTHORIZED){				
				System.out.println("注册失败，无权限！");				
			}else{
				System.out.println("注册失败: " + e.getMessage());
			}			
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public void sendMessage(String content, int chattype,String targetId,String imageUrl) {
		// TODO Auto-generated method stub
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 如果是群聊，设置chattype,默认是单聊
		if (chattype == Constant.MULTICHAT)
			message.setChatType(ChatType.GroupChat);
		TextMessageBody txtBody = new TextMessageBody(content);
		// 设置消息body
		message.addBody(txtBody);
		//自定义扩展消息，用于头像
		message.setAttribute(Constant.IMAGEURL, imageUrl);
		
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(targetId);		
		try {
		    //发送消息
			EMChatManager.getInstance().sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setAppInited() {
		// TODO Auto-generated method stub
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}
	@Override
	public void loadConversationsandGroups() {
		// TODO Auto-generated method stub
		EMGroupManager.getInstance().loadAllGroups();
		EMChatManager.getInstance().loadAllConversations();
	}
	@Override
	public void clearConversation(String targetname) {
		// TODO Auto-generated method stub
		EMChatManager.getInstance().clearConversation(targetname);
	}
	@Override
	public int getUnreadMesTotal() {
		// TODO Auto-generated method stub
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}
	@Override
	public void joingroup(String groupid) {
		// TODO Auto-generated method stub
		try {
			EMGroupManager.getInstance().joinGroup(groupid);
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
