package com.peer.activitymain;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.HistoryMsgAdapter;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;

public class TopicHistoryActivity extends BasicActivity {
	private TextView title;
	private ListView chathistory;
	private LinearLayout back;
	private List<Map> mlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topichistory);
		init();
		Intent intent=getIntent();
		ReplieTask task=new ReplieTask();
		task.execute(intent.getStringExtra("topicid"));
	}
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chatmsg));
		chathistory=(ListView)findViewById(R.id.lv_chathistory);
	}
	private class ReplieTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			try {
				mlist=PeerUI.getInstance().getISessionManager().TopicReplies(params[0], callback);
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
				HistoryMsgAdapter history=new HistoryMsgAdapter(TopicHistoryActivity.this,mlist);
				chathistory.setAdapter(history);
			}			
		}
	}
	
}
