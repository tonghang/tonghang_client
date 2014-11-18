package com.peer.activity;

import com.peer.R;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewFunctionActivity extends BasicActivity {
	private LinearLayout back;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfunction);
		init();		
	}
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.newfunction));
	}
}
