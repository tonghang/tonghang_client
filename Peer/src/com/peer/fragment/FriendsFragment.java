package com.peer.fragment;

import java.util.List;

import com.peer.R;
import com.peer.adapter.FriendsAdapter;
import com.peer.adapter.HomepageAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendsFragment extends BasicFragment {
	private ListView mlistview;
	private RelativeLayout seenewfriends;
	private List<User> list;
	private FriendsAdapter adapter;
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
//		FriendsTask task=new FriendsTask();
//		task.execute();
	}
	private void init() {
		// TODO Auto-generated method stub
		mlistview=(ListView)getView().findViewById(R.id.lv_friends);				
		seenewfriends=(RelativeLayout)getView().findViewById(R.id.rl_newfriends);
		seenewfriends.setOnClickListener(this);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(list!=null){
			list.clear();
		}
		FriendsTask task=new FriendsTask();
		task.execute();
	}
	
	private class FriendsTask extends AsyncTask<Void, Void, List>{

		@Override
		protected List doInBackground(Void... arg0) {
			// TODO Auto-generated method stub       
            try {
            	list=PeerUI.getInstance().getISessionManager().myFriends();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
			return list;
		}
		@Override
		protected void onPostExecute(List result) {
			// TODO Auto-generated method stub
			if(!result.isEmpty()){
				adapter=new FriendsAdapter(getActivity(),list);
				mlistview.setAdapter(adapter);
			}
		}
	}

}
