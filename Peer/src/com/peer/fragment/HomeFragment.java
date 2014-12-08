package com.peer.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peer.R;
import com.peer.activitymain.CreatTopicActivity;
import com.peer.activitymain.HomePageActivity;
import com.peer.activitymain.SearchActivity;
import com.peer.adapter.HomepageAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends BasicFragment{
	private TextView createtopic;
	private LinearLayout search;
	private PullToRefreshListView mPullrefreshlistview;	
	public RelativeLayout errorItem;
	public TextView errorText;
	
	List<Map> list=new ArrayList<Map>();
	HomepageAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initdata();
		init();
		
	}
	private void init() {
		// TODO Auto-generated method stub
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		
		search=(LinearLayout)getView().findViewById(R.id.ll_search);		
		createtopic=(TextView)getView().findViewById(R.id.tv_createtopic);
		
		createtopic.setOnClickListener(this);		
		search.setOnClickListener(this);
			
		mPullrefreshlistview=(PullToRefreshListView)getView().findViewById(R.id.pull_refresh_homepage);
		
		adapter=new HomepageAdapter(getActivity(),list);
		mPullrefreshlistview.setAdapter(adapter);
//		RefreshListner();
	}
	private void initdata() {
		// TODO Auto-generated method stub
		Map m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
	}
	private void freshdata(){
		Map m=new HashMap<String, String>();
		m.put("type", "topic");
		for(int i=0;i<10;i++){
			list.add(m);
		}	
	}
}
