package com.peer.activity;

import java.util.ArrayList;
import com.peer.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterTagActivity extends BasicActivity {
	private EditText tagname1,tagname2,tagname3,tagname4,tagname5;
	private Button registe_tag;
	private TextView remind,title;
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registertag);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.register_tag));
		tagname1=(EditText)findViewById(R.id.et_tagname_1);
		tagname2=(EditText)findViewById(R.id.et_tagname_2);
		tagname3=(EditText)findViewById(R.id.et_tagname_3);
		tagname4=(EditText)findViewById(R.id.et_tagname_4);
		tagname5=(EditText)findViewById(R.id.et_tagname_5);

		remind=(TextView)findViewById(R.id.tv_remind);
		registe_tag=(Button)findViewById(R.id.bt_registe_tag);

		registe_tag.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		
		tagname1.addTextChangedListener(watcher);
		tagname2.addTextChangedListener(watcher);
		registe_tag.setEnabled(false);		
			
	}
	TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			String t1=tagname1.getText().toString().trim();
			String t2=tagname2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				registe_tag.setEnabled(true);
			}else{
				registe_tag.setEnabled(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			String t1=tagname1.getText().toString().trim();
			String t2=tagname2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				registe_tag.setEnabled(true);
			}else{
				registe_tag.setEnabled(false);
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_registe_tag:
			RegisteTag();
			break;
		default:
			break;
		}
	}
	private void RegisteTag() {
		// TODO Auto-generated method stub
		String t1=tagname1.getText().toString().trim();
		String t2=tagname2.getText().toString().trim();
		String t3=tagname3.getText().toString().trim();
		String t4=tagname4.getText().toString().trim();
		String t5=tagname5.getText().toString().trim();
		String [] arr={t1,t2,t3,t4,t5};
		ArrayList<String> list=new ArrayList<String>();
		
		boolean sameTag=true;
		boolean Tolong=true;
		boolean isbreak=true;
		for(int i=0;i<arr.length;i++){
			if(!arr[i].equals("")){
				list.add(arr[i]);									
			}			
		}
		if(!t1.equals("")&&!t2.equals("")){
			String fomate="^[A-Za-z]+$";
			for(int j=0;j<list.size();j++){	
				if(list.get(j).matches(fomate)){
					if(list.get(j).length()<13){
						Tolong=false;
					}else{
						Tolong=true;
						remind.setText(getResources().getString(R.string.skillname));
						break;
					}	
				}else{
					if(list.get(j).length()<7){
						Tolong=false;
					}else{
						Tolong=true;
						remind.setText(getResources().getString(R.string.skillname));
						break;
					}
				}
							
			}
			for(int j=0;j<list.size();j++){
				for(int k=j+1;k<list.size();k++){
					if(!list.get(j).equals(list.get(k))){
						sameTag=false;					
					}else{
						sameTag=true;
						isbreak=false;
						remind.setText(getResources().getString(R.string.repetskill));
						break;
					}	
				}
				if(!isbreak){
					break;
				}
			}
			
			if(!sameTag&&!Tolong){			
				Intent intent=new Intent(RegisterTagActivity.this,CompleteActivity.class);
				intent.putStringArrayListExtra("tags", list);
				remind.setText("");
				startActivity(intent);							
			}
		}else{
			remind.setText(getResources().getString(R.string.skillnull));
			return;
		}		
	}
}
