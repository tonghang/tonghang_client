package com.peer.activitymain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.moments.WechatMoments.ShareParams;

import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.BasicActivity;
import com.peer.activity.SettingActivity;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.AutoWrapRadioGroup;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.ManagerActivity;

public class CreatTopicActivity extends BasicActivity {
	private TextView title;
	private Button creattopic;
	private LinearLayout back;
	private EditText topic;
	private List<String> list;
	private AutoWrapRadioGroup tagContainer;	
	private boolean isselect=false;
	private String selectlabel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creattopic);
		getlables();
		init();
	}
	private void getlables() {
		// TODO Auto-generated method stub
		try {
			list=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	private void init() {
		// TODO Auto-generated method stub	
		creattopic=(Button)findViewById(R.id.bt_creattopic);
		creattopic.setEnabled(false);
		creattopic.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.createtopic));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		tagContainer = (AutoWrapRadioGroup) findViewById(R.id.tag_container);	
		for(int i=0;i<list.size();i++){
			RadioButton rb=(RadioButton)getLayoutInflater().inflate(R.layout.skillradio, tagContainer, false);
			rb.setHeight((int)getResources().getDimension(R.dimen.hight));
			rb.setText(list.get(i));
			rb.setTextSize(18);
			tagContainer.addView(rb);
		}	
		tagContainer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton)findViewById(checkedId);
				selectlabel=tempButton.getText().toString();
				isselect=true;
				System.out.println("选中的标签是："+tempButton.getText().toString());
			}
		});
		topic=(EditText)findViewById(R.id.et_topic);
		topic.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(isselect&&TextUtils.isEmpty(topic.getText().toString().trim())){
					creattopic.setEnabled(false);
				}else{
					creattopic.setEnabled(true);
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
				if(isselect&&!TextUtils.isEmpty(topic.getText().toString().trim())){
					creattopic.setEnabled(true);
				}else{
					creattopic.setEnabled(false);
				}				
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_creattopic:
			if(checkNetworkState()){
				ShareDialog();				
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}
			break;

		default:
			break;
		}
	}
	public void ShareDialog(){
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.sharedailog))  
		.setMessage(getResources().getString(R.string.isshare)) .setNegativeButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialoginterface, int i) {
				// TODO Auto-generated method stub
				CreatTopicTask task=new CreatTopicTask();
				task.execute(selectlabel,topic.getText().toString().trim());
			}
		}) 
		 .setPositiveButton(getResources().getString(R.string.sharesure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){            	 
              
              OnekeyShare oks = new OnekeyShare();
      		oks.setNotification(R.drawable.logo, CreatTopicActivity.this.getString(R.string.app_name));
      		//不同平台的分享参数，请看文档		
      		oks.setText("我在同行中创建了关于"+topic.getText().toString()+"的话题");
      		oks.setSilent(true);
      		oks.setDialogMode();
      		oks.disableSSOWhenAuthorize();
      	
      		oks.setPlatform(WechatMoments.NAME);
      		oks.setCallback(new PlatformActionListener() {
				
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
					// TODO Auto-generated method stub
					CreatTopicTask task=new CreatTopicTask();
					task.execute(selectlabel,topic.getText().toString().trim());
				}
				
				@Override
				public void onCancel(Platform arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
      		oks.show(CreatTopicActivity.this);
             }
		 })
		 .show();  
	}
	private class CreatTopicTask extends AsyncTask<String, String, List>{

		@Override
		protected List<String> doInBackground(String... paramer) {
			// TODO Auto-generated method stub				
			List<String> list=new ArrayList<String>();
			String groupid=null;
			list.add(paramer[0]);
			list.add(paramer[1]);			
			try {
				groupid=PeerUI.getInstance().getISessionManager().creatTopic(paramer[0], paramer[1]);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(groupid);
			return list;
		}
		@Override
		protected void onPostExecute(List result) {
			// TODO Auto-generated method stub
			topic.setText("");
			ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);				
			ChatRoomTypeUtil.getInstance().setTitle((String)result.get(0));
			ChatRoomTypeUtil.getInstance().setTheme((String)result.get(1));
			ChatRoomTypeUtil.getInstance().setHuanxingId((String)result.get(2));
			User u=new User();
			try {
				u.setImage(PeerUI.getInstance().getISessionManager().getImagUrL());
				u.setUsername(PeerUI.getInstance().getISessionManager().getUserName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			ChatRoomTypeUtil.getInstance().setUser(u);	
			Intent intent=new Intent(CreatTopicActivity.this,ChatRoomActivity.class);
			startActivity(intent);
			ManagerActivity.getAppManager().finishActivity(CreatTopicActivity.this);
		}
	}
}
