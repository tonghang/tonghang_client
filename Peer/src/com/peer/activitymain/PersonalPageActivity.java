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
import com.peer.localDB.LocalStorage;
import com.peer.util.AutoWrapLinearLayout;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.ManagerActivity;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Gravity;
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
	private TextView nikename,title,topic_whose,acount,city,birth,sex,skill;
	private RelativeLayout topic_click;
	private LinearLayout back,bottomline,content;
//	private ListView skillllist;
	private AutoWrapLinearLayout tagContainer;
	
	private List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	private User userpage;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage);
		init();
		LoadImageUtil.initImageLoader(this);
		if(LocalStorage.getBoolean(this, "istestui")){
			List lablelist=new ArrayList<String>();
			lablelist.add("我最爱的美食是糖醋排骨");
			lablelist.add("java是我擅长的编程语言");
			for(int i=0;i<lablelist.size();i++){
				String tag=(String) lablelist.get(i);					
				skill=(TextView) getLayoutInflater().inflate(R.layout.skill, tagContainer, false);
				skill.setHeight((int)getResources().getDimension(R.dimen.hight));
				skill.setTextSize(20);
				skill.setTextColor(getResources().getColor(R.color.white));
				int pading=(int)getResources().getDimension(R.dimen.pading);
				skill.setText(tag);
				skill.setTag(""+i);
				tagContainer.addView(skill);
			}	
		}else{
			PersonalTask task=new PersonalTask();
			task.execute(PersonpageUtil.getInstance().getPersonid());
		}
				
	}
	
	private void init() {
		// TODO Auto-generated method stub	
		topic_whose=(TextView)findViewById(R.id.tv_topic);
		title=(TextView)findViewById(R.id.tv_title);
		acount=(TextView)findViewById(R.id.personcount);
		city=(TextView)findViewById(R.id.city);
		birth=(TextView)findViewById(R.id.birthday);
		sex=(TextView)findViewById(R.id.sex);
		
		topic_click=(RelativeLayout)findViewById(R.id.rl_topic);
		topic_click.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		nikename=(TextView)findViewById(R.id.personnike);
		personhead=(ImageView)findViewById(R.id.personhead);
		bottomline=(LinearLayout)findViewById(R.id.ll_personpagebottom);
		content=(LinearLayout)findViewById(R.id.contentauto);
		tagContainer = (AutoWrapLinearLayout) findViewById(R.id.tag_container);
//		skillllist=(ListView)findViewById(R.id.lv_pageskill);			
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
					ManagerActivity.getAppManager().finishActivity();
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
					if(LocalStorage.getBoolean(PersonalPageActivity.this, "istestui")){
						Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
						startActivity(intent);
					}else{
						Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
						intent.putExtra("userId", userpage.getUserid());
						intent.putExtra("image", userpage.getImage());
						intent.putExtra("nike", userpage.getUsername());
						intent.putExtra("email", userpage.getEmail());
						startActivity(intent);
					}	
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
					ChatRoomTypeUtil.getInstance().setHuanxingId(PersonpageUtil.getInstance().getHuanxinId());
					ChatRoomTypeUtil.getInstance().setTitle(PersonpageUtil.getInstance().getPersonname());					
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
			intent.putExtra("userId", userpage.getUserid());
			intent.putExtra("image", userpage.getImage());
			intent.putExtra("nike", userpage.getUsername());
			intent.putExtra("email", userpage.getEmail());			
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
				userpage=user;
				LoadImageUtil.imageLoader.displayImage(user.getImage(), personhead, LoadImageUtil.options);				
				nikename.setText(user.getUsername());
				acount.setText(user.getEmail());
				city.setText(user.getCity());
				birth.setText(user.getBirthday());
				sex.setText(user.getSex());
				for(int i=0;i<user.getLabels().size();i++){
					String tag=user.getLabels().get(i);					

					skill=(TextView) getLayoutInflater().inflate(R.layout.skill, tagContainer, false);
					skill.setHeight((int)getResources().getDimension(R.dimen.hight));
					skill.setTextSize(20);
					skill.setTextColor(getResources().getColor(R.color.white));
					int pading=(int)getResources().getDimension(R.dimen.pading);
					skill.setText(tag);
					skill.setTag(""+i);
					tagContainer.addView(skill);
				}
				
			}
		}
		
	}
}
