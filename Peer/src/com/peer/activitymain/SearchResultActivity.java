package com.peer.activitymain;

import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.SeachResultAdapter;
import com.peer.adapter.SearchSkillAdapter;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.SearchUtil;

public class SearchResultActivity extends BasicActivity {
	private ListView mlistview;
	private TextView title;
	private LinearLayout back;
	private List<String> labellist;
	private List<User> userlist;
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
		mlistview=(ListView)findViewById(R.id.lv_searchresult);
		if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTAG)){				
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYTAG)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
			SearchTask task=new SearchTask();
			task.execute(searchtarget);			
		}
		
	}
	private class SearchTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub	
			SessionListener callback=new SessionListener();
			try {
				if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTAG)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
					userlist=PeerUI.getInstance().getISessionManager().searchUsersByNickName(paramer[0],callback);
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYTAG)){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
					userlist=PeerUI.getInstance().getISessionManager().searchUserByLabel(paramer[0],callback);
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
				if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTAG)){				
					if(labellist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SearchSkillAdapter adapter=new SearchSkillAdapter(SearchResultActivity.this,labellist);
						mlistview.setAdapter(adapter);
					}					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
					if(userlist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SeachResultAdapter adapter=new SeachResultAdapter(SearchResultActivity.this,userlist);
						mlistview.setAdapter(adapter);
					}
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
					if(userlist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SeachResultAdapter adapter=new SeachResultAdapter(SearchResultActivity.this,userlist);
						mlistview.setAdapter(adapter);
					}					
				}else if(SearchUtil.getInstance().getSearchtype().equals(Constant.USERBYTAG)){
					if(labellist.isEmpty()){
						ShowMessage(getResources().getString(R.string.search_null));
					}else{
						SearchSkillAdapter adapter=new SearchSkillAdapter(SearchResultActivity.this,labellist);
						mlistview.setAdapter(adapter);
					}	
				}
			}else{
				ShowMessage("搜索失败");
			}
			
		}
	}
	
	
	
}
