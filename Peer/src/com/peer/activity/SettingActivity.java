package com.peer.activity;

import java.io.File;

import com.peer.R;
import com.peer.util.ManagerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends BasicActivity{
	private LinearLayout back,setting_set,newfunction_set,feedback_set,newversion_set,clearcash_set;
	private Button relogin,todesk;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.setting));
		
		setting_set=(LinearLayout)findViewById(R.id.ll_setting_set);
		newfunction_set=(LinearLayout)findViewById(R.id.ll_newfunction_set);
		feedback_set=(LinearLayout)findViewById(R.id.ll_feedback_set);
		newversion_set=(LinearLayout)findViewById(R.id.ll_newversion_set);
		relogin=(Button)findViewById(R.id.bt_relogin);
		todesk=(Button)findViewById(R.id.bt_todesk);
		
		todesk.setOnClickListener(this);
		relogin.setOnClickListener(this);
		
		clearcash_set=(LinearLayout)findViewById(R.id.ll_clearcash_set);
		clearcash_set.setOnClickListener(this);
		
		newversion_set.setOnClickListener(this);
		setting_set.setOnClickListener(this);
		newfunction_set.setOnClickListener(this);
		feedback_set.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub]
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_setting_set:
			Intent setting=new Intent(SettingActivity.this,MessageNotifyActivity.class);
			startActivity(setting);
			break;
		case R.id.ll_newfunction_set:
			Intent newfunction=new Intent(SettingActivity.this,NewFunctionActivity.class);
			startActivity(newfunction);
			break;
		case R.id.ll_feedback_set:
			Intent feedback=new Intent(SettingActivity.this,FeedBackActivity.class);
			startActivity(feedback);
			break;
		case R.id.ll_newversion_set:
//			UmengUpdateAgent.forceUpdate(SettingActivity.this);
			break;
		case R.id.ll_clearcash_set:
			deleteFilesByDirectory(getCacheDir());
			ShowMessage(getResources().getString(R.string.cleancash));
			break;
		case R.id.bt_relogin:
			Relogin();
			break;		
		case R.id.bt_todesk:
			Todesk();
			break;
		default:
			break;
		}
	}
	private void Relogin(){
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.exitlogin))  
		.setMessage(getResources().getString(R.string.relogin)) .setNegativeButton(getResources().getString(R.string.cancel), null) 
		 .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){            	 
            	 ManagerActivity.getAppManager().restart(SettingActivity.this);
             }
		 })
		 .show();  
	}
	private void Todesk(){
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.close))  
		.setMessage(getResources().getString(R.string.todesk)) .setNegativeButton(getResources().getString(R.string.cancel), null) 
		 .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){
            	Intent intent = new Intent(Intent.ACTION_MAIN);
 		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 		        intent.addCategory(Intent.CATEGORY_HOME);
 		        startActivity(intent);
             }
		 })
		 .show(); 
	}
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
