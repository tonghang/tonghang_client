package com.peer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.peer.R;
import com.peer.IMimplements.RingLetterImp;
import com.peer.activitymain.HomePageActivity;
import com.peer.activitymain.MainActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.util.ManagerActivity;

public class LoginActivity extends BasicActivity{
	private EditText email_login,password_login;
	private Button login_login;
	private TextView register_login,forget_login,login_remind;
	private CheckBox testUI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();	
		
	}
	
	private void init() {
		// TODO Auto-generated method stub		
		email_login=(EditText)findViewById(R.id.et_email_login);
		password_login=(EditText)findViewById(R.id.et_password_login);
		
		email_login.addTextChangedListener(textwatcher);
		password_login.addTextChangedListener(textwatcher);
		
		login_login=(Button)findViewById(R.id.bt_login_login);
//		login_login.setEnabled(false);
		register_login=(TextView)findViewById(R.id.tv_register_login);
		forget_login=(TextView)findViewById(R.id.tv_forgetpasw_login);
//		login_remind=(TextView)findViewById(R.id.tv_remind_login);
		
		register_login.setOnClickListener(this);
		forget_login.setOnClickListener(this);
		login_login.setOnClickListener(this);
		
		testUI=(CheckBox)findViewById(R.id.cb_testui);
		if(LocalStorage.getBoolean(this, "istestui")){
			testUI.setChecked(true);
			login_login.setEnabled(true);
		}else{
			testUI.setChecked(false);
			login_login.setEnabled(false);
		}
		
		testUI.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					LocalStorage.saveBoolean(LoginActivity.this, "istestui", true);
					login_login.setEnabled(true);
				}else{
					LocalStorage.saveBoolean(LoginActivity.this, "istestui", false);
					login_login.setEnabled(false);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login_login:
			if(LocalStorage.getBoolean(this, "istestui")){
				Intent home=new Intent(LoginActivity.this,HomePageActivity.class);
				startActivity(home);
				finish();
			}else{
				if(checkNetworkState()){
					String email=email_login.getText().toString().trim();
					String password=password_login.getText().toString().trim();
					
					LoginTask task=new LoginTask();
					task.execute(email,password);
				}else{
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}			
			}			
			break;
		case R.id.tv_register_login:
			Intent regist=new Intent(LoginActivity.this,RegisterAcountActivity.class);
			startActivity(regist);
			break;
		case R.id.tv_forgetpasw_login:	
			Intent forget=new Intent(LoginActivity.this,FindPasswordActivity.class);
			startActivity(forget);
			break;
		default:
			break;
		}
	}
	private class LoginTask extends AsyncTask<String, String, String>{		
		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub
						
			SessionListener callback=new SessionListener();
			try {
				User u=PeerUI.getInstance().getISessionManager().login(paramer[0], paramer[1], callback);
				if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
					String huanxinid=PeerUI.getInstance().getISessionManager().getHuanxingUser();
//					RingLetterImp.getInstance().login(huanxinid, paramer[1]);
//					RingLetterImp.getInstance().loadConversationsandGroups();					
//本地存储操作。。。			
					 LocalStorage.saveString(LoginActivity.this, "email", paramer[0]);
					 UserDao userdao=new UserDao(LoginActivity.this);
					 if(userdao.findOne(paramer[0])==null){
						 com.peer.localDBbean.UserBean userbean=new com.peer.localDBbean.UserBean();
						 userbean.setEmail(u.getEmail());
						 userbean.setPassword(paramer[1]);
						 userbean.setAge(u.getBirthday());
						 userbean.setCity(u.getCity());
						 userbean.setNikename(u.getUsername());
						 userbean.setImage(u.getImage());
						 userbean.setSex(u.getSex());
//						 userdao.addUser(userbean);
					 }
					 
				}
			
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
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
				
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
			  ManagerActivity.getAppManager().finishAllActivity();
		        return true;
		   }
		  return super.onKeyDown(keyCode, event);
	}
	TextWatcher textwatcher=new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			String email=email_login.getText().toString().trim();
			String password=password_login.getText().toString().trim();
			if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
				login_login.setEnabled(true);
			}else{
				login_login.setEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			String email=email_login.getText().toString().trim();
			String password=password_login.getText().toString().trim();
			if(!email.equals("")&&!password.equals("")){
				login_login.setEnabled(true);
			}else{
				login_login.setEnabled(false);
			}
		}				
		};	
		
}
