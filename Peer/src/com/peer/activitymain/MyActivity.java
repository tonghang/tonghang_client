package com.peer.activitymain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.MySkillActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.activity.SettingActivity;
import com.peer.constant.Constant;
import com.peer.util.PersonpageUtil;

public class MyActivity extends BasicActivity{
	private TextView tv_my,showmessgenum;
	private LinearLayout find,come,my,friends;
	private ImageView backmy;
	private RelativeLayout myacount_my,personalpage;
	private LinearLayout personmessage_my,mytag_my,setting_my;
	public static final int UPDATESUCESS=9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		tv_my=(TextView)findViewById(R.id.tv_my);
		tv_my.setTextColor(getResources().getColor(R.color.bottomtextblue));
		
		personalpage=(RelativeLayout)findViewById(R.id.rl_ponseralpage);
		
		myacount_my=(RelativeLayout)findViewById(R.id.rl_myacount_my);
		personmessage_my=(LinearLayout)findViewById(R.id.ll_personmessage_my);
		mytag_my=(LinearLayout)findViewById(R.id.ll_mytag_my);
		setting_my=(LinearLayout)findViewById(R.id.ll_setting_my);
		
		personalpage.setOnClickListener(this);
		myacount_my.setOnClickListener(this);
		personmessage_my.setOnClickListener(this);
		mytag_my.setOnClickListener(this);
		setting_my.setOnClickListener(this);
				
		find=(LinearLayout)findViewById(R.id.ll_find);		
		come=(LinearLayout)findViewById(R.id.ll_come);
		my=(LinearLayout)findViewById(R.id.ll_my);
		friends=(LinearLayout)findViewById(R.id.ll_friends);
		showmessgenum=(TextView)findViewById(R.id.showmessgenum);
		
		find.setOnClickListener(this);
		come.setOnClickListener(this);
		my.setOnClickListener(this);	
		friends.setOnClickListener(this);
		backmy=(ImageView)findViewById(R.id.iv_backmy);
		backmy.setImageResource(R.drawable.mysetting_press);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_myacount_my:
			Intent myacount=new Intent(MyActivity.this,MyAcountActivity.class);
			startActivity(myacount);
			break;
		case R.id.ll_personmessage_my:
			Intent personmessage=new Intent(MyActivity.this,PersonalMessageActivity.class);
			startActivityForResult(personmessage, UPDATESUCESS);
			break;
		case R.id.ll_mytag_my:
			Intent mytag=new Intent(MyActivity.this,MySkillActivity.class);
			startActivity(mytag);
			break;
		case R.id.ll_setting_my:
			Intent setting=new Intent(MyActivity.this,SettingActivity.class);
			startActivity(setting);
			break;
		case R.id.rl_ponseralpage:
			PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
			Intent topersonalpage=new Intent(MyActivity.this,PersonalPageActivity.class);
			startActivity(topersonalpage);
			break;
		default:
			break;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case UPDATESUCESS:
				ShowMessage("更新成功");
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
