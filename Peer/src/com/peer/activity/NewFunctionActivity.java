package com.peer.activity;

import com.peer.R;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewFunctionActivity extends BasicActivity {
	private LinearLayout back;
	private TextView title,introduce;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfunction);
		init();		
	}
	private void init() {
		// TODO Auto-generated method stub
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.newfunction));
		introduce=(TextView)findViewById(R.id.introduce);
		String vertionname=getInfo();
		introduce.setText("当前的版本号为"+vertionname);
	}
	private String getInfo() {
		// TODO Auto-generated method stub
		String pkName = this.getPackageName();
		String versionName=null;
		try {
			versionName = this.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return versionName;
	}
	
}
