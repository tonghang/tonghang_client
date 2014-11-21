package com.peer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.peer.R;
import com.peer.activitymain.ComeMessageActivity;
import com.peer.activitymain.FriendsActivity;
import com.peer.activitymain.HomePageActivity;
import com.peer.activitymain.MyActivity;
import com.peer.util.HomeWatcher;
import com.peer.util.ManagerActivity;
import com.peer.util.OnHomePressedListener;
/**
 * all activity extends this activity
 * @author Concoon-break
 *
 */
public class BasicActivity extends Activity implements OnClickListener{	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	     
	    ManagerActivity.getAppManager().addActivity(this);
						
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!checkNetworkState()){			
//			ShowMessage(getResources().getString(R.string.Broken_network_prompt));
		}
	}
	
	/**
	 *  Prompt information to user
	 */
	public void ShowMessage(String message){
		Toast.makeText(this, message, 0).show();
	}
	/**
	 * check Network State
	 * @return
	 */
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.ll_find:
			Intent tofind=new Intent(this,HomePageActivity.class);
			startActivity(tofind);
			overridePendingTransition(R.anim.fade, R.anim.hold); 
			break;
		case R.id.ll_come:
			Intent tocome=new Intent(this,ComeMessageActivity.class);
			startActivity(tocome);
			overridePendingTransition(R.anim.fade, R.anim.hold); 
			break;
		case R.id.ll_my:
			Intent tosetting=new Intent(this,MyActivity.class);
			startActivity(tosetting);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		case R.id.ll_friends:
			Intent tofriends=new Intent(this,FriendsActivity.class);
			startActivity(tofriends);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		default:
			break;
		}
	}
}
