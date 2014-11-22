package com.peer.activitymain;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.event.NewFriensEvent;

import de.greenrobot.event.EventBus;

public class NewFriendsActivity extends BasicActivity {
	private ListView mlistview;
	private TextView title;
	private LinearLayout back;	
	private NewfriendsAdapter adapter;
	private List<String> mlist;
	private EventBus mBus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfriends);
		initdata();
		init();		
		registEventBus();
	}	
	
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.newfriends));
		
		mlistview=(ListView)findViewById(R.id.lv_newfriends);
		adapter=new NewfriendsAdapter(this,mlist);
		mlistview.setAdapter(adapter);
	}
	private void initdata() {
		// TODO Auto-generated method stub
		mlist=new ArrayList<String>();
		for(int i=0;i<10;i++){
			mlist.add("friends");
		}
	}
	private void registEventBus() {
		// TODO Auto-generated method stub
		 mBus=EventBus.getDefault();
		/*
		 * Registration: three parameters are respectively, message subscriber (receiver), receiving method name, event classes
		 */
		 mBus.register(this, "getEvent",NewFriensEvent.class);
	}
	private void getEvent(NewFriensEvent event){
		mlist.remove(event.getPosition());
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 mBus.unregister(this);
	}
}
