package com.peer.activitymain;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easemob.EMConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.BasicActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import com.peer.localDB.LocalStorage;
import com.peer.widgetutil.FxService;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;


public class MainActivity extends BasicActivity{
	private HomeFragment homefragment;
	private ComeMsgFragment comemsgfragment;
	private FriendsFragment friendsfragment;
	private MyFragment myfragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	/* bottom layout*/
	private LinearLayout find,come,my,friends;
	private TextView tv_find,tv_come,tv_my,tv_friends,unreadmsgnum,tv_newfriendsnum;
	private ImageView findback,comeback,friendsback,myback;
	
	private NewMessageBroadcastReceiver msgReceiver;
	private BadgeView unredmsg,bdnewfriendsnum;
	public int intnewfriendsnum;
	
	private String mPageName="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		init();
		registerEMchat();
		if(LocalStorage.getBoolean(this, Constant.CAN_UPGRADE_SILENTLY)){
			UmengUpdateAgent.silentUpdate(this);
		}else{
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.update(this);
		}
		
	}
	private void registerEMchat() {
		// TODO Auto-generated method stub		
		if(EMChatManager.getInstance().isConnected()){
			msgReceiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(
					EMChatManager.getInstance().getNewMessageBroadcastAction());
			intentFilter.setPriority(3);
			registerReceiver(msgReceiver, intentFilter);
			EMChatManager.getInstance().addConnectionListener(new IMconnectionListner());
			
			EMChat.getInstance().setAppInited();
		}		
	}
	private void init() {
		// TODO Auto-generated method stub
		/*init fragment*/
		homefragment=new HomeFragment();
		comemsgfragment=new ComeMsgFragment();
		friendsfragment=new FriendsFragment();
		myfragment=new MyFragment();
		fragments=new Fragment[]{
				homefragment,comemsgfragment,friendsfragment,myfragment
		};
		getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, homefragment)
			.add(R.id.fragment_container,comemsgfragment).hide(comemsgfragment)
			.add(R.id.fragment_container,friendsfragment).hide(friendsfragment)
			.show(homefragment).commit();
		/*init bottom layout*/
		find=(LinearLayout)findViewById(R.id.ll_find);		
		come=(LinearLayout)findViewById(R.id.ll_come);
		my=(LinearLayout)findViewById(R.id.ll_my);
		friends=(LinearLayout)findViewById(R.id.ll_friends);
		
		tv_find=(TextView)findViewById(R.id.tv_find);
		tv_come=(TextView)findViewById(R.id.tv_come);
		tv_friends=(TextView)findViewById(R.id.tv_friends);
		tv_my=(TextView)findViewById(R.id.tv_my);
		unreadmsgnum=(TextView)findViewById(R.id.showmessgenum);
		tv_newfriendsnum=(TextView)findViewById(R.id.tv_newfriendsnum);
		unredmsg=new BadgeView(this,unreadmsgnum);
		bdnewfriendsnum=new BadgeView(this,tv_newfriendsnum);
		
		findback=(ImageView)findViewById(R.id.iv_backfind);
		comeback=(ImageView)findViewById(R.id.iv_backcome);
		friendsback=(ImageView)findViewById(R.id.iv_backfriends);
		myback=(ImageView)findViewById(R.id.iv_backmy);
			
		tv_find.setTextColor(getResources().getColor(R.color.bottomtextblue));		
		findback.setImageResource(R.drawable.peer_press);
				
	}
	
	public void onTabClicked(View v) {
		// TODO Auto-generated method stub	
		tv_find.setTextColor(getResources().getColor(R.color.bottomtextgray));	
		tv_come.setTextColor(getResources().getColor(R.color.bottomtextgray));					
		tv_friends.setTextColor(getResources().getColor(R.color.bottomtextgray));
		tv_my.setTextColor(getResources().getColor(R.color.bottomtextgray));		
		
		findback.setImageResource(R.drawable.peer_nol);
		comeback.setImageResource(R.drawable.come_mess_nol);
		friendsback.setImageResource(R.drawable.find_label_nol);
		myback.setImageResource(R.drawable.mysetting_nol);
		
		switch (v.getId()) {
		case R.id.ll_find:
			index=0;			
			tv_find.setTextColor(getResources().getColor(R.color.bottomtextblue));			
			findback.setImageResource(R.drawable.peer_press);
			break;
		case R.id.ll_come:
			index=1;			
			tv_come.setTextColor(getResources().getColor(R.color.bottomtextblue));					
			comeback.setImageResource(R.drawable.come_mess_press);	
			break;
		case R.id.ll_friends:
			index=2;	
			tv_friends.setTextColor(getResources().getColor(R.color.bottomtextblue));
			friendsback.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_my:
			index=3;			
			tv_my.setTextColor(getResources().getColor(R.color.bottomtextblue));		
			myback.setImageResource(R.drawable.mysetting_press);
			break;
		default:
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		currentTabIndex = index;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
			  Intent intent1 = new Intent(MainActivity.this, FxService.class);
				stopService(intent1);
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.addCategory(Intent.CATEGORY_HOME);
		        startActivity(intent);
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		unregisterReceiver(msgReceiver);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUnreadLabel();
		MobclickAgent.onPageStart(mPageName);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SessionListener callback=new SessionListener();
				try {
					List<User> mlist=PeerUI.getInstance().getISessionManager().Invitations(callback);			
					if(mlist!=null){
						intnewfriendsnum=mlist.size();
						friendsfragment.setNewfriendsNum(intnewfriendsnum);
					}					
					runOnUiThread(new Runnable() {
						public void run() {
							updatenewfriends();	
						}
					});
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}).start();
		
	}
	public void updatenewfriends(){
		if(intnewfriendsnum>0){
			bdnewfriendsnum.setText(String.valueOf(intnewfriendsnum));
			bdnewfriendsnum.show();
		}else{
			bdnewfriendsnum.hide();
		}
	}
	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = easemobchatImp.getInstance().getUnreadMesTotal();
		if (count > 0) {
			unredmsg.setText(String.valueOf(count));
			unredmsg.show();
		} else {
			unredmsg.hide();
		}
	}
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        //消息id
	        String msgId = intent.getStringExtra("msgid");
	        //发消息的人的username(userid)
	        String msgFrom = intent.getStringExtra("from");
	        //更方便的方法是通过msgId直接获取整个message
	        EMMessage message = EMChatManager.getInstance().getMessage(msgId);	       
	        
	        if (ChatRoomActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(ChatRoomActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (msgFrom.equals(ChatRoomActivity.activityInstance.getToChatUsername()))
						return;
				}
			}	        
	        abortBroadcast();
	        updateUnreadLabel();
	        notifyNewMessage(message);
	        if (comemsgfragment!=null) {
	        	comemsgfragment.refresh();
			}
	        
	        }
	}
	public class IMconnectionListner implements EMConnectionListener{

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					homefragment.errorItem.setVisibility(View.GONE);
				}
			});
		}

		@Override
		public void onDisconnected(int error) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					homefragment.errorItem.setVisibility(View.VISIBLE);
				}
			});
		}

	}
}
