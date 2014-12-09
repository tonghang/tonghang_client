package com.peer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.peer.R;
import com.peer.adapter.ChatHistoryAdapter;


public class ComeMsgFragment extends Fragment {
	private ListView ListView_come;
	public static boolean isForeground = true;
	
	private ChatHistoryAdapter adapter;
	private List<EMConversation> list;
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
		list=loadConversationsWithRecentChat();		
		ListView_come=(ListView)getView().findViewById(R.id.lv_come);	
		adapter=new ChatHistoryAdapter(getActivity(),1, list);
		ListView_come.setAdapter(adapter);
	}
	/**
	 * 刷新页面
	 */
	public void refresh() {
		list.clear();
		list.addAll(loadConversationsWithRecentChat());
		adapter.notifyDataSetChanged();
	}
	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
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
}
