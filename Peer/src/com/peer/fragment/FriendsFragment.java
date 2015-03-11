package com.peer.fragment;

import java.util.List;
import com.peer.R;
import com.peer.adapter.FriendsAdapter;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.readystatesoftware.viewbadger.BadgeView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendsFragment extends BasicFragment{
	private ListView mlistview;
	private RelativeLayout seenewfriends;
	private List<User> list;
	private FriendsAdapter adapter;
	private BadgeView newnum;
	private TextView tv_newfriends;
	private int newfriendsnum;
	public static Handler refreshhandle;
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
		refreshhandle=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==Constant.REFRESHHANDLE){
					if(list!=null){
						list.clear();
					}
					FriendsTask task=new FriendsTask();
					task.execute();	
				}
			}
		};
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if(!hidden&&checkNetworkState()){
				if(list!=null){
					list.clear();
				}
				FriendsTask task=new FriendsTask();
				task.execute();	
			}
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();				
	}
	private void init() {
		// TODO Auto-generated method stub
		mlistview=(ListView)getView().findViewById(R.id.lv_friends);				
		seenewfriends=(RelativeLayout)getView().findViewById(R.id.rl_newfriends);
		tv_newfriends=(TextView)getView().findViewById(R.id.tv_newfriends);
		newnum=new BadgeView(getActivity(),tv_newfriends);
		seenewfriends.setOnClickListener(this);
	}
	public void setNewfriendsNum(int number){
		this.newfriendsnum=number;
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if(newfriendsnum>0){
					newnum.setText(String.valueOf(newfriendsnum));
					newnum.show();
				}else{
					newnum.hide();
				}	
			}
		});
		
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
