package com.peer.activitymain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;

public class AddFriendsActivity extends BasicActivity {
	private TextView title;
	private LinearLayout back;
	private Button send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriends);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.checkfriends));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		send=(Button)findViewById(R.id.bt_send);
		send.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_send:
			AddFriendsTask task=new AddFriendsTask();
			task.execute();
			ShowMessage(getResources().getString(R.string.addfriendssuccess));
			break;

		default:
			break;
		}
	}
	private class AddFriendsTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub				
			SessionListener callback=new SessionListener();
			try {
				PeerUI.getInstance().getISessionManager().addFriends("2", "hello", callback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

		}
	}
}
