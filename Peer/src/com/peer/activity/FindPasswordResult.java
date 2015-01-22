package com.peer.activity;

import com.peer.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FindPasswordResult extends BasicActivity {
	private LinearLayout back;
	private Button bt_back;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpasswordresult);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.findpassword));
		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		bt_back=(Button)findViewById(R.id.back);
		bt_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
