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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.peer.R;
import com.peer.IMimplements.RingLetterImp;
import com.peer.activity.BasicActivity;
import com.peer.adapter.ChatMsgViewAdapter;
import com.peer.constant.Constant;
import com.peer.entity.ChatMsgEntity;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.util.ChatRoomTypeUtil;

public class ChatRoomActivity extends BasicActivity {
	private TitlePopup titlePopup;
	private LinearLayout back;
	private RelativeLayout rl_owner;
	private Button sendmessage;
	private EditText messagebody;
	private ImageView downwindow;
	private TextView tv_tagname,nikename,tv_theme,tv_seemember;
	boolean isFirst=true;
	private ListView selflistview;
	private ChatMsgViewAdapter adapter;
	
	private List<ChatMsgEntity> msgList=new ArrayList<ChatMsgEntity>();
	
	private NewMessageBroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);
		init();
		initChatListener();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		rl_owner=(RelativeLayout)findViewById(R.id.host_imfor);
		if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.exitroom), R.color.white));
			rl_owner.setVisibility(View.VISIBLE);
		}else if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.SINGLECHAT){
			rl_owner.setVisibility(View.GONE);
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.deletemes), R.color.white));
		}
		popupwindow();
		
		ScrollView mScrollView = (ScrollView)findViewById(R.id.scrollContent);  
		mScrollView.setVerticalScrollBarEnabled(false);  
		mScrollView.setHorizontalScrollBarEnabled(false);
		downwindow=(ImageView)findViewById(R.id.im_downview);
		downwindow.setOnClickListener(this);
		
		tv_tagname=(TextView)findViewById(R.id.tv_tagname);
//		tv_tagname.setText(tagname);
		
		nikename=(TextView)findViewById(R.id.tv_nikename);
//		nikename.setText(ownernike);
		
		tv_theme=(TextView)findViewById(R.id.theme_chat);
//		tv_theme.setText(theme);
		
		tv_seemember=(TextView)findViewById(R.id.tv_seemember);
		tv_seemember.setOnClickListener(this);
		
		selflistview=(ListView)findViewById(R.id.lv_chat);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);

		messagebody=(EditText)findViewById(R.id.et_sendmessage);


		sendmessage=(Button)findViewById(R.id.btn_send);
		sendmessage.setOnClickListener(this);	
		
		adapter=new ChatMsgViewAdapter(this, msgList);
		selflistview.setAdapter(adapter);
		
		
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
	private void popupwindow() {
		// TODO Auto-generated method stub		
		
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(item.mTitle.equals(getResources().getString(R.string.exitroom))){
					finish();
				}else if(item.mTitle.equals(getResources().getString(R.string.deletemes))){
					ShowMessage(getResources().getString(R.string.deletechatmsg));
				}
					
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_seemember:
			Intent intent=new Intent(ChatRoomActivity.this,ChatRoomListnikeActivity.class);
			startActivity(intent);
			break;

		case R.id.im_downview:
			titlePopup.show(v);

			break;
		case R.id.host_image:

			break;
		case R.id.btn_send:
			String content=messagebody.getText().toString().trim();			
			RingLetterImp.getInstance().sendMessage(content, Constant.SINGLECHAT, "yzq");
			
			
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
			break;
		default:
			break;
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
			
			
			//获取到消息
			TextMessageBody txtBody = (TextMessageBody)message.getBody();
			String msg=txtBody.getMessage();
			
			
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   str   =   formatter.format(curDate);  
			ChatMsgEntity entity=new ChatMsgEntity();
			entity.setDate(str);
			entity.setMessage(msg);
			entity.setMsgType(Constant.OTHER);					
			msgList.add(entity);
			adapter.notifyDataSetChanged();
			selflistview.setSelection(selflistview.getCount() - 1);

		}
	}
}
