package com.peer.activitymain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.SkillAdapter;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.PersonpageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonalPageActivity extends BasicActivity {
	private ImageView personhead;
	private TextView nikename,title,topic_whose;
	private RelativeLayout topic_click;
	private LinearLayout back,bottomline;
	private ListView skillllist;
	private List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage);
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		topic_whose=(TextView)findViewById(R.id.tv_topic);
		title=(TextView)findViewById(R.id.tv_title);
		topic_click=(RelativeLayout)findViewById(R.id.rl_topic);
		topic_click.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		nikename=(TextView)findViewById(R.id.personnike);
		personhead=(ImageView)findViewById(R.id.personhead);
		bottomline=(LinearLayout)findViewById(R.id.ll_personpagebottom);
		skillllist=(ListView)findViewById(R.id.lv_pageskill);
		SkillAdapter adapter=new SkillAdapter(this,"page");
		skillllist.setAdapter(adapter);		
		ViewType();		
	}
	private void ViewType() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight=1;
		switch (PersonpageUtil.getInstance().getPersonpagetype()) {
		case Constant.UNFRIENDSPAGE:
			topic_whose.setText(getResources().getString(R.string.topic_other));
			title.setText(getResources().getString(R.string.personalpage_other));
			params.rightMargin=(int) getResources().getDimension(R.dimen.marginsize_around);
			params.leftMargin=(int) getResources().getDimension(R.dimen.marginsize_around);
			Button bt=new Button(this);
			bt.setText(getResources().getString(R.string.sendmsg));
			bt.setBackgroundResource(R.drawable.selector_commit);	
			bt.setLayoutParams(params);
			bt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
					Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
					startActivity(intent);
				}
			});
			
			Button bt2=new Button(this);
			bt2.setText(getResources().getString(R.string.addfriends));
			bt2.setBackgroundResource(R.drawable.selector_commit);		
			bt2.setLayoutParams(params);
			bottomline.addView(bt);
			bottomline.addView(bt2);
			bt2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub					
					Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
					startActivity(intent);
				}
			});
			break;
		case Constant.FRIENDSPAGE:
			topic_whose.setText(getResources().getString(R.string.topic_other));
			params.rightMargin=(int) getResources().getDimension(R.dimen.marginsize_around);
			params.leftMargin=(int) getResources().getDimension(R.dimen.marginsize_around);
			title.setText(getResources().getString(R.string.personalpage_other));
			Button bt3=new Button(this);
			bt3.setText(getResources().getString(R.string.sendmsg));
			bt3.setBackgroundResource(R.drawable.selector_commit);
			bt3.setLayoutParams(params);
			bottomline.addView(bt3);
			bt3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
					Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
					startActivity(intent);
				}
			});
			break;
		case Constant.OWNPAGE:
			topic_whose.setText(getResources().getString(R.string.topic_owen));
			title.setText(getResources().getString(R.string.personalpage_own));
			bottomline.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}		
				
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_topic:
			Intent intent=new Intent(PersonalPageActivity.this,TopicActivity.class);
			startActivity(intent);			
			break;

		default:
			break;
		}
	}
}
