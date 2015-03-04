package com.peer.activitymain;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;

import com.peer.adapter.TopicAdapter;
import com.peer.client.Topic;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;

public class MytopicActivity extends BasicActivity {
	private ListView topichistory;
	private LinearLayout back;
	private List<Topic> mlist;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytopic);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.topic_owen));
		topichistory=(ListView)findViewById(R.id.lv_topichistory);	
		
		String userid=null;
		try {
			userid = PeerUI.getInstance().getISessionManager().getUserId();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TopicTask task=new TopicTask();
		task.execute(userid);
	}
	private class TopicTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();			
			try {
				mlist=PeerUI.getInstance().getISessionManager().topicHistory(params[0],callback);
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
				TopicAdapter adapter=new TopicAdapter(MytopicActivity.this,mlist);
				topichistory.setAdapter(adapter);
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}
		}
	}
}
