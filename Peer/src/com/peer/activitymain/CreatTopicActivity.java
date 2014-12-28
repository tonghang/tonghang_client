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
import com.peer.util.ChatRoomTypeUtil;


public class CreatTopicActivity extends BasicActivity {
	private TextView title;
	private Button creattopic;
	private LinearLayout back;
	private RadioGroup rg_lables;
	private RadioButton r1,r2,r3,r4,r5;
	private View v1,v2,v3,v4,v5;
	private EditText topic;
	private List<String> list;
	
	private boolean isselect=false;
	private String selectlabel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creattopic);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		try {
			list=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		topic=(EditText)findViewById(R.id.et_topic);
		creattopic=(Button)findViewById(R.id.bt_creattopic);
		creattopic.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.createtopic));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		rg_lables=(RadioGroup)findViewById(R.id.rg_lables);		
		rg_lables.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton)findViewById(checkedId);
				selectlabel=tempButton.getText().toString();
				isselect=true;
//				System.out.println("选中的标签是："+tempButton.getText().toString());
			}
		});
		addRadiobutton();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_creattopic:
			if(!TextUtils.isEmpty(topic.getText().toString().trim())&&isselect){
				CreatTopicTask task=new CreatTopicTask();
				task.execute(selectlabel,topic.getText().toString().trim());
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
		}
	}
	private void addRadiobutton(){
		if(list.size()==2){
			r1=(RadioButton)findViewById(R.id.label1);
			v1=(View)findViewById(R.id.v1);
			r1.setText(list.get(0));
			
			r2=(RadioButton)findViewById(R.id.label2);
			v2=(View)findViewById(R.id.v2);
			r2.setText(list.get(1));
		}else if(list.size()==3){
			r1=(RadioButton)findViewById(R.id.label1);
			v1=(View)findViewById(R.id.v1);
			r1.setText(list.get(0));
			
			r2=(RadioButton)findViewById(R.id.label2);
			v2=(View)findViewById(R.id.v2);
			r2.setText(list.get(1));
			
			r3=(RadioButton)findViewById(R.id.label3);
			v3=(View)findViewById(R.id.v3);
			r3.setText(list.get(2));
			
			r3.setVisibility(View.VISIBLE);
			v3.setVisibility(View.VISIBLE);
		}else if(list.size()==4){
			r1=(RadioButton)findViewById(R.id.label1);
			v1=(View)findViewById(R.id.v1);
			r2=(RadioButton)findViewById(R.id.label2);
			v2=(View)findViewById(R.id.v2);
			r3=(RadioButton)findViewById(R.id.label3);
			v3=(View)findViewById(R.id.v3);
			r4=(RadioButton)findViewById(R.id.label4);
			v4=(View)findViewById(R.id.v4);
			r1.setText(list.get(0));
			r2.setText(list.get(1));
			r3.setText(list.get(2));
			r4.setText(list.get(3));
			v3.setVisibility(View.VISIBLE);
			v4.setVisibility(View.VISIBLE);
		}else if(list.size()==5){
			r1=(RadioButton)findViewById(R.id.label1);
			v1=(View)findViewById(R.id.v1);
			r2=(RadioButton)findViewById(R.id.label2);
			v2=(View)findViewById(R.id.v2);
			r3=(RadioButton)findViewById(R.id.label3);
			v3=(View)findViewById(R.id.v3);
			r4=(RadioButton)findViewById(R.id.label4);
			v4=(View)findViewById(R.id.v4);
			r5=(RadioButton)findViewById(R.id.label5);
			v5=(View)findViewById(R.id.v5);
			r1.setText(list.get(0));
			r2.setText(list.get(1));
			r3.setText(list.get(2));
			r4.setText(list.get(3));
			r5.setText(list.get(4));
			v3.setVisibility(View.VISIBLE);
			v4.setVisibility(View.VISIBLE);
			v5.setVisibility(View.VISIBLE);
		}
	}
}
