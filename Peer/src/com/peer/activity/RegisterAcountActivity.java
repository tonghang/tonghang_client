package com.peer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.peer.R;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;


public class RegisterAcountActivity extends BasicActivity{
	private EditText email_registe,password_registe,repasword_registe,nike_registe;
	private TextView registe_remind,title,xieyi;
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
		email_registe.addTextChangedListener(textwatcher);
		password_registe.addTextChangedListener(textwatcher);
		repasword_registe.addTextChangedListener(textwatcher);
		nike_registe.addTextChangedListener(textwatcher);
		complete_registe.setEnabled(false);	
		xieyi=(TextView)findViewById(R.id.xieyi);
		xieyi.setOnClickListener(this);
		SpannableStringBuilder builder = new SpannableStringBuilder(xieyi.getText().toString());
		ForegroundColorSpan colorspan = new ForegroundColorSpan(getResources().getColor(R.color.backcolornol));
		builder.setSpan(colorspan, 16, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		xieyi.setText(builder);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_complete_registe:
			if(checkNetworkState()){				
				Register();					
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}		
			break;
		case R.id.xieyi:
			Intent intent=new Intent(RegisterAcountActivity.this,xieyiActivity.class);
			startActivity(intent);
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
			if(LocalStorage.getBoolean(RegisterAcountActivity.this, Constant.CAN_REGISTER_USER)){
				registe_remind.setText(getResources().getString(R.string.config_registe));
			}else{
				pd = ProgressDialog.show(RegisterAcountActivity.this, "", "正在注册。。。");
				RegisterTask task=new RegisterTask();
				task.execute(email,password,nikename);
			}
			
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
					String huanxing_username=PeerUI.getInstance().getISessionManager().getHuanxingUser();					
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
			pd.dismiss();
			if(result.equals(Constant.CALLBACKSUCCESS)){				
				Intent intent=new Intent(RegisterAcountActivity.this,RegisterTagActivity.class);
				startActivity(intent);
				finish();
			}else if(result.equals(getResources().getString(R.string.exit_eamil))){
				registe_remind.setText(result);
			}else{
				registe_remind.setText(getResources().getString(R.string.regist_fail));
			}
		}
	}

}
