package com.peer.activitymain;

import java.util.List;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.fragment.FriendsFragment;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.util.AutoWrapLinearLayout;
import com.peer.util.AutoWrapRadioGroup;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.ManagerActivity;
import com.peer.util.PersonpageUtil;
import com.peer.util.SearchUtil;
import com.peer.widgetutil.LoadImageUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PersonalPageActivity extends BasicActivity {
	private ImageView personhead,delete;
	private TextView nikename,title,topic_whose,acount,city,sex;
	private RadioButton skill;
	private RelativeLayout topic_click;
	private LinearLayout back,bottomline,content;
	private Button send,addfriend;

	private AutoWrapRadioGroup tagContainer;
	private TitlePopup titlePopup;
	private User userpage;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage);
		init();
		LoadImageUtil.initImageLoader(this);
		if(PersonpageUtil.getInstance().isShouldRefresh()){
			//当传递过来的用户数据不全时，根据用户ID去获取完整的用户信息
			PersonalTask task=new PersonalTask();
			task.execute(PersonpageUtil.getInstance().getUser().getUserid());
		}else{
			User user=PersonpageUtil.getInstance().getUser();		
			userpage=user;
			LoadImageUtil.imageLoader.displayImage(user.getImage(), personhead, LoadImageUtil.options);				
			nikename.setText(user.getUsername());
			acount.setText(user.getEmail());
			city.setText(user.getCity());
			sex.setText(user.getSex());
			for(int i=0;i<user.getLabels().size();i++){
				String tag=user.getLabels().get(i);					

				skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tagContainer, false);
				skill.setHeight((int)getResources().getDimension(R.dimen.hight));
				skill.setTextSize(20);
				skill.setTextColor(getResources().getColor(R.color.white));
				int pading=(int)getResources().getDimension(R.dimen.pading);
				skill.setText(tag);
				skill.setTag(""+i);
				tagContainer.addView(skill);
			}
			ViewType();
		}
	}
	private void init() {
		// TODO Auto-generated method stub	
		topic_whose=(TextView)findViewById(R.id.tv_topic);
		title=(TextView)findViewById(R.id.tv_title);
		acount=(TextView)findViewById(R.id.personcount);
		city=(TextView)findViewById(R.id.city);
		sex=(TextView)findViewById(R.id.sex);
		
		delete=(ImageView)findViewById(R.id.im_downview);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		delete.setOnClickListener(this);
		
		topic_click=(RelativeLayout)findViewById(R.id.rl_topic);
		topic_click.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		nikename=(TextView)findViewById(R.id.personnike);
		personhead=(ImageView)findViewById(R.id.personhead);
		bottomline=(LinearLayout)findViewById(R.id.ll_personpagebottom);
		content=(LinearLayout)findViewById(R.id.contentauto);
		tagContainer = (AutoWrapRadioGroup) findViewById(R.id.tag_container);
		send=(Button)findViewById(R.id.send);
		addfriend=(Button)findViewById(R.id.addfriends);
		tagContainer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton)findViewById(checkedId);
				String selectlabel=tempButton.getText().toString();
				SearchUtil.getInstance().setSearchname(selectlabel);
				SearchUtil.getInstance().setSearchtype(Constant.USERBYLABEL);
				SearchUtil.getInstance().setCallbacklabel(selectlabel);
				Intent intent=new Intent(PersonalPageActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();		
	}
	private void ViewType() {
		// TODO Auto-generated method stub			
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		String userid=null;
		try {
			userid = PeerUI.getInstance().getISessionManager().getUserId();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(userid.equals(PersonpageUtil.getInstance().getUser().getUserid())){
			MypersonPage();
		}else if(PersonpageUtil.getInstance().getUser().isIs_friends()){
			friendsPersonPage();
		}else{
			unfriendsPersonPage();
		}
						
	}
	private void friendsPersonPage(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		String userid=null;
		
		delete.setVisibility(View.VISIBLE);
		send.setVisibility(View.GONE);
		addfriend.setVisibility(View.GONE);
		
		titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.deletefriends), R.color.white));
		popupwindow();
		if(sex.getText().toString().equals("男")){
			topic_whose.setText(getResources().getString(R.string.topic_other));
			title.setText(getResources().getString(R.string.personalpage_other));
		}else{
			topic_whose.setText(getResources().getString(R.string.topic_nvother));
			title.setText(getResources().getString(R.string.personalpage_nvother));
		}	
		Button bt3=new Button(this);
		bt3.setText(getResources().getString(R.string.sendmsg));
		bt3.setTextColor(getResources().getColor(R.color.white));
		bt3.setBackgroundResource(R.drawable.select_personal);
		bt3.setLayoutParams(params);
		bottomline.addView(bt3);
		bt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	/*			ChatRoomTypeUtil.getInstance().setUserId(PersonpageUtil.getInstance().getUser().getUserid());
				ChatRoomTypeUtil.getInstance().setHuanxingId(PersonpageUtil.getInstance().getUser().getHuangxin_username());
				ChatRoomTypeUtil.getInstance().setTitle(PersonpageUtil.getInstance().getUser().getUsername());					
	*/
				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
				ChatRoomTypeUtil.getInstance().setUser(userpage);
				Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
				startActivity(intent);
			}
		});
	}
	private void unfriendsPersonPage(){
		if(sex.getText().toString().equals("男")){
			topic_whose.setText(getResources().getString(R.string.topic_other));
			title.setText(getResources().getString(R.string.personalpage_other));
		}else{
			topic_whose.setText(getResources().getString(R.string.topic_nvother));
			title.setText(getResources().getString(R.string.personalpage_nvother));
		}			
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
/*				ChatRoomTypeUtil.getInstance().setUserId(PersonpageUtil.getInstance().getUser().getUserid());
				ChatRoomTypeUtil.getInstance().setHuanxingId(PersonpageUtil.getInstance().getUser().getHuangxin_username());
				ChatRoomTypeUtil.getInstance().setTitle(PersonpageUtil.getInstance().getUser().getUsername());
				
*/				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);				
				ChatRoomTypeUtil.getInstance().setUser(userpage);				
				Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
				startActivity(intent);
				ManagerActivity.getAppManager().finishActivity();
			}
		});
		addfriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
				intent.putExtra("userId", userpage.getUserid());
				intent.putExtra("image", userpage.getImage());
				intent.putExtra("nike", userpage.getUsername());
				intent.putExtra("email", userpage.getEmail());
				startActivity(intent);
				ManagerActivity.getAppManager().finishActivity();
			}
		});
	}
	public void MypersonPage(){
		topic_whose.setText(getResources().getString(R.string.topic_owen));
		title.setText(getResources().getString(R.string.personalpage_own));
		bottomline.setVisibility(View.INVISIBLE);
		if(!checkNetworkState()){
			String email=LocalStorage.getString(PersonalPageActivity.this, Constant.EMAIL);
			UserDao userdao=new UserDao(PersonalPageActivity.this);
			UserBean user=userdao.findOne(email);
			LoadImageUtil.imageLoader.displayImage(user.getImage(), personhead, LoadImageUtil.options);				
			nikename.setText(user.getNikename());
			acount.setText(user.getEmail());
			city.setText(user.getCity());
			sex.setText(user.getSex());
			List<String> lables=null;
			try {
				lables = PeerUI.getInstance().getISessionManager().getLabels();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<lables.size();i++){
				String tag=lables.get(i);		
				skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tagContainer, false);
				skill.setHeight((int)getResources().getDimension(R.dimen.hight));
				skill.setTextSize(20);
				skill.setTextColor(getResources().getColor(R.color.white));
				int pading=(int)getResources().getDimension(R.dimen.pading);
				skill.setText(tag);
				skill.setTag(""+i);
				tagContainer.addView(skill);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_topic:
			if(checkNetworkState()){
				Intent intent=new Intent(PersonalPageActivity.this,TopicActivity.class);
				intent.putExtra("userId", userpage.getUserid());
				intent.putExtra("image", userpage.getImage());
				intent.putExtra("nike", userpage.getUsername());
				intent.putExtra("email", userpage.getEmail());			
				startActivity(intent);
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}						
			break;
		case R.id.im_downview:
			titlePopup.show(v);
			break;
		default:
			break;
		}
	}
	private void popupwindow() {
		// TODO Auto-generated method stub		
		
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub	
				if(item.mTitle.equals(getResources().getString(R.string.deletefriends))){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							SessionListener callback=new SessionListener();
							try {
								PeerUI.getInstance().getISessionManager().deleteFriend(userpage.getUserid(), callback);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
								FriendsFragment.refreshhandle.sendEmptyMessage(Constant.REFRESHHANDLE);
								finish();
							}
						}
					}).start();
				}
			}
		});
	}
	private class PersonalTask extends AsyncTask<String, Void, User>{

		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
			User user=null;
			try {
				user=PeerUI.getInstance().getISessionManager().personalPage(params[0], callback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;
		}
		@Override
		protected void onPostExecute(User user) {
			// TODO Auto-generated method stub
			if(user!=null){
				userpage=user;
				LoadImageUtil.imageLoader.displayImage(user.getImage(), personhead, LoadImageUtil.options);				
				nikename.setText(user.getUsername());
				acount.setText(user.getEmail());
				city.setText(user.getCity());
				sex.setText(user.getSex());
				for(int i=0;i<user.getLabels().size();i++){
					String tag=user.getLabels().get(i);					

					skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tagContainer, false);
					skill.setHeight((int)getResources().getDimension(R.dimen.hight));
					skill.setTextSize(20);
					skill.setTextColor(getResources().getColor(R.color.white));
					int pading=(int)getResources().getDimension(R.dimen.pading);
					skill.setText(tag);
					skill.setTag(""+i);
					tagContainer.addView(skill);
				}
				ViewType();
			}
		}
		
	}
}
