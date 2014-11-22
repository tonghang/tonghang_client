package com.peer.activitymain;

import android.content.Intent;
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
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.constant.Constant;
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
	private String roomaddress,ownernike,theme,tagname,image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);
		init();
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
		
		selflistview=(ListView)findViewById(R.id.lv_selflistview);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);

		messagebody=(EditText)findViewById(R.id.et_sendmessage);


		sendmessage=(Button)findViewById(R.id.btn_send);
		sendmessage.setOnClickListener(this);	
		
		
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
			
			break;
		default:
			break;
		}
	}	
}
