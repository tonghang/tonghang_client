package com.peer.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.NotificationCompat;
import com.easemob.util.EasyUtils;
import com.peer.R;
import com.peer.activitymain.ComeMessageActivity;
import com.peer.activitymain.FriendsActivity;
import com.peer.activitymain.HomePageActivity;
import com.peer.activitymain.MyActivity;
import com.peer.util.ManagerActivity;
/**
 * all activity extends this activity
 * @author Concoon-break
 *
 */
public class BasicActivity extends FragmentActivity implements OnClickListener{	
	private static final int notifiId = 11;
    protected NotificationManager notificationManager;
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
		EMChatManager.getInstance().activityResumed();
	       
	}
	  /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
//        if(!EasyUtils.isAppRunningForeground(this)){
//            return;
//        }
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);       
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        String ticker = txtBody.getMessage();
        //设置状态栏提示
        mBuilder.setTicker(message.getFrom()+": " + ticker);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
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
