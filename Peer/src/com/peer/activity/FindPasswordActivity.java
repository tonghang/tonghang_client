package com.peer.activity;

import com.peer.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
public class FindPasswordActivity extends BasicActivity {
	private TextView remind,title;
	private Button find;
	private EditText email;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.findpassword));
		remind=(TextView)findViewById(R.id.email_test);
		email=(EditText)findViewById(R.id.et_email_find);
		find=(Button)findViewById(R.id.bt_findpassword);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		find.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
