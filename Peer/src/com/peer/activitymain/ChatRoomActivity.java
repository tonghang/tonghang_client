package com.peer.activitymain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import cn.sharesdk.onekeyshare.OnekeyShare;
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
import com.peer.adapter.HistoryMsgAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.entity.ChatMsgEntity;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.ManagerActivity;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.FxService;
import com.peer.widgetutil.LoadImageUtil;

public class ChatRoomActivity extends BasicActivity {
	private List<User> list;
	private TitlePopup titlePopup;
	private LinearLayout back;
	private RelativeLayout rl_owner;
	private Button sendmessage;
	private EditText messagebody;
	private ImageView downwindow,ownerimg;
	private TextView tv_tagname,nikename,tv_theme,sharelogo;
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
	private List<Map> Mulmlist;
	
	private String imagurl=null;
	private String userid=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);
		LoadImageUtil.initImageLoader(this);
		init();
		roomType();
		initChatListener();	
		//单聊时ChatRoomTypeUtil.getInstance().getUser();toChatUsername=ChatRoomTypeUtil.getInstance().getHuanxingId();
		//群聊时ChatRoomTypeUtil.getInstance().getTopic();toChatUsername=ChatRoomTypeUtil.getInstance().getHuanxingId();
	}	
	
	private void init() {
		// TODO Auto-generated method stub
		try {
			imagurl=PeerUI.getInstance().getISessionManager().getImagUrL();
			userid=PeerUI.getInstance().getISessionManager().getUserId();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		rl_owner=(RelativeLayout)findViewById(R.id.host_imfor);		
		popupwindow();
		ScrollView mScrollView = (ScrollView)findViewById(R.id.scrollContent);  
		mScrollView.setVerticalScrollBarEnabled(false);  
		mScrollView.setHorizontalScrollBarEnabled(false);
		downwindow=(ImageView)findViewById(R.id.im_downview);
		downwindow.setOnClickListener(this);	
		
		ownerimg=(ImageView)findViewById(R.id.host_image);
		ownerimg.setOnClickListener(this);
		
		tv_tagname=(TextView)findViewById(R.id.tv_tagname);		
		nikename=(TextView)findViewById(R.id.tv_nikename);
		tv_theme=(TextView)findViewById(R.id.theme_chat);
		sharelogo=(TextView)findViewById(R.id.tv_share);
		sharelogo.setOnClickListener(this);
		
		selflistview=(ListView)findViewById(R.id.lv_chat);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);

		messagebody=(EditText)findViewById(R.id.et_sendmessage);
		hideKeyboard();

		sendmessage=(Button)findViewById(R.id.btn_send);
		sendmessage.setOnClickListener(this);	
		sendmessage.setEnabled(false);
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
			rl_owner.setVisibility(View.VISIBLE);
			if(!ChatRoomTypeUtil.getInstance().isIsowner()){
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.lookformember), R.color.white));			
			}else{
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.exitroom), R.color.white));
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.lookformember), R.color.white));				
			}
			Intent intent=getIntent();
			if(intent.getStringExtra(Constant.FROMFLOAT)!=null&&intent.getStringExtra(Constant.FROMFLOAT).equals(Constant.FROMFLOAT)){
				tv_tagname.setText(intent.getStringExtra(Constant.TAGNAME));
				
				nikename.setText(intent.getStringExtra(Constant.OWNERNIKE));
				LoadImageUtil.imageLoader.displayImage(intent.getStringExtra(Constant.IMAGE), ownerimg,LoadImageUtil.options);	
				tv_theme.setText(intent.getStringExtra(Constant.THEME));
				topicId=intent.getStringExtra(Constant.TOPICID);				
			}else{
				tv_tagname.setText(ChatRoomTypeUtil.getInstance().getTopic().getLabel_name());
				User u=ChatRoomTypeUtil.getInstance().getTopic().getUser();
				nikename.setText(u.getUsername());
				LoadImageUtil.imageLoader.displayImage(u.getImage(), ownerimg,LoadImageUtil.options);	
				tv_theme.setText(ChatRoomTypeUtil.getInstance().getTopic().getSubject());
				topicId=ChatRoomTypeUtil.getInstance().getTopic().getTopicid();
			}
			//将浮动窗口取消并保存在配置文件中
			Intent serviceintent = new Intent(ChatRoomActivity.this, FxService.class);
			stopService(serviceintent);			
			LocalStorage.saveBoolean(ChatRoomActivity.this, Constant.ISFLOAT, false);
			//加入公开群聊
			easemobchatImp.getInstance().joingroup(toChatUsername);
			
			ReplieTask task=new ReplieTask();
			task.execute(ChatRoomTypeUtil.getInstance().getTopic().getTopicid());
			conversation = EMChatManager.getInstance().getConversation(ChatRoomTypeUtil.getInstance().getTopic().getHuangxin_group_id());
			conversation.resetUnreadMsgCount();		
		}else if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.SINGLECHAT){
			rl_owner.setVisibility(View.GONE);
			tv_tagname.setText(ChatRoomTypeUtil.getInstance().getUser().getUsername());
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.deletemes), R.color.white));
			conversation = EMChatManager.getInstance().getConversation(ChatRoomTypeUtil.getInstance().getUser().getHuangxin_username());
			for(int i=0;i<conversation.getMsgCount();i++){
				EMMessage message =conversation.getMessage(i);				
				
				TextMessageBody body=(TextMessageBody) message.getBody();
				String content=body.getMessage();			
				String   time =DateUtils.getTimestampString(new Date(message.getMsgTime())) ; 
					
				ChatMsgEntity entity=new ChatMsgEntity();
				entity.setMessage(content);
				entity.setDate(time);
				try {
					entity.setImage(message.getStringAttribute(Constant.IMAGEURL));
					entity.setUserId(message.getStringAttribute(Constant.USERID));
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		
	}
	private void popupwindow() {
		// TODO Auto-generated method stub		
		
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(!checkNetworkState()){
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}else{
					if(item.mTitle.equals(getResources().getString(R.string.exitroom))){
						easemobchatImp.getInstance().exitgroup(toChatUsername);
						finish();
					}else if(item.mTitle.equals(getResources().getString(R.string.deletemes))){
						easemobchatImp.getInstance().clearConversation(toChatUsername);
						msgList.clear();
						adapter.notifyDataSetChanged();
					}else if(item.mTitle.equals(getResources().getString(R.string.lookformember))){
						Intent intent=new Intent(ChatRoomActivity.this,ChatRoomListnikeActivity.class);
						intent.putExtra("topicId", topicId);
						intent.putExtra("groupId", ChatRoomTypeUtil.getInstance().getTopic().getHuangxin_group_id());
						startActivity(intent);			
					}
				}				
					
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			// 把此会话的未读数置为0	
			if(checkNetworkState()){
				startfloatView();
			}else{
				finish();
			}			
			break;
		
		case R.id.tv_share:
			showShare();			
			break;

		case R.id.im_downview:
			titlePopup.show(v);

			break;
		case R.id.host_image:
			FriendsTask task=new FriendsTask();
			task.execute();
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
	 private void showShare() {
		  OnekeyShare oks = new OnekeyShare();
		  //关闭sso授权
//		  oks.disableSSOWhenAuthorize(); 

		 // 分享时Notification的图标和文字
		  oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		  // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		  oks.setTitle(getString(R.string.share));
		  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		  oks.setTitleUrl("http://sharesdk.cn");
		  // text是分享文本，所有平台都需要这个字段
		  oks.setText("我参与了"+tv_theme.getText().toString()+"的讨论。各位同行们：来“同行”APP吧，我们一起交流行业信息，没准还能找到更好的工作。下载地址：http://114.215.143.83:3000/download/peer.apk");
		  // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		  oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		  // url仅在微信（包括好友和朋友圈）中使用
		  oks.setUrl("http://sharesdk.cn");
		  // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		  oks.setComment("我是测试评论文本");
		  // site是分享此内容的网站名称，仅在QQ空间使用
		  oks.setSite(getString(R.string.app_name));
		  // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		  oks.setSiteUrl("http://sharesdk.cn");

		 // 启动分享GUI
		  oks.show(this);
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
					PeerUI.getInstance().getISessionManager().replyTopic(ChatRoomTypeUtil.getInstance().getTopic().getTopicid(), content, callback);					
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
							entity.setImage(imagurl);
							entity.setMessage(content);
							entity.setUserId(userid);
							entity.setMsgType(Constant.SELF);					
							msgList.add(entity);
							adapter.notifyDataSetChanged();
							selflistview.setSelection(selflistview.getCount() - 1);		
							messagebody.setText("");
						}
					});					
				}else{
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ShowMessage(getResources().getString(R.string.broken_net));
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
			//环信发送消息，携带消息内容，自己头像，自己Id
			easemobchatImp.getInstance().sendMessage(content, Constant.SINGLECHAT, toChatUsername,imagurl,userid);			
			
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   str   =   formatter.format(curDate);     
			ChatMsgEntity entity=new ChatMsgEntity();
			entity.setDate(str);
			entity.setImage(imagurl);
			entity.setUserId(userid);
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
			  startfloatView();
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
	}
	public void startfloatView(){
		if(ChatRoomTypeUtil.getInstance().isIsowner()&&ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
			if(!LocalStorage.getBoolean(ChatRoomActivity.this, Constant.ISFLOAT)){
				Intent intentfloat = new Intent(ChatRoomActivity.this, FxService.class);				
				User u=ChatRoomTypeUtil.getInstance().getUser();			
				intentfloat.putExtra(Constant.IMAGE, u.getImage());
				intentfloat.putExtra(Constant.OWNERNIKE, u.getUsername());
				intentfloat.putExtra(Constant.THEME, ChatRoomTypeUtil.getInstance().getTopic().getSubject());			
				intentfloat.putExtra(Constant.TAGNAME, ChatRoomTypeUtil.getInstance().getTopic().getLabel_name());
				intentfloat.putExtra(Constant.USERID, u.getUserid());
				intentfloat.putExtra(Constant.ROOMID, ChatRoomTypeUtil.getInstance().getTopic().getHuangxin_group_id());
				intentfloat.putExtra(Constant.TOPICID,  ChatRoomTypeUtil.getInstance().getTopic().getTopicid());
				intentfloat.putExtra(Constant.FROMFLOAT, "float");
				startService(intentfloat);
				finish();
				LocalStorage.saveBoolean(ChatRoomActivity.this, Constant.ISFLOAT, true);
			}else{
				finish();
			}
		}else if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.SINGLECHAT){
			// 把此会话的未读数置为0
			conversation.resetUnreadMsgCount();
			finish();			
		}else{
			easemobchatImp.getInstance().exitgroup(toChatUsername);
			finish();
		}
	}
	private class ReplieTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			try {
				Mulmlist=PeerUI.getInstance().getISessionManager().TopicReplies(params[0], callback);
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
				for(int i=0;i<Mulmlist.size();i++){
					ChatMsgEntity entity=new ChatMsgEntity();
					entity.setMessage((String)Mulmlist.get(i).get("replybody"));
					entity.setDate((String)Mulmlist.get(i).get(Constant.TIME));
					User user=(User) Mulmlist.get(i).get(Constant.USER);
					entity.setUserId(user.getUserid());
					if(user.getUserid().equals(userid)){
						entity.setMsgType(Constant.SELF);
					}else{
						entity.setMsgType(Constant.OTHER);
					}	
					entity.setImage(user.getImage());
					msgList.add(entity);					
				}
				adapter=new ChatMsgViewAdapter(ChatRoomActivity.this, msgList);
				selflistview.setAdapter(adapter);
				selflistview.setSelection(selflistview.getCount() - 1);	
				
			}
						
		}
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
	private class FriendsTask extends AsyncTask<Void, Void, List<User>>{

		@Override
		protected List<User> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub       
            try {
            	list=PeerUI.getInstance().getISessionManager().myFriends();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
			return list;
		}
		@Override
		protected void onPostExecute(List<User> result) {
			// TODO Auto-generated method stub
			if(ChatRoomTypeUtil.getInstance().getUser().getUserid().equals(userid)){			
				ToMypersonalpage();
			}else if(!result.isEmpty()){				
//					PersonpageUtil.getInstance().setPersonid(ChatRoomTypeUtil.getInstance().getUser().getUserid());
//					for(int i=0;i<result.size();i++){
//						if(ChatRoomTypeUtil.getInstance().getUser().getUserid().equals(result.get(i).getUserid())){
//							PersonpageUtil.getInstance().setPersonpagetype(Constant.FRIENDSPAGE);
//							break;
//						}else{
//							PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
//						}
//					}
					PersonpageUtil.getInstance().setUser(ChatRoomTypeUtil.getInstance().getUser());
					Intent topersonalpage=new Intent(ChatRoomActivity.this,PersonalPageActivity.class);
					startActivity(topersonalpage);				
			}else{
//				PersonpageUtil.getInstance().setPersonid(ChatRoomTypeUtil.getInstance().getUser().getUserid());
//				PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
				PersonpageUtil.getInstance().setUser(ChatRoomTypeUtil.getInstance().getUser());
				Intent topersonalpage=new Intent(ChatRoomActivity.this,PersonalPageActivity.class);
				startActivity(topersonalpage);	
				
			}
		}
	}
	private void ToMypersonalpage() {
		// TODO Auto-generated method stub
		String huangxin_username=null;
		List<String> labels=null;
		try {
			huangxin_username=PeerUI.getInstance().getISessionManager().getHuanxingUser();
			labels=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		LocalStorage.getString(ChatRoomActivity.this, Constant.EMAIL);
		UserDao userdao=new UserDao(ChatRoomActivity.this);
		UserBean userbean=userdao.findOne(LocalStorage.getString(ChatRoomActivity.this, Constant.EMAIL));
		User user=new User();
		user.setEmail(userbean.getEmail());
		user.setBirthday(userbean.getAge());
		user.setCity(userbean.getCity());
		user.setSex(userbean.getSex());
		user.setImage(userbean.getImage());
		user.setUsername(userbean.getNikename());
		user.setUserid(userid);
		user.setHuangxin_username(huangxin_username);
		user.setLabels(labels);
		PersonpageUtil.getInstance().setUser(user);
		Intent topersonalpage=new Intent(ChatRoomActivity.this,PersonalPageActivity.class);
		startActivity(topersonalpage);
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
			String fromuserid=null;
			try {
				if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
					image=Constant.WEB_SERVER_ADDRESS+message.getStringAttribute(Constant.IMAGEURL);
				}else{
					 image=message.getStringAttribute(Constant.IMAGEURL);
				}				
				fromuserid=message.getStringAttribute(Constant.USERID);
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
			entity.setUserId(fromuserid);
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
