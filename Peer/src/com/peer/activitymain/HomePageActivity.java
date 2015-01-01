package com.peer.activitymain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.HomepageAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomePageActivity extends BasicActivity{	
	private TextView tv_find,showmessgenum,createtopic;
	private LinearLayout search,find_homepage,come_homepage,my_homepage,friends;
	private ImageView findback;
	private ProgressDialog pd;
	private PullToRefreshListView mPullrefreshlistview;
	
	List<Map> list=new ArrayList<Map>();
	HomepageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		initdata();
		init();		
	}		
	private void init() {
		// TODO Auto-generated method stub
		tv_find=(TextView)findViewById(R.id.tv_find);
		tv_find.setTextColor(getResources().getColor(R.color.bottomtextblue));
		
		find_homepage=(LinearLayout)findViewById(R.id.ll_find);		
		come_homepage=(LinearLayout)findViewById(R.id.ll_come);
		my_homepage=(LinearLayout)findViewById(R.id.ll_my);
		friends=(LinearLayout)findViewById(R.id.ll_friends);
		
		search=(LinearLayout)findViewById(R.id.ll_search);
		showmessgenum=(TextView)findViewById(R.id.showmessgenum);
		
		findback=(ImageView)findViewById(R.id.iv_backfind);
		
		createtopic=(TextView)findViewById(R.id.tv_createtopic);
		createtopic.setOnClickListener(this);
		
		
		come_homepage.setOnClickListener(this);
		my_homepage.setOnClickListener(this);
		friends.setOnClickListener(this);
		search.setOnClickListener(this);
		
		findback.setImageResource(R.drawable.find_label_press);
			
		mPullrefreshlistview=(PullToRefreshListView)findViewById(R.id.pull_refresh_homepage);
		adapter=new HomepageAdapter(this,list);
		mPullrefreshlistview.setAdapter(adapter);
		RefreshListner();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_search:
			Intent tosearch=new Intent(HomePageActivity.this,SearchActivity.class);
			startActivity(tosearch);			
			break;
		case R.id.tv_createtopic:
			Intent creat=new Intent(HomePageActivity.this,CreatTopicActivity.class);
			startActivity(creat);
			break;
		}
	}
	private void RefreshListner() {
		// TODO Auto-generated method stub
		mPullrefreshlistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.clear();
				FreshTask task=new FreshTask();
				task.execute("dowm");
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);				
				FreshTask task=new FreshTask();
				task.execute("up");
			
			}			
		});
	}
	private class FreshTask extends AsyncTask<String, String, Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);														
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(arg0[0].equals("up")){
				freshdata();	
			}else{
				initdata();
			}		
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			adapter.notifyDataSetChanged();
			mPullrefreshlistview.onRefreshComplete();
		}
		
	}
	private void initdata() {
		// TODO Auto-generated method stub
		Map user=new HashMap();
		user.put("type", "user");
		user.put("username", "离尘之影");
		List<String> labels=new ArrayList<String>();
		labels.add("美食");
		labels.add("java");
		user.put("labels", labels);		
		list.add(user);
		
		Map topic=new HashMap();
		topic.put("type", "topic");
		topic.put("label_name", "美食");
		topic.put("subject", "话题：大家来交流一下各国美食的做法，让我们的厨艺更上一层楼");
		list.add(topic);
		
	}
	private void freshdata(){
		Map user=new HashMap();
		user.put("type", "user");
		user.put("username", "离尘之影");
		List<String> labels=new ArrayList<String>();
		labels.add("美食");
		labels.add("java");
		user.put("labels", labels);		
		list.add(user);
		
		Map topic=new HashMap();
		topic.put("type", "topic");
		topic.put("label_name", "美食");
		topic.put("subject", "大家来交流一下各国美食的做法，让我们的厨艺更上一层楼");
		list.add(topic);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
//			  	Intent intent1 = new Intent(HomePageActivity.this, FxService.class);
//				stopService(intent1);
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.addCategory(Intent.CATEGORY_HOME);
		        startActivity(intent);
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
	}
}

