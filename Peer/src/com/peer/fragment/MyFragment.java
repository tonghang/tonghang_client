package com.peer.fragment;

import com.peer.R;

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
		// TODO Auto-generated method stub
		
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
				
		
		
	}
}
