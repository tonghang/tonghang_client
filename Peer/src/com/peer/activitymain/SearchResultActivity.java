package com.peer.activitymain;

import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.SeachResultAdapter;
import com.peer.adapter.SearchSkillAdapter;
import com.peer.adapter.TopicAdapter;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.SearchUtil;

public class SearchResultActivity extends BasicActivity {
	private PullToRefreshListView mlistview;
	private TextView title;
	private LinearLayout back;
	private List<String> labellist;
	private List<User> userlist;
	private List<Topic> topiclist;
	private int page=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		String searchtarget=SearchUtil.getInstance().getSearchname();
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.searchresult));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		mlistview=(PullToRefreshListView)findViewById(R.id.lv_searchresult);
		if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELTOPIC)){				
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELUSER)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICLIST)){
			SearchTask task=new SearchTask();
			task.execute(SearchUtil.getInstance().getCallbacklabel());
		}
		RefreshListner();
		
	}
	private void RefreshListner() {
		// TODO Auto-generated method stub
		mlistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub					
				String label = DateUtils.formatDateTime(SearchResultActivity.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				downRefreshTask refresh=new downRefreshTask();
				refresh.execute();
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(SearchResultActivity.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				page=+1;
				RefreshTask task=new RefreshTask();
				task.execute(SearchUtil.getInstance().getSearchname());
			}			
		});
	}
	private class RefreshTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);														
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			SessionListener callback=new SessionListener();
			try {
				if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELTOPIC)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
					topiclist.addAll(PeerUI.getInstance().getISessionManager().searchTopicBykey(paramer[0],page,callback));
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELUSER)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
					userlist=PeerUI.getInstance().getISessionManager().searchUsersByNickName(paramer[0],page,callback);
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICLIST)){
					topiclist.addAll(PeerUI.getInstance().getISessionManager().searchTopicByLabel(paramer[0],page,callback));
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mlistview.onRefreshComplete();
		}		
	}
	private class SearchTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub	
			SessionListener callback=new SessionListener();
			try {
				if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELTOPIC)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
					topiclist=PeerUI.getInstance().getISessionManager().searchTopicBykey(paramer[0],1,callback);
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELUSER)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
					userlist=PeerUI.getInstance().getISessionManager().searchUsersByNickName(paramer[0],page,callback);
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICLIST)){
					topiclist=PeerUI.getInstance().getISessionManager().searchTopicByLabel(paramer[0],1,callback);
				}
				
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
				if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELTOPIC)){				
					if(labellist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SearchSkillAdapter adapter=new SearchSkillAdapter(SearchResultActivity.this,labellist,Constant.TOPICLIST);
						mlistview.setAdapter(adapter);
					}					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
					if(topiclist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						TopicAdapter adapter=new TopicAdapter(SearchResultActivity.this,topiclist);
						mlistview.setAdapter(adapter);
					}
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICLIST)){
					if(topiclist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						TopicAdapter adapter=new TopicAdapter(SearchResultActivity.this,topiclist);
						mlistview.setAdapter(adapter);
					}
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
					if(userlist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SeachResultAdapter adapter=new SeachResultAdapter(SearchResultActivity.this,userlist);
						mlistview.setAdapter(adapter);
					}					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.LABELUSER)){
					if(labellist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SearchSkillAdapter adapter=new SearchSkillAdapter(SearchResultActivity.this,labellist,Constant.USERBYNIKE);
						mlistview.setAdapter(adapter);
					}	
				}
			}else{
				ShowMessage("搜索失败");
			}
			
		}
	}
	private class downRefreshTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);														
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mlistview.onRefreshComplete();
		}		
	}
	
	
}
