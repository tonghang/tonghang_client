package com.peer.activity;


import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

public class SplashActivity extends BasicActivity {
	private LinearLayout rootLayout;
	private	Map config;
//	private TextView versionText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		rootLayout = (LinearLayout) findViewById(R.id.splash_root);

		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(2000);
		rootLayout.startAnimation(animation);
		//友盟统计 发送策略
		MobclickAgent.updateOnlineConfig( this );
		//友盟统计 数据加密
		AnalyticsConfig.enableEncrypt(true);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(checkNetworkState()){
//			Autologin();
			SystemConfigTask task=new SystemConfigTask();
			task.execute();
		}else{
			Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
//	private void Autologin() {
//		// TODO Auto-generated method stub
//		final String email=LocalStorage.getString(this, Constant.EMAIL);
//		UserDao userdao=new UserDao(this);
//		final String password=userdao.getPassord(email);
//		String status=userdao.getUserStatus(email);
////		SystemConfig(email,password,status);
//	}
	/*
	public void SystemConfig(final String username,final String password,final String status){
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub	
				SessionListener callback=new SessionListener();
				try {
					Thread.sleep(1000);	
					Map config=PeerUI.getInstance().getISessionManager().SystemConfig(callback);
					if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
						LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_UPGRADE_SILENTLY, (Boolean)config.get("can_upgrade_silently"));
						LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_LOGIN, (Boolean)config.get("can_login"));
						LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_REGISTER_USER, (Boolean)config.get("can_register_user"));
						boolean canlogin=(Boolean)config.get("can_login");
						if(canlogin&&!username.equals("")&&!password.equals("")&&
								status!=null&&status.equals(Constant.LOGINED)){						
								LoginTask task=new LoginTask();
								task.execute(username,password);					 
						}else{						
							 runOnUiThread(new Runnable() {
									public void run() {
										Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
										startActivity(intent);
										finish();
									}
								});	
						}
					}else{
						runOnUiThread(new Runnable() {
							public void run() {
								Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
								startActivity(intent);
								finish();
							}
						});	
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});
		t.start();
	}
	*/
	private void SaveMsg(String emial,String password,User u){
		//本地存储操作。。。										
		 LocalStorage.saveString(SplashActivity.this, Constant.EMAIL, emial);
		 UserDao userdao=new UserDao(SplashActivity.this);
		 userdao.UpdateUserStatus(Constant.LOGINED, emial);
		 
		 com.peer.localDBbean.UserBean userbean=new com.peer.localDBbean.UserBean();
		 userbean.setEmail(emial);
		 userbean.setPassword(password);
		 userbean.setAge(u.getBirthday());
		 userbean.setCity(u.getCity());
		 userbean.setNikename(u.getUsername());
		 userbean.setImage(u.getImage());
		 userbean.setSex(u.getSex());
		 if(userdao.findOne(emial)==null){						
			 userdao.addUser(userbean);
		 }else{
			 userdao.updateUser(userbean);
		 }
	}
	private class SystemConfigTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			try {
				Thread.sleep(2000);	
				config=PeerUI.getInstance().getISessionManager().SystemConfig(callback);
			}catch (Exception e){
				e.printStackTrace();
			}
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null&&result.equals(Constant.CALLBACKSUCCESS)){
				LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_UPGRADE_SILENTLY, (Boolean)config.get("can_upgrade_silently"));
				LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_LOGIN, (Boolean)config.get("can_login"));
				LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_REGISTER_USER, (Boolean)config.get("can_register_user"));
				boolean canlogin=(Boolean)config.get("can_login");
				
				String email=LocalStorage.getString(SplashActivity.this, Constant.EMAIL);
				UserDao userdao=new UserDao(SplashActivity.this);
				String password=userdao.getPassord(email);
				String status=userdao.getUserStatus(email);
				if(canlogin&&!email.equals("")&&!password.equals("")&&
						status!=null&&status.equals(Constant.LOGINED)){
					LoginTask task=new LoginTask();
					task.execute(email,password);
				}else{
					Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}else{
				Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
		
	}
	private class LoginTask extends AsyncTask<String, String, SessionListener>{

		@Override
		protected SessionListener doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			try {							
				User u=PeerUI.getInstance().getISessionManager().login(params[0], params[1], callback);					
				if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){												
					JPushInterface.setAlias(getApplication(), u.getHuangxin_username(), new TagAliasCallback() {							
						@Override
						public void gotResult(int code, String arg1, Set<String> arg2) {
							// TODO Auto-generated method stub
							System.out.println("code"+code);
							Log.i("注册极光结果放回", String.valueOf(code));
						}
					});
					 SaveMsg(params[0], params[1],u);
					 String huanxinid=PeerUI.getInstance().getISessionManager().getHuanxingUser();
					 easemobchatImp.getInstance().login(huanxinid, params[1]);
					 easemobchatImp.getInstance().loadConversationsandGroups();
				}											
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				 Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
				 startActivity(intent);
				 finish();
//				e.printStackTrace();
			} 				
			return callback;
		}
		@Override
		protected void onPostExecute(SessionListener result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.getMessage().equals(Constant.CALLBACKSUCCESS)){
				List<String> labels=null;
				try {
					labels = PeerUI.getInstance().getISessionManager().getLabels();
					if(labels.isEmpty()){					
						 Intent intent=new Intent(SplashActivity.this,RegisterTagActivity.class);
						 startActivity(intent);
						 finish();
					 }else{						
						Intent intent=new Intent(SplashActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					 }
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else{
				 Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
				 startActivity(intent);
				 finish();
			}
		}
	}
	
}
