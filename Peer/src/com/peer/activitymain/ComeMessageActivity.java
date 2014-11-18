package com.peer.activitymain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.ComeMessageAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComeMessageActivity extends BasicActivity{
	private TextView tv_come,showmessgenum;
	private ImageView backcome;
	private LinearLayout find,come,my,friends;
	private ListView ListView_come;
	public static final String KEY_MESSAGE = "message";
	public static boolean isForeground = true;
	
	List<Map> list=new ArrayList<Map>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comemessage);
		initdata();
		init();		
	}
	
	private void init() {
		// TODO Auto-generated method stub		
		tv_come=(TextView)findViewById(R.id.tv_come);
		tv_come.setTextColor(getResources().getColor(R.color.bottomtextblue));
		
		find=(LinearLayout)findViewById(R.id.ll_find);		
		come=(LinearLayout)findViewById(R.id.ll_come);
		my=(LinearLayout)findViewById(R.id.ll_my);
		friends=(LinearLayout)findViewById(R.id.ll_friends);
		
		find.setOnClickListener(this);
		
		my.setOnClickListener(this);	
		friends.setOnClickListener(this);
		showmessgenum=(TextView)findViewById(R.id.showmessgenum);	
		backcome=(ImageView)findViewById(R.id.iv_backcome);
		backcome.setImageResource(R.drawable.come_mess_press);		
		ListView_come=(ListView)findViewById(R.id.lv_come);	
		ComeMessageAdapter adapter=new ComeMessageAdapter(this,list);
		ListView_come.setAdapter(adapter);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private void initdata() {
		// TODO Auto-generated method stub
		Map m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
	}
}
