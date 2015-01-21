package com.peer.activity;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
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
import com.peer.activitymain.ChatRoomActivity;
import com.peer.activitymain.MainActivity;
import com.peer.localDB.LocalStorage;
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
//    	notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(getApplicationInfo().icon)
//                .setWhen(System.currentTimeMillis()).setAutoCancel(true);       
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        String ticker = txtBody.getMessage();
//        //设置状态栏提示
//        mBuilder.setTicker(message.getFrom()+": " + ticker);
//
//        Notification notification = mBuilder.build();
//        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
        
        Calendar c = Calendar.getInstance(); 
	    int hours=c.get(Calendar.HOUR_OF_DAY); 
		int munite=c.get(Calendar.MINUTE);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	      //构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
	    Notification notification = new Notification(R.drawable.logo,"职客",System.currentTimeMillis());
	    Intent intent = new Intent(BasicActivity.this,MainActivity.class);
	     
	      
	      PendingIntent pendingIntent = PendingIntent.getActivity(BasicActivity.this,0,intent,0);                                                                          notification.setLatestEventInfo(getApplicationContext(), "通知标题", "通知显示的内容", pendingIntent);
	      notification.setLatestEventInfo(this, "职客",ticker, pendingIntent);	     
	      notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失	
	      int starth=LocalStorage.getInt(BasicActivity.this, "starth", 22);
	      int startm=LocalStorage.getInt(BasicActivity.this, "startm", 30);
	      int endh=LocalStorage.getInt(BasicActivity.this, "endh", 8);
	      int endm=LocalStorage.getInt(BasicActivity.this, "endm", 30);
	      if(DateFormat.is24HourFormat(this)){
				if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
					 ToNotifyStyle(notification);
				}
			}else{
				 String amorpm;
			     if(c.get(Calendar.AM_PM)==0){
			    	 amorpm="am";
			    	 if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
						 ToNotifyStyle(notification);
					}
			     }else{
			    	 amorpm="pm";
			    	 hours=hours+12;
			    	 if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
						 ToNotifyStyle(notification);
					}
			     }
			    	
			}		 	    
	      manager.notify(1, notification);//发动通知,id由自己指定，每一个Notification对应的唯一标志           
        
    }
    private void ToNotifyStyle(Notification notification) {
		// TODO Auto-generated method stub
		if(LocalStorage.getBoolean(this, "sound")){
	    	 notification.defaults = Notification.DEFAULT_SOUND;//声音默认
	     }
	     if(LocalStorage.getBoolean(this, "vibrate")){
	    	 notification.defaults = Notification.DEFAULT_VIBRATE;
	     }
//	     if(LocalStorage.getBoolean(this, "sound")&&LocalStorage.getBoolean(this, "vibrate")){
//	    	 notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND; 
//	     }		
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
		}
	}
}
