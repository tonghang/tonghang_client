package com.peer.activitymain;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.peer.R;
import com.peer.activity.BasicActivity;

public class CreatTopicActivity extends BasicActivity {
	private TextView title;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creattopic);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.createtopic));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
	}
}
