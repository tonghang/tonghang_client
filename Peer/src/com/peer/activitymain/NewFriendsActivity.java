package com.peer.activitymain;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.BasicActivity;
import com.peer.activity.LoginActivity;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.event.NewFriensEvent;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;

import de.greenrobot.event.EventBus;

public class NewFriendsActivity extends BasicActivity {
	private ListView mlistview;
	private TextView title;
	private LinearLayout back;	
	private NewfriendsAdapter adapter;
	private EventBus mBus;
	private List<User> mlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfriends);
		init();		
		registEventBus();		
		InvitationsTask task=new InvitationsTask();
		task.execute();	
	}	
	
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.newfriends));
		
		mlistview=(ListView)findViewById(R.id.lv_newfriends);
		
	}
	private void registEventBus() {
		// TODO Auto-generated method stub
		 mBus=EventBus.getDefault();
		/*
		 * Registration: three parameters are respectively, message subscriber (receiver), receiving method name, event classes
		 */
		 mBus.register(this, "getEvent",NewFriensEvent.class);
	}
	private void getEvent(NewFriensEvent event){
		
		mlist.remove(event.getPosition());
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 mBus.unregister(this);
	}
	private class InvitationsTask extends AsyncTask<String, String, String>{		
		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub						
			SessionListener callback=new SessionListener();
			try {
				mlist=PeerUI.getInstance().getISessionManager().Invitations(callback);			
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equals(Constant.CALLBACKSUCCESS)){
				adapter=new NewfriendsAdapter(NewFriendsActivity.this,mlist);
				mlistview.setAdapter(adapter);
			}			
		}
		
	}
}
