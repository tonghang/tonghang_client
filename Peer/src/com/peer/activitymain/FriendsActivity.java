package com.peer.activitymain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.FriendsAdapter;

public class FriendsActivity extends BasicActivity {
	private LinearLayout find,come,my,friends;
	private ListView mlistview;
	private RelativeLayout seenewfriends;
	private ImageView friendsback;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_friends);
	init();
}
private void init() {
	// TODO Auto-generated method stub
	friendsback=(ImageView)findViewById(R.id.iv_backfriends);
	friendsback.setImageResource(R.drawable.find_label_press);
	
	
	mlistview=(ListView)findViewById(R.id.lv_friends);
	
	FriendsAdapter adapter=new FriendsAdapter(this);
	mlistview.setAdapter(adapter);

	find=(LinearLayout)findViewById(R.id.ll_find);		
	come=(LinearLayout)findViewById(R.id.ll_come);
	my=(LinearLayout)findViewById(R.id.ll_my);
	friends=(LinearLayout)findViewById(R.id.ll_friends);
	seenewfriends=(RelativeLayout)findViewById(R.id.rl_newfriends);
	seenewfriends.setOnClickListener(this);
	
	find.setOnClickListener(this);
	come.setOnClickListener(this);
	my.setOnClickListener(this);	
	
}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	super.onClick(v);
	switch (v.getId()) {
	case R.id.rl_newfriends:
		Intent intent=new Intent(FriendsActivity.this,NewFriendsActivity.class);
		startActivity(intent);		
		break;

	default:
		break;
	}
}
}
