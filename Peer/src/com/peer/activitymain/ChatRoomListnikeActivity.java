package com.peer.activitymain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.FriendsAdapter;
import com.peer.client.User;
import com.peer.client.easemobchatUser;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;


public class ChatRoomListnikeActivity extends BasicActivity {
	private LinearLayout back;
	private ListView listnike_chatroom;
	private TextView title;
	private List<User> list=new ArrayList<User>();
	private FriendsAdapter adapter;
	private List<Map> easemobchatusers=new ArrayList<Map>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_listnike);
		init();
		Intent intent=getIntent();
		EMGroup group=null;
		try {
			group=EMGroupManager.getInstance().getGroupFromServer(intent.getStringExtra("groupId"));
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> list=group.getMembers();
		for(int i=0;i<list.size();i++){
			Map m=new HashMap<String, Object>();
			m.put("username", list.get(i));
			m.put("is_group",false);			
			easemobchatusers.add(m);
		}
		easemobchatUser users=new easemobchatUser();
		users.setEasemobchatusers(easemobchatusers);		
		topicUserTask task=new topicUserTask();
		task.execute(users);
				
	}
	private void init() {
		// TODO Auto-generated method stub		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chatroommember));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		listnike_chatroom=(ListView)findViewById(R.id.lv_listnike_chatroom);	
	}
	
	private class topicUserTask extends AsyncTask<easemobchatUser, easemobchatUser, String>{

		@Override
		protected String doInBackground(easemobchatUser... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();	
			List<Map> resultlist=null;
			try {
				resultlist=PeerUI.getInstance().getISessionManager().convertToUser(params[0], callback);
				for(int i=0;i<resultlist.size();i++){
					Map map=resultlist.get(i);
					if(map.get("type").equals(Constant.USER)){
						User user=(User) map.get(Constant.USER);
						list.add(user);
					}					
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equals(Constant.CALLBACKSUCCESS)){
				adapter=new FriendsAdapter(ChatRoomListnikeActivity.this,list);
				listnike_chatroom.setAdapter(adapter);
			}
		}
	}
}
