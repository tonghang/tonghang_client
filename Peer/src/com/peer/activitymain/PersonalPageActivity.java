package com.peer.activitymain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.SkillAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Intent;
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
	private ImageView personhead,delete;
	private TextView nikename,title,topic_whose,acount,city;
	private RelativeLayout topic_click;
	private LinearLayout back,bottomline;
	private ListView skillllist;
	private List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	
	private String image;
	private String nike;
	private String email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage);
		init();
		LoadImageUtil.initImageLoader(this);
		PersonalTask task=new PersonalTask();
		task.execute(PersonpageUtil.getInstance().getPersonid());		
	}
	
	private void init() {
		// TODO Auto-generated method stub		
		topic_whose=(TextView)findViewById(R.id.tv_topic);
		title=(TextView)findViewById(R.id.tv_title);
		acount=(TextView)findViewById(R.id.personcount);
		city=(TextView)findViewById(R.id.city);
		topic_click=(RelativeLayout)findViewById(R.id.rl_topic);
		topic_click.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		nikename=(TextView)findViewById(R.id.personnike);
		personhead=(ImageView)findViewById(R.id.personhead);
		bottomline=(LinearLayout)findViewById(R.id.ll_personpagebottom);
		skillllist=(ListView)findViewById(R.id.lv_pageskill);			
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
			Button send=new Button(this);
			send.setText(getResources().getString(R.string.sendmsg));
			send.setBackgroundResource(R.drawable.selector_commit);	
			send.setLayoutParams(params);
			send.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
					ChatRoomTypeUtil.getInstance().setHuanxingId(PersonpageUtil.getInstance().getHuanxinId());
					ChatRoomTypeUtil.getInstance().setTitle(PersonpageUtil.getInstance().getPersonname());
					Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
					startActivity(intent);
				}
			});
			
			Button addfriend=new Button(this);
			addfriend.setText(getResources().getString(R.string.addfriends));
			addfriend.setBackgroundResource(R.drawable.selector_commit);		
			addfriend.setLayoutParams(params);
			bottomline.addView(send);
			bottomline.addView(addfriend);
			addfriend.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub					
					Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
					startActivity(intent);
				}
			});
			break;
		case Constant.FRIENDSPAGE:
			delete.setVisibility(View.VISIBLE);
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
			intent.putExtra("userId", "1");
			intent.putExtra("image", image);
			intent.putExtra("nike", nike);
			intent.putExtra("email", email);			
			startActivity(intent);			
			break;
		default:
			break;
		}
	}
	private class PersonalTask extends AsyncTask<String, Void, User>{

		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			User user=null;
			try {
				user=PeerUI.getInstance().getISessionManager().personalPage(params[0], callback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;
		}
		@Override
		protected void onPostExecute(User user) {
			// TODO Auto-generated method stub
			if(user!=null){
				image=user.getImage();
				nike=user.getUsername();
				email=user.getEmail();
				LoadImageUtil.imageLoader.displayImage(image, personhead, LoadImageUtil.options);				
				nikename.setText(nike);
				acount.setText(email);
				city.setText(user.getCity());
				SkillAdapter adapter=new SkillAdapter(PersonalPageActivity.this,"page",user.getLabels());
				skillllist.setAdapter(adapter);	
			}
		}
		
	}
}
