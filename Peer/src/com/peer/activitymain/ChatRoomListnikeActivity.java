package com.peer.activitymain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.FriendsAdapter;


public class ChatRoomListnikeActivity extends BasicActivity {
	private LinearLayout back;
	private ListView listnike_chatroom;
	private TextView title;
	private String comefrom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_listnike);
		Intent intent=getIntent();
		comefrom=intent.getStringExtra("comefrom");
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chatroommember));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		listnike_chatroom=(ListView)findViewById(R.id.lv_listnike_chatroom);
		
		FriendsAdapter adapter=new FriendsAdapter(this);
		listnike_chatroom.setAdapter(adapter);
	}
}
