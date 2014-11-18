package com.peer.activity;

import com.peer.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UpdateNikeActivity extends BasicActivity {
	private TextView title;
	private EditText nikename;
	private Button updatenike;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatenike);
		
		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.updatenike));
		Intent t=getIntent();
		t.getStringExtra("nikename");
		nikename=(EditText)findViewById(R.id.et_updatenike);
		nikename.setText(t.getStringExtra("nikename"));
		updatenike=(Button)findViewById(R.id.bt_nikename);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		updatenike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(nikename.getText().toString().trim().equals("")){
					ShowMessage(getResources().getString(R.string.inputnike));
				}else{
					Intent t=getIntent();
					t.putExtra("newnikename", nikename.getText().toString());
					setResult(PersonalMessageActivity.UPDATENIKENAME, t);
					finish();
				}				
			}
		});
				
	}
}
