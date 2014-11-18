package com.peer.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.R;

public class UpdatePasswordActivity extends BasicActivity {
	private EditText oldpasw,newpasw,reputpasw;
	public Button submit;
	private LinearLayout back;
	private TextWatcher textwatcher;
	private TextView remind,title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changpassword);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.updatepassword));
		oldpasw=(EditText)findViewById(R.id.et_oldpasw);
		newpasw=(EditText)findViewById(R.id.et_newpasw);
		reputpasw=(EditText)findViewById(R.id.et_repasw);
		
		remind=(TextView)findViewById(R.id.updatepasw_remind);
		CreateTextwatcher();
		oldpasw.addTextChangedListener(textwatcher);
		newpasw.addTextChangedListener(textwatcher);
		reputpasw.addTextChangedListener(textwatcher);
		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		
		submit=(Button)findViewById(R.id.bt_changesubmite);
		submit.setEnabled(false);
		submit.setOnClickListener(this);
	}
	private void CreateTextwatcher() {
		// TODO Auto-generated method stub
		textwatcher=new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				TestNull();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				TestNull();
			}			
		};
		
	}
	private void TestNull() {
		// TODO Auto-generated method stub
		String old=oldpasw.getText().toString();
		String newpasws=newpasw.getText().toString();
		String testnew=reputpasw.getText().toString();
		if(!old.equals("")&&!newpasws.equals("")&&!testnew.equals("")){
			submit.setEnabled(true);
		}else{
			submit.setEnabled(false);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_changesubmite:
			UpdatePassword();
			break;

		default:
			break;
		}
	}
	private void UpdatePassword() {
		// TODO Auto-generated method stub
		String old=oldpasw.getText().toString().trim();
		String newpasws=newpasw.getText().toString().trim();
		String testnew=reputpasw.getText().toString().trim();
		String password="";
		if(!old.equals(password)){
			remind.setText(getResources().getString(R.string.erroroldpsw));
			return;
		}else if(!newpasws.matches("^[a-zA-Z0-9_]{5,17}$")){
			remind.setText(getResources().getString(R.string.errorpswformat));
			return;
		}else if(!newpasws.equals(testnew)){
			remind.setText(getResources().getString(R.string.notmatchpsw));
			return;
		}else{
			
		}
	}
	
	
}
