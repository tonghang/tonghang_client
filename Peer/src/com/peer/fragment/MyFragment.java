package com.peer.fragment;

import com.peer.R;
import com.peer.activity.LoginActivity;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.widgetutil.LoadImageUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFragment extends BasicFragment {
	private RelativeLayout myacount_my,personalpage;
	private LinearLayout personmessage_my,mytag_my,setting_my;
	private ImageView headpic;
	private TextView tv_nikename,tv_email;
	public static final int UPDATESUCESS=9;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
	}
	private void init() {
		LoadImageUtil.initImageLoader(getActivity());
		
		personalpage=(RelativeLayout)getView().findViewById(R.id.rl_ponseralpage);		
		myacount_my=(RelativeLayout)getView().findViewById(R.id.rl_myacount_my);
		personmessage_my=(LinearLayout)getView().findViewById(R.id.ll_personmessage_my);
		mytag_my=(LinearLayout)getView().findViewById(R.id.ll_mytag_my);
		setting_my=(LinearLayout)getView().findViewById(R.id.ll_setting_my);
		
		personalpage.setOnClickListener(this);
		myacount_my.setOnClickListener(this);
		personmessage_my.setOnClickListener(this);
		mytag_my.setOnClickListener(this);
		setting_my.setOnClickListener(this);
		
		headpic=(ImageView)getView().findViewById(R.id.im_headpic);
		tv_nikename=(TextView)getView().findViewById(R.id.tv_nikename);
		tv_email=(TextView)getView().findViewById(R.id.tv_email);	
		
		getlocalMsg();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getlocalMsg();
	}
	public void getlocalMsg(){
		String email=LocalStorage.getString(getActivity(),Constant.EMAIL);
		UserDao u=new UserDao(getActivity());
		UserBean user=u.findOne(email);
		tv_nikename.setText(user.getNikename());
		tv_email.setText(user.getEmail());
		LoadImageUtil.imageLoader.displayImage(user.getImage(), headpic, LoadImageUtil.options);		
	}
}
