package com.peer.activitymain;

import com.peer.R;
import com.peer.activity.BasicActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class SearchActivity extends BasicActivity {
	private boolean isSearchSkill=true;
	private TextView searchtag,searchuser;
	private Button clean;
	private Button search_search;
	private EditText contentsearch;
	private LinearLayout back,mLayoutClearSearchText;
	private InputMethodManager imm;
	private TableLayout tbl_skill;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
		
	}
	private void init() {
		// TODO Auto-generated method stub
		tbl_skill=(TableLayout)findViewById(R.id.tl_skillrecomend);
		
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
		
		search_search=(Button)findViewById(R.id.tv_search_search);		
		search_search.setOnClickListener(this);
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
		case R.id.tv_search_search:
			Search();
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
		tbl_skill.setVisibility(View.VISIBLE);
		  searchtag.setTextColor(getResources().getColor(R.color.black));
		  searchtag.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchborder));
		  searchuser.setTextColor(getResources().getColor(R.color.seachbluetext));
		  searchuser.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbordernol));
	  }
	private void SearchUser() {
			// TODO Auto-generated method stub
		tbl_skill.setVisibility(View.GONE);
		isSearchSkill=false;
		  searchuser.setTextColor(getResources().getColor(R.color.black));
		  searchuser.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchborder));
		  searchtag.setTextColor(getResources().getColor(R.color.seachbluetext));
		  searchtag.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbordernol));
		}
	 private void Search() {
			// TODO Auto-generated method stub
		 Intent intent=new Intent(SearchActivity.this, SearchResultActivity.class);
		 startActivity(intent);
			if(isSearchSkill){//判断搜索技能还是搜索用户
				
			}
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
