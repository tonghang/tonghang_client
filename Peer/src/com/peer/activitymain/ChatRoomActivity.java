package com.peer.activitymain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.BasicActivity;
import com.peer.adapter.ChatMsgViewAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.entity.ChatMsgEntity;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.widgetutil.LoadImageUtil;

public class ChatRoomActivity extends BasicActivity {
	private TitlePopup titlePopup;
	private LinearLayout back;
	private RelativeLayout rl_owner;
	private Button sendmessage;
	private EditText messagebody;
	private ImageView downwindow,ownerimg;
	private TextView tv_tagname,nikename,tv_theme,tv_seemember;
	boolean isFirst=true;
	private ListView selflistview;
	private ChatMsgViewAdapter adapter;	
	private List<ChatMsgEntity> msgList=new ArrayList<ChatMsgEntity>();
	private String topicId;
	private String toChatUsername;
	public static ChatRoomActivity activityInstance = null;
	private EMConversation conversation;
	private NewMessageBroadcastReceiver receiver;
	private InputMethodManager manager;
	
	private String imagurl=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);
		LoadImageUtil.initImageLoader(this);
		init();
		roomType();
		initChatListener();		
	}	
	
	private void init() {
		// TODO Auto-generated method stub
		try {
			imagurl=PeerUI.getInstance().getISessionManager().getImagUrL();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		toChatUsername=ChatRoomTypeUtil.getInstance().getHuanxingId();
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		rl_owner=(RelativeLayout)findViewById(R.id.host_imfor);		
		popupwindow();
		ScrollView mScrollView = (ScrollView)findViewById(R.id.scrollContent);  
		mScrollView.setVerticalScrollBarEnabled(false);  
		mScrollView.setHorizontalScrollBarEnabled(false);
		downwindow=(ImageView)findViewById(R.id.im_downview);
		downwindow.setOnClickListener(this);	
		
		ownerimg=(ImageView)findViewById(R.id.host_image);
		
		tv_tagname=(TextView)findViewById(R.id.tv_tagname);		
		nikename=(TextView)findViewById(R.id.tv_nikename);
		tv_theme=(TextView)findViewById(R.id.theme_chat);
		tv_seemember=(TextView)findViewById(R.id.tv_seemember);
		tv_seemember.setOnClickListener(this);
		
		selflistview=(ListView)findViewById(R.id.lv_chat);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);

		messagebody=(EditText)findViewById(R.id.et_sendmessage);
		hideKeyboard();

		sendmessage=(Button)findViewById(R.id.btn_send);
		sendmessage.setOnClickListener(this);	
		sendmessage.setEnabled(true);
		messagebody.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(messagebody.getText().toString().trim())){
					sendmessage.setEnabled(false);
				}else{
					sendmessage.setEnabled(true);
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
				if(TextUtils.isEmpty(messagebody.getText().toString().trim())){
					sendmessage.setEnabled(false);
				}else{
					sendmessage.setEnabled(true);
				}
			}
		});
							
	}
	private void initChatListener() {
		// TODO Auto-generated method stub
		// 注册接收消息广播
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
		
	}
	private void roomType() {
		// TODO Auto-generated method stub
		if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.exitroom), R.color.white));
			rl_owner.setVisibility(View.VISIBLE);
			tv_tagname.setText(ChatRoomTypeUtil.getInstance().getTitle());
			User u=ChatRoomTypeUtil.getInstance().getUser();
			nikename.setText(u.getUsername());
			LoadImageUtil.imageLoader.displayImage(u.getImage(), ownerimg,LoadImageUtil.options);	
			tv_theme.setText(ChatRoomTypeUtil.getInstance().getTheme());
			topicId=ChatRoomTypeUtil.getInstance().getTopicId();
			//加入公开群聊
			easemobchatImp.getInstance().joingroup(toChatUsername);		
		
		}else if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.SINGLECHAT){
			rl_owner.setVisibility(View.GONE);
			tv_tagname.setText(ChatRoomTypeUtil.getInstance().getTitle());
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.deletemes), R.color.white));
		}
		conversation = EMChatManager.getInstance().getConversation(ChatRoomTypeUtil.getInstance().getHuanxingId());
		for(int i=0;i<conversation.getMsgCount();i++){
			EMMessage message =conversation.getMessage(i);
			TextMessageBody body=(TextMessageBody) message.getBody();
			String content=body.getMessage();			
			String   time =DateUtils.getTimestampString(new Date(message.getMsgTime())) ; 
				
			ChatMsgEntity entity=new ChatMsgEntity();
			entity.setMessage(content);
			entity.setDate(time);
			
			if(message.direct==EMMessage.Direct.SEND){
				entity.setMsgType(Constant.SELF);
			}else{
				entity.setMsgType(Constant.OTHER);
			}			
			msgList.add(entity);
		}	
		adapter=new ChatMsgViewAdapter(this, msgList);
		selflistview.setAdapter(adapter);
		selflistview.setSelection(selflistview.getCount() - 1);	
		// 把此会话的未读数置为0
		conversation.resetUnreadMsgCount();	
	}
	private void popupwindow() {
		// TODO Auto-generated method stub		
		
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(item.mTitle.equals(getResources().getString(R.string.exitroom))){
					easemobchatImp.getInstance().exitgroup(toChatUsername);
					finish();
				}else if(item.mTitle.equals(getResources().getString(R.string.deletemes))){
					easemobchatImp.getInstance().clearConversation(toChatUsername);
					msgList.clear();
					adapter.notifyDataSetChanged();
			//		ShowMessage(getResources().getString(R.string.deletechatmsg));
				}
					
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_back:
			// 把此会话的未读数置为0
			conversation.resetUnreadMsgCount();
			finish();
			break;
		
		case R.id.tv_seemember:
			if(checkNetworkState()){
				Intent intent=new Intent(ChatRoomActivity.this,ChatRoomListnikeActivity.class);
				intent.putExtra("topicId", topicId);			
				startActivity(intent);
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}
			
			break;

		case R.id.im_downview:
			titlePopup.show(v);

			break;
		case R.id.host_image:

			break;
		case R.id.btn_send:
			if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
				reply();
			}else{
				sendMessage();
			}			
			break;
		default:
			break;
		}
	}
	private void reply() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String content=messagebody.getText().toString().trim();
				
				SessionListener callback=new SessionListener();
				try {
					PeerUI.getInstance().getISessionManager().replyTopic(ChatRoomTypeUtil.getInstance().getTopicId(), content, callback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
					runOnUiThread(new Runnable() {
						public void run() {
							SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
							 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
							String   str   =   formatter.format(curDate);     
							ChatMsgEntity entity=new ChatMsgEntity();
							entity.setDate(str);
							entity.setMessage(content);
							entity.setMsgType(Constant.SELF);					
							msgList.add(entity);
							adapter.notifyDataSetChanged();
							selflistview.setSelection(selflistview.getCount() - 1);		
							messagebody.setText("");
						}
					});					
				}				
			}
		}).start();		
	}

	private void sendMessage() {
		// TODO Auto-generated method stub
		if(EMChatManager.getInstance().isConnected()){
			String content=messagebody.getText().toString().trim();				
			easemobchatImp.getInstance().sendMessage(content, Constant.SINGLECHAT, toChatUsername,imagurl);			
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   str   =   formatter.format(curDate);     
			ChatMsgEntity entity=new ChatMsgEntity();
			entity.setDate(str);
			entity.setMessage(content);
			entity.setMsgType(Constant.SELF);					
			msgList.add(entity);
			adapter.notifyDataSetChanged();
			selflistview.setSelection(selflistview.getCount() - 1);		
			messagebody.setText("");
		}else{
			ShowMessage(getResources().getString(R.string.broken_net));
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
//		InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 把此会话的未读数置为0
				conversation.resetUnreadMsgCount();
				finish();
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
		}
	}
	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 记得把广播给终结掉
			abortBroadcast();

			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			// 如果是群聊消息，获取到group id
			String image=null;
			try {
				 image=message.getStringAttribute(Constant.IMAGEURL);
			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			//获取到消息
			TextMessageBody txtBody = (TextMessageBody)message.getBody();
			String msg=txtBody.getMessage();
			
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   str   =   formatter.format(curDate);  
			ChatMsgEntity entity=new ChatMsgEntity();
			entity.setImage(image);
			entity.setDate(str);
			entity.setMessage(msg);
			entity.setMsgType(Constant.OTHER);					
			msgList.add(entity);
			adapter.notifyDataSetChanged();
			selflistview.setSelection(selflistview.getCount() - 1);

		}
	}
	public String getToChatUsername() {
		return toChatUsername;
	}
}
