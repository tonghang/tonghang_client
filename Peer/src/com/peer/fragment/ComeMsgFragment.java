package com.peer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.peer.R;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.client.easemobchatUser;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;


public class ComeMsgFragment extends BasicFragment {
	private ListView ListView_come;
	private boolean hidden;
	private List<EMGroup> groups;
	private ChatHistoryAdapter adapter;
	private List<EMConversation> list;
	private List<Map> easemobchatusers=new ArrayList<Map>();
	
	private String isnumber = "^\\d+$";//正则用来匹配纯数字
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_comemsg, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();	
	}
	private void init() {
		// TODO Auto-generated method stub	
//		groups = EMGroupManager.getInstance().getAllGroups();
		list=loadConversationsWithRecentChat();		
		ListView_come=(ListView)getView().findViewById(R.id.lv_come);	
		for(EMConversation em:loadConversationsWithRecentChat()){
			Map m=new HashMap<String, Object>();
			m.put("username", em.getUserName());
			/*环信的群组ID为纯数字，用正则匹配来判断是不是群组*/
			m.put("is_group", em.getUserName().matches(isnumber));			
			easemobchatusers.add(m);
		}
		easemobchatUser users=new easemobchatUser();
		users.setEasemobchatusers(easemobchatusers);
		comeMsgTask task=new comeMsgTask();
		task.execute(users);		
	}
	public void refresh() {
		if(list!=null){
			list.clear();
		}				
		list.addAll(loadConversationsWithRecentChat());
		easemobchatusers.clear();
		for(EMConversation em:loadConversationsWithRecentChat()){
			Map m=new HashMap<String, Object>();
			m.put("username", em.getUserName());
			/*环信的群组ID为纯数字，用正则匹配来判断是不是群组*/
			m.put("is_group", em.getUserName().matches(isnumber));
			easemobchatusers.add(m);
		}
		easemobchatUser users=new easemobchatUser();
		users.setEasemobchatusers(easemobchatusers);
		comeMsgTask task=new comeMsgTask();
		task.execute(users);		
	}
	
	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().
				getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}
	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}
	private class comeMsgTask extends AsyncTask<easemobchatUser, easemobchatUser, List>{

		@Override
		protected List doInBackground(easemobchatUser... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			List mlist=null;
			try {
				mlist=PeerUI.getInstance().getISessionManager().convertToUser(params[0], callback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return mlist;
		}
		@Override
		protected void onPostExecute(List result) {
			// TODO Auto-generated method stub
			adapter=new ChatHistoryAdapter(getActivity(), result);
//			adapter.setUsermsg(result);			
			ListView_come.setAdapter(adapter);
		}
		
		
	}
}
