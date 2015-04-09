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
//	private TextView versionText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		Autologin();
		
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
		
	}
	private void Autologin() {
		// TODO Auto-generated method stub
		String email=LocalStorage.getString(this, Constant.EMAIL);
		UserDao userdao=new UserDao(this);
		String password=userdao.getPassord(email);
		String status=userdao.getUserStatus(email);
		 if(status!=null&&status.equals(Constant.LOGINED)){
			 SystemConfig(email,password);						 
		 }else{
			 new Thread(new Runnable() {			
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
						startActivity(intent);
					}
				}).start();
		 }
	}
	public void SystemConfig(final String username,final String password){
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub	
				SessionListener callback=new SessionListener();
				try {
					Thread.sleep(1000);	
					Map config=PeerUI.getInstance().getISessionManager().SystemConfig(callback);
					LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_UPGRADE_SILENTLY, (Boolean)config.get("can_upgrade_silently"));
					LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_LOGIN, (Boolean)config.get("can_login"));
					LocalStorage.saveBoolean(SplashActivity.this, Constant.CAN_REGISTER_USER, (Boolean)config.get("can_register_user"));
					boolean canlogin=(Boolean)config.get("can_login");
					if(canlogin){
						 LoginTask task=new LoginTask();
						 task.execute(username,password);
					}else{						
						 runOnUiThread(new Runnable() {
								public void run() {
									Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
									startActivity(intent);
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
					//本地存储操作。。。										
					 LocalStorage.saveString(SplashActivity.this, Constant.EMAIL, params[0]);
					 UserDao userdao=new UserDao(SplashActivity.this);
					 userdao.UpdateUserStatus(Constant.LOGINED, params[0]);
					 
					 com.peer.localDBbean.UserBean userbean=new com.peer.localDBbean.UserBean();
					 userbean.setEmail(params[0]);
					 userbean.setPassword(params[1]);
					 userbean.setAge(u.getBirthday());
					 userbean.setCity(u.getCity());
					 userbean.setNikename(u.getUsername());
					 userbean.setImage(u.getImage());
					 userbean.setSex(u.getSex());
					 if(userdao.findOne(params[0])==null){						
						 userdao.addUser(userbean);
					 }else{
						 userdao.updateUser(userbean);
					 }
					 
					 List<String> labels=PeerUI.getInstance().getISessionManager().getLabels();
					 if(labels.isEmpty()){					
						 Intent intent=new Intent(SplashActivity.this,RegisterTagActivity.class);
						 startActivity(intent);
					 }else{							
						String huanxinid=PeerUI.getInstance().getISessionManager().getHuanxingUser();
						easemobchatImp.getInstance().login(huanxinid, params[1]);
						easemobchatImp.getInstance().loadConversationsandGroups();	
						Intent intent=new Intent(SplashActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					 }
				}else{						
					runOnUiThread(new Runnable() {
						public void run() {
							Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});						
				}											
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 				
			
			return callback;
		}
		@Override
		protected void onPostExecute(SessionListener result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
}
