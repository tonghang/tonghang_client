package com.peer.activitymain;

import com.peer.R;
import com.peer.activity.BasicActivity;
import com.peer.constant.Constant;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.util.SearchUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchActivity extends BasicActivity {
	public boolean isSearchSkill=true;
	private int searchtype=2;//搜索类型 默认按话题关键字搜索话题
	private TextView searchtag,searchuser;
	private Button clean;
	private ImageView search_search;
	private EditText contentsearch;
	private LinearLayout back,mLayoutClearSearchText,downview;
	private InputMethodManager imm;
	private TitlePopup tagPopup,userPopup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
		popupwindow();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switch (searchtype) {
		case 1:
			SearchUtil.getInstance().setSearchtype(Constant.LABELTOPIC);
			break;
		case 2:
			SearchUtil.getInstance().setSearchtype(Constant.TOPICBYTOPIC);
			break;
		case 3:
			SearchUtil.getInstance().setSearchtype(Constant.LABELUSER);
			break;
		case 4:
			SearchUtil.getInstance().setSearchtype(Constant.USERBYNIKE);
			break;
		default:
			break;
		}
	}
	private void init() {
		// TODO Auto-generated method stub		
		searchtag=(TextView)findViewById(R.id.searchtag);
		searchtag.setOnClickListener(this);
		searchtag.setTextColor(getResources().getColor(R.color.black));
		searchtag.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchborder));
		
		searchuser=(TextView)findViewById(R.id.search_user);
		searchuser.setOnClickListener(this);
		searchuser.setTextColor(getResources().getColor(R.color.seachbluetext));
		searchuser.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbordernol));
		
		mLayoutClearSearchText = (LinearLayout) findViewById(R.id.layout_clear_search_text);
		
		clean= (Button) findViewById(R.id.btn_clear_search_text);
		clean.setOnClickListener(this);
		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		
		contentsearch=(EditText)findViewById(R.id.et_contentsearch);
		contentsearch.addTextChangedListener(watcher);
		contentsearch.setFocusable(true);
		contentsearch.setFocusableInTouchMode(true); 
		contentsearch.requestFocus();
		
		imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE); 
		imm.showSoftInput(contentsearch, InputMethodManager.RESULT_SHOWN); 
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); 
		
		search_search=(ImageView)findViewById(R.id.im_search_search);		
		search_search.setOnClickListener(this);
		
		tagPopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		tagPopup.addAction(new ActionItem(this, getResources().getString(R.string.bytopic), R.color.white));
		tagPopup.addAction(new ActionItem(this, getResources().getString(R.string.bytag), R.color.white));		
		
		userPopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		userPopup.addAction(new ActionItem(this, getResources().getString(R.string.bytag), R.color.white));
		userPopup.addAction(new ActionItem(this, getResources().getString(R.string.bynike), R.color.white));
		
		downview=(LinearLayout)findViewById(R.id.search_downview);
		downview.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchtag:
			SearchSkill();					
			break;
		case R.id.search_user:
			SearchUser();						
			break;
		case R.id.btn_clear_search_text:
			contentsearch.setText("");
			mLayoutClearSearchText.setVisibility(View.GONE);
			break;
		case R.id.search_downview:
			if(isSearchSkill){
				tagPopup.showonserchtag(v);	
			}else{
				userPopup.showonserchuser(v);
			}
			break;
		case R.id.im_search_search:
			if(checkNetworkState()){
				String searchtaget=contentsearch.getText().toString().trim();
				if(TextUtils.isEmpty(searchtaget)){
					ShowMessage("搜索框不能为空");
				}else{
					Search(searchtaget);
				}	
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}											
			break;
		case R.id.ll_back:
			imm.hideSoftInputFromWindow(contentsearch.getWindowToken(), 0);
			finish();
			break;
		default:
			break;
		}
		
	};
	 
	private void SearchSkill() {
			// TODO Auto-generated method stub
		  isSearchSkill=true;
		  searchtype=2;
		  SearchUtil.getInstance().setSearchtype(Constant.TOPICBYTOPIC);
		  contentsearch.setHint(getResources().getString(R.string.bytopic));
		  searchtag.setTextColor(getResources().getColor(R.color.black));
		  searchtag.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchborder));
		  searchuser.setTextColor(getResources().getColor(R.color.seachbluetext));
		  searchuser.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbordernol));
	  }
	private void SearchUser() {
			// TODO Auto-generated method stub
		  isSearchSkill=false;
		  searchtype=4;
		  SearchUtil.getInstance().setSearchtype(Constant.USERBYNIKE);
		  contentsearch.setHint(getResources().getString(R.string.bynike));
		  searchuser.setTextColor(getResources().getColor(R.color.black));
		  searchuser.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchborder));
		  searchtag.setTextColor(getResources().getColor(R.color.seachbluetext));
		  searchtag.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbordernol));
		}
	 private void Search(String tagetname) {
			// TODO Auto-generated method stub
		 imm.hideSoftInputFromWindow(contentsearch.getWindowToken(), 0);
		 SearchUtil.getInstance().setSearchname(tagetname);
		 
		 Intent intent=new Intent(SearchActivity.this, SearchResultActivity.class);
		 startActivity(intent);
		}
	 private void popupwindow() {
		 tagPopup.setItemOnClickListener(new OnItemOnClickListener() {			
				@Override
				public void onItemClick(ActionItem item, int position) {
					// TODO Auto-generated method stub
					if(item.mTitle.equals(getResources().getString(R.string.bytag))){
						contentsearch.setHint(item.mTitle);
						SearchUtil.getInstance().setSearchtype(Constant.LABELTOPIC);
						searchtype=1;//安标签搜索话题
					}else if(item.mTitle.equals(getResources().getString(R.string.bytopic))){
						contentsearch.setHint(item.mTitle);
						SearchUtil.getInstance().setSearchtype(Constant.TOPICBYTOPIC);
						searchtype=2;//按话题关键字搜索话题
					}					
				}
		 });
		 userPopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(item.mTitle.equals(getResources().getString(R.string.bytag))){
					contentsearch.setHint(item.mTitle);
					SearchUtil.getInstance().setSearchtype(Constant.LABELUSER);
					searchtype=3;//按标签搜索用户
				}else if(item.mTitle.equals(getResources().getString(R.string.bynike))){
					contentsearch.setHint(item.mTitle);
					SearchUtil.getInstance().setSearchtype(Constant.USERBYNIKE);
					searchtype=4;//按昵称搜索用户
				}
				
			}
		});
	 }
 
	 TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			int textLength = contentsearch.getText().length();
			if (textLength > 0) {
				mLayoutClearSearchText.setVisibility(View.VISIBLE);
			} else {
				mLayoutClearSearchText.setVisibility(View.GONE);
			}
		}
	};
}
