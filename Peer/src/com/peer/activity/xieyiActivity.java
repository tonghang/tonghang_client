package com.peer.activity;


import com.peer.R;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class xieyiActivity extends BasicActivity {
	private TextView title;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xieyi);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.xieyi));
		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
	}

}
