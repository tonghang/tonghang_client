package com.peer.activitymain;

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
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.util.SearchUtil;

public class SearchResultActivity extends BasicActivity {
	private ListView mlistview;
	private TextView title;
	private LinearLayout back;
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
		if(LocalStorage.getBoolean(this, "istestui")){
			if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){				
				SearchSkillAdapter adapter=new SearchSkillAdapter(this);
				mlistview.setAdapter(adapter);
			}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
				SeachResultAdapter adapter=new SeachResultAdapter(this);
				mlistview.setAdapter(adapter);
			}	
		}else{
			if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){				
				SearchTask task=new SearchTask();
				task.execute(searchtarget);
				
//				SearchSkillAdapter adapter=new SearchSkillAdapter(this);
//				mlistview.setAdapter(adapter);
			}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
				SeachResultAdapter adapter=new SeachResultAdapter(this);
				mlistview.setAdapter(adapter);
			}	
		}
			
	}
	private class SearchTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub	
//			boolean b=RingLetterImp.getInstance().register(paramer[0], paramer[1], "");
			
			SessionListener callback=new SessionListener();
			try {
				PeerUI.getInstance().getISessionManager().search(paramer[0]);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			if(result){
//				Intent intent=new Intent(RegisterAcountActivity.this,RegisterTagActivity.class);
//				startActivity(intent);
//			}
		}
	}
	
	
	
}
