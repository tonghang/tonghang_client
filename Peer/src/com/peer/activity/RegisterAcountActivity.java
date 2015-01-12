package com.peer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.IMinterface.IM;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;


public class RegisterAcountActivity extends BasicActivity{
	private EditText email_registe,password_registe,repasword_registe,nike_registe;
	private TextView registe_remind,title;
	private Button complete_registe;
	private LinearLayout back;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeracount);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.register_acount));
		
		registe_remind=(TextView)findViewById(R.id.registe_remind);
		email_registe=(EditText)findViewById(R.id.et_email_regist);
		password_registe=(EditText)findViewById(R.id.et_password_registe);
		repasword_registe=(EditText)findViewById(R.id.et_repassword_registe);
		nike_registe=(EditText)findViewById(R.id.et_nike_registe);	
		complete_registe=(Button)findViewById(R.id.bt_complete_registe);
		complete_registe.setOnClickListener(this);		
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		if(LocalStorage.getBoolean(this, "istestui")){
			complete_registe.setEnabled(true);
		}else{			
			email_registe.addTextChangedListener(textwatcher);
			password_registe.addTextChangedListener(textwatcher);
			repasword_registe.addTextChangedListener(textwatcher);
			nike_registe.addTextChangedListener(textwatcher);
			complete_registe.setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_complete_registe:
			if(LocalStorage.getBoolean(this, "istestui")){
				Intent intent=new Intent(RegisterAcountActivity.this,RegisterTagActivity.class);
				startActivity(intent);	
			}else{
				if(checkNetworkState()){
					Register();					
				}else{
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}
			}	
			break;
		default:
			break;
		}
	}
	private void Register() {
		// TODO Auto-generated method stub
		
		String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		String email=email_registe.getText().toString().trim();
		String password=password_registe.getText().toString().trim();
		String repassword=repasword_registe.getText().toString().trim();		
		String nikename=nike_registe.getText().toString().trim();
		
		if(!email.matches(format)){
			registe_remind.setText(getResources().getString(R.string.erroremail));
			return ;
		}else if(!password.matches("^[a-zA-Z0-9_]{5,17}$")){
			registe_remind.setText(getResources().getString(R.string.errorpswformat));
			return ;
		}else if(!password.equals(repassword)){
			registe_remind.setText(getResources().getString(R.string.notmatchpsw));
			return ;
		}else if(nike_registe.length()>10){
			registe_remind.setText(getResources().getString(R.string.errornike));
			return ;
		}else{	
			pd = ProgressDialog.show(RegisterAcountActivity.this, "", "正在注册。。。");
			RegisterTask task=new RegisterTask();
			task.execute(email,password,nikename);
		}		
	}	
	 TextWatcher textwatcher=new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			testnull();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			testnull();
		}				
		};
	private void testnull(){
		String email=email_registe.getText().toString().trim();
		String password=password_registe.getText().toString().trim();
		String passwordtesst=repasword_registe.getText().toString().trim();
		String nikename=nike_registe.getText().toString().trim();
		if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(passwordtesst)&&!TextUtils.isEmpty(nikename)){
			complete_registe.setEnabled(true);
		}else{
			complete_registe.setEnabled(false);
		}
	}
	private class RegisterTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... paramer) {
			// TODO Auto-generated method stub				
			SessionListener callback=new SessionListener();
			try {
				
				PeerUI.getInstance().getISessionManager().register(paramer[0], paramer[1], paramer[2], callback);
				if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
					
					LocalStorage.saveString(RegisterAcountActivity.this, Constant.EMAIL, paramer[0]);
					UserBean u=new UserBean();
					u.setEmail(paramer[0]);
					u.setPassword(paramer[1]);
					u.setNikename(paramer[2]);			
					UserDao userdao=new UserDao(RegisterAcountActivity.this);
					userdao.addUser(u);
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
				pd.dismiss();
				Intent intent=new Intent(RegisterAcountActivity.this,RegisterTagActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

}
