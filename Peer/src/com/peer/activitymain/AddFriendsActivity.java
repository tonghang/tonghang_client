package com.peer.activitymain;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.ManagerActivity;
import com.peer.widgetutil.LoadImageUtil;

public class AddFriendsActivity extends BasicActivity {
	private TextView title,tv_nike,tv_email;
	private ImageView headpic;
	private LinearLayout back;
	private Button send;
	private EditText reson;
	private String id,image,nike,email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriends);
		
		Intent intent=getIntent();
		id=intent.getStringExtra("userId");
		image=intent.getStringExtra("image");
		nike=intent.getStringExtra("nike");
		email=intent.getStringExtra("email");		
		init();
				
		
	}
	private void init() {
		// TODO Auto-generated method stub
		LoadImageUtil.initImageLoader(this);
		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.checkfriends));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		send=(Button)findViewById(R.id.bt_send);
		send.setOnClickListener(this);
		headpic=(ImageView)findViewById(R.id.personhead);
		LoadImageUtil.imageLoader.displayImage(image, headpic, LoadImageUtil.options);
		tv_nike=(TextView)findViewById(R.id.personnike);
		tv_nike.setText(nike);
		tv_email=(TextView)findViewById(R.id.email);
		tv_email.setText(email);
		reson=(EditText)findViewById(R.id.add_reson);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_send:
			if(checkNetworkState()){
				AddFriendsTask task=new AddFriendsTask();
				task.execute(id,reson.getText().toString().trim());
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}		
			break;

		default:
			break;
		}
	}
	private class AddFriendsTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub				
			SessionListener callback=new SessionListener();
			try {
				PeerUI.getInstance().getISessionManager().addFriends(paramer[0], paramer[1], callback);
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
				ShowMessage(getResources().getString(R.string.addfriendssuccess));
				finish();
			}			
		}
	}
}
