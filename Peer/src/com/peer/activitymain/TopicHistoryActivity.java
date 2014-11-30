package com.peer.activitymain;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.HistoryMsgAdapter;

public class TopicHistoryActivity extends BasicActivity {
	private TextView title;
	private ListView chathistory;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topichistory);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chatmsg));
		chathistory=(ListView)findViewById(R.id.lv_chathistory);
		HistoryMsgAdapter history=new HistoryMsgAdapter(this);
		chathistory.setAdapter(history);
	}
}
