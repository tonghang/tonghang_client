package com.peer.fragment;

import com.peer.R;
import com.peer.adapter.FriendsAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendsFragment extends BasicFragment {
	private ListView mlistview;
	private RelativeLayout seenewfriends;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_friends, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		
	}
	private void init() {
		// TODO Auto-generated method stub
		mlistview=(ListView)getView().findViewById(R.id.lv_friends);		
		FriendsAdapter adapter=new FriendsAdapter(getActivity());
		mlistview.setAdapter(adapter);
		seenewfriends=(RelativeLayout)getView().findViewById(R.id.rl_newfriends);
		seenewfriends.setOnClickListener(this);
	}

}
