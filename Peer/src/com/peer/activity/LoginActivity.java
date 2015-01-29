package com.peer.activity;

import java.util.List;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
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
	private ProgressDialog pd;
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

		register_login=(TextView)findViewById(R.id.tv_register_login);
		forget_login=(TextView)findViewById(R.id.tv_forgetpasw_login);
		login_remind=(TextView)findViewById(R.id.tv_remind_login);
		
		register_login.setOnClickListener(this);
		forget_login.setOnClickListener(this);
		login_login.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login_login:
			if(checkNetworkState()){
				String email=email_login.getText().toString().trim();
				String password=password_login.getText().toString().trim();
				pd = ProgressDialog.show(LoginActivity.this,"", "正在登陆请稍候。。。");
				Login(email,password);
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
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
	public void Login(final String username,final String password){
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SessionListener callback=new SessionListener();
				try {
					
					User u=PeerUI.getInstance().getISessionManager().login(username, password, callback);
					
					if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){											
	
						JPushInterface.setAlias(getApplication(), u.getHuangxin_username(), new TagAliasCallback() {
							
							@Override
							public void gotResult(int code, String arg1, Set<String> arg2) {
								// TODO Auto-generated method stub
								System.out.println("code"+code);
								Log.i("注册极光结果放回", String.valueOf(code));
//								Toast.makeText(RegisterAcountActivity.this, code, 0).show();
							}
						});
						//本地存储操作。。。			
						String userid=PeerUI.getInstance().getISessionManager().getUserId();
						
						 LocalStorage.saveString(LoginActivity.this, Constant.EMAIL, username);
						 UserDao userdao=new UserDao(LoginActivity.this);
						 com.peer.localDBbean.UserBean userbean=new com.peer.localDBbean.UserBean();
						 userbean.setEmail(username);
						 userbean.setPassword(password);
						 userbean.setAge(u.getBirthday());
						 userbean.setCity(u.getCity());
						 userbean.setNikename(u.getUsername());
						 userbean.setImage(u.getImage());
						 userbean.setSex(u.getSex());
						 if(userdao.findOne(username)==null){						
							 userdao.addUser(userbean);
						 }else{
							 userdao.updateUser(userbean);
						 }
						 
						 List<String> labels=PeerUI.getInstance().getISessionManager().getLabels();
						 if(labels.isEmpty()){
							 pd.dismiss();
							 Intent intent=new Intent(LoginActivity.this,RegisterTagActivity.class);
							 startActivity(intent);
						 }else{
							pd.dismiss();
							String huanxinid=PeerUI.getInstance().getISessionManager().getHuanxingUser();
							easemobchatImp.getInstance().login(huanxinid, password);
							easemobchatImp.getInstance().loadConversationsandGroups();	
							Intent intent=new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
						 }
					}else{						
						runOnUiThread(new Runnable() {
							public void run() {
								pd.dismiss();
								login_remind.setText(getResources().getString(R.string.remind_login));
							}
						});						
					}
				
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});
		t.start();
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
