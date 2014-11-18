package com.peer.activity;

import com.peer.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAcountActivity extends BasicActivity {
	private LinearLayout changepassword,back;
	private TextView myemail,title;
	public static final int CHANGEPASSWORD=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myacount);
		init();	
		
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.mycount));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		
		myemail=(TextView)findViewById(R.id.myeamil);
//		myemail.setText(LocalStorage.getString(this, "email"));
		
		changepassword=(LinearLayout)findViewById(R.id.ll_changepassword);
		changepassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyAcountActivity.this,UpdatePasswordActivity.class);				
				startActivityForResult(intent, CHANGEPASSWORD);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case CHANGEPASSWORD:
				ShowMessage(getResources().getString(R.string.updatepasw));
				break;
			}
			}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
