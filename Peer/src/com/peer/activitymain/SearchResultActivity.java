package com.peer.activitymain;

import java.util.ArrayList;
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
import com.peer.client.ISessionListener;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
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
//		labellist=new ArrayList<String>();
		String searchtarget=SearchUtil.getInstance().getSearchname();
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.searchresult));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		mlistview=(ListView)findViewById(R.id.lv_searchresult);
		if(LocalStorage.getBoolean(this, "istestui")){
//			if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){				
//				SearchSkillAdapter adapter=new SearchSkillAdapter(this,labellist);
//				mlistview.setAdapter(adapter);
//			}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
//				SeachResultAdapter adapter=new SeachResultAdapter(this);
//				mlistview.setAdapter(adapter);
//			}	
		}else{
			if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){				
				SearchTask task=new SearchTask();
				task.execute(searchtarget);
				
			}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
				SearchTask task=new SearchTask();
				task.execute(searchtarget);
			}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSERBYLABEL){
				SearchTask task=new SearchTask();
				task.execute(searchtarget);
				
			}
		}
			
	}
	private class SearchTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub	
			SessionListener callback=new SessionListener();
			try {
				if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
					userlist=PeerUI.getInstance().getISessionManager().searchUsersByNickName(paramer[0],callback);
				}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){
					labellist=PeerUI.getInstance().getISessionManager().search(paramer[0],callback);
				}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSERBYLABEL){
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
				if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){				
					SearchSkillAdapter adapter=new SearchSkillAdapter(SearchResultActivity.this,labellist);
					mlistview.setAdapter(adapter);
				}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
					SeachResultAdapter adapter=new SeachResultAdapter(SearchResultActivity.this,userlist);
					mlistview.setAdapter(adapter);
				}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSERBYLABEL){
					SeachResultAdapter adapter=new SeachResultAdapter(SearchResultActivity.this,userlist);
					mlistview.setAdapter(adapter);
				}
			}else{
				ShowMessage("未搜索到您想查找的内容！");
			}
			
		}
	}
	
	
	
}
