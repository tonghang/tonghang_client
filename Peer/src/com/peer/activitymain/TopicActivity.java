package com.peer.activitymain;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.TopicAdapter;

public class TopicActivity extends BasicActivity {
	private TextView title;
	private ListView topichistory;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.topic_other));
		topichistory=(ListView)findViewById(R.id.lv_topichistory);
		TopicAdapter adapter=new TopicAdapter(this);
		topichistory.setAdapter(adapter);
	}

}
