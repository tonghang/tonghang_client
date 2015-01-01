package com.peer.activitymain;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.FriendsAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;


public class ChatRoomListnikeActivity extends BasicActivity {
	private LinearLayout back;
	private ListView listnike_chatroom;
	private TextView title;
	private List<User> list;
	private FriendsAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_listnike);
		init();
		Intent intent=getIntent();
		topicUserTask task=new topicUserTask();
		task.execute(intent.getStringExtra("topicId"));		
	}
	private void init() {
		// TODO Auto-generated method stub		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chatroommember));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		listnike_chatroom=(ListView)findViewById(R.id.lv_listnike_chatroom);	
	}
	
	private class topicUserTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();		
			try {
				list=PeerUI.getInstance().getISessionManager().inTopicUser(params[0], callback);
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
