package com.peer.activitymain;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.adapter.SeachResultAdapter;
import com.peer.adapter.SearchSkillAdapter;
import com.peer.constant.Constant;
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
		
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.searchresult));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		mlistview=(ListView)findViewById(R.id.lv_searchresult);
		if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHSKILL){
			SearchSkillAdapter adapter=new SearchSkillAdapter(this);
			mlistview.setAdapter(adapter);
		}else if(SearchUtil.getInstance().getSearchtype()==Constant.SEARCHUSER){
			SeachResultAdapter adapter=new SeachResultAdapter(this);
			mlistview.setAdapter(adapter);
		}		
	}
}
