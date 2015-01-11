package com.peer.activitymain;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.util.AutoWrapLinearLayout;
import com.peer.util.AutoWrapRadioGroup;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.ManagerActivity;


public class CreatTopicActivity extends BasicActivity {
	private TextView title;
	private Button creattopic;
	private LinearLayout back;
	private RadioGroup rg_lables;
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
		if(LocalStorage.getBoolean(this, "istestui")){
			list=new ArrayList<String>();
			list.add("美食");
			list.add("java");
		}else{
			try {
				list=PeerUI.getInstance().getISessionManager().getLabels();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void init() {
		// TODO Auto-generated method stub		
		topic=(EditText)findViewById(R.id.et_topic);
		creattopic=(Button)findViewById(R.id.bt_creattopic);
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
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_creattopic:
			if(LocalStorage.getBoolean(this, "istestui")){
				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
				Intent intent=new Intent(CreatTopicActivity.this,ChatRoomActivity.class);
				startActivity(intent);
			}else{
				if(!TextUtils.isEmpty(topic.getText().toString().trim())&&isselect){
					CreatTopicTask task=new CreatTopicTask();
					task.execute(selectlabel,topic.getText().toString().trim());
				}	
			}
			break;

		default:
			break;
		}
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
