package com.peer.activitymain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;

public class CreatTopicActivity extends BasicActivity {
	private TextView title;
	private Button creattopic;
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
		creattopic=(Button)findViewById(R.id.bt_creattopic);
		creattopic.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.createtopic));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_creattopic:
			ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
			Intent intent=new Intent(CreatTopicActivity.this,ChatRoomActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
}