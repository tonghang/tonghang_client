package com.peer.activitymain;

import java.util.List;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ImageView;
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
import com.peer.widgetutil.LoadImageUtil;

public class TopicActivity extends BasicActivity {
	private ImageView headpic;
	private TextView title,nike,email;
	private ListView topichistory;
	private LinearLayout back;
	private List<Topic> mlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		init();
		Intent intent=getIntent();
		TopicTask task=new TopicTask();
		task.execute(intent.getStringExtra("userId"));
		try {
			if(PeerUI.getInstance().getISessionManager().getUserId().equals(intent.getStringExtra("userId"))){
				title.setText(getResources().getString(R.string.topic_owen));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		LoadImageUtil.initImageLoader(this);		
		LoadImageUtil.imageLoader.displayImage(intent.getStringExtra("image"), headpic, LoadImageUtil.options);
		nike.setText(intent.getStringExtra("nike"));
		email.setText(intent.getStringExtra("email"));						
	}

	private void init() {
		// TODO Auto-generated method stub
		headpic=(ImageView)findViewById(R.id.personhead);
		nike=(TextView)findViewById(R.id.personnike);
		email=(TextView)findViewById(R.id.email);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.topic_other));
		topichistory=(ListView)findViewById(R.id.lv_topichistory);		
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
				TopicAdapter adapter=new TopicAdapter(TopicActivity.this,mlist);
				topichistory.setAdapter(adapter);
			}		
		}
	}
}
