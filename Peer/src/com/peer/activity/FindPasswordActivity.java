package com.peer.activity;

import com.peer.R;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
		email.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(email.getText().toString())){
					find.setEnabled(false);
				}else{
					find.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(email.getText().toString())){
					find.setEnabled(false);
				}else{
					find.setEnabled(true);
				}
			}
		});
		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		find.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(LocalStorage.getBoolean(FindPasswordActivity.this, "istestui")){
					Intent intent=new Intent(FindPasswordActivity.this,FindPasswordResult.class);
					startActivity(intent);
					finish();					
				}else{
					PasswordTask task=new PasswordTask();
					task.execute(email.getText().toString().trim());
				}
							
			}
		});
	}
	private class PasswordTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub  
			SessionListener callback=new SessionListener();
            try {
            	
            	PeerUI.getInstance().getISessionManager().forgetPassword(params[0], callback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equals(Constant.CALLBACKSUCCESS)){
				Intent intent=new Intent(FindPasswordActivity.this,FindPasswordResult.class);
				startActivity(intent);
				finish();
			}
		}
	}
	
}
