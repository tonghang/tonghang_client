package com.peer.activitymain;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.NewfriendsAdapter;

public class NewFriendsActivity extends BasicActivity {
	private ListView mlistview;
	private TextView title;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfriends);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.newfriends));
		
		mlistview=(ListView)findViewById(R.id.lv_newfriends);
		NewfriendsAdapter adapter=new NewfriendsAdapter(this);
		mlistview.setAdapter(adapter);
	}

}
