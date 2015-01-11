package com.peer.activity;

import com.peer.R;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedBackActivity extends BasicActivity {
	private EditText content;
	private Button commite;
	private LinearLayout back;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.feedback));
		
		content=(EditText)findViewById(R.id.et_feedback_content);		
		content.addTextChangedListener(watcher);
		commite=(Button)findViewById(R.id.commite_feedback);
		commite.setEnabled(false);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		commite.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String contentcommite=content.getText().toString().trim();
				if(checkNetworkState()){
					FeedBackTask task=new FeedBackTask();
					task.execute(contentcommite);					
				}else{
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}							
			}
		});
	}
	TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			if(!content.getText().toString().trim().equals("")){
				commite.setEnabled(true);
			}else {
				commite.setEnabled(false);
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
			if(!content.getText().toString().trim().equals("")){
				commite.setEnabled(true);
			}else {
				commite.setEnabled(false);
			}
		}
	};

	private class FeedBackTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub  
			SessionListener callback=new SessionListener();
            try {            	
            	PeerUI.getInstance().getISessionManager().feedback(params[0], callback);
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
				content.setText("");
				ShowMessage(getResources().getString(R.string.commit));
			}
		}
	}
}
