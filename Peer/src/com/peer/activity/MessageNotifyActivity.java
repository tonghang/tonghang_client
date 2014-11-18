package com.peer.activity;

import com.peer.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MessageNotifyActivity extends BasicActivity {
	private final int START=1,END=2; 
	private LinearLayout back;
	private CheckBox sound,vibrate;
	int starth=22,startm=30,endh=8,endm=30;
	private TextView start,end,title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_notify);
		init();			
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.notify));
		
		start=(TextView)findViewById(R.id.start);
		start.setOnClickListener(this);
		end=(TextView)findViewById(R.id.end);
		end.setOnClickListener(this);
		sound=(CheckBox)findViewById(R.id.cb_sound);
		vibrate=(CheckBox)findViewById(R.id.cb_vibrate);
//		if(LocalStorage.getBoolean(this, "sound")){
//			sound.setChecked(true);
//		}else{
//			sound.setChecked(false);
//		}
//		if(LocalStorage.getBoolean(this, "vibrate")){
//			vibrate.setChecked(true);
//		}else{
//			vibrate.setChecked(false);
//		}			
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		
		sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
//				if(isChecked){
//					LocalStorage.saveBoolean(MessageNotifyActivity.this, "sound", true);
//				}else{
//					LocalStorage.saveBoolean(MessageNotifyActivity.this, "sound", false);
//				}
			}
		});
		vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
//				if(isChecked){
//					LocalStorage.saveBoolean(MessageNotifyActivity.this, "vibrate", true);
//				}else{
//					LocalStorage.saveBoolean(MessageNotifyActivity.this, "vibrate", false);
//				}				
			}
		});		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.start:
			showDialog(START);
			break;
		case R.id.end:
			showDialog(END);
			break;

		default:
			break;
		}
	}
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case START:
			return new TimePickerDialog(this, mtimesetListener1, starth, startm, true);
		case END:
			return new TimePickerDialog(this, mtimesetListener2, endh, endm, true);
		default:
			break;
		}
		
		return null;
	}
	private TimePickerDialog.OnTimeSetListener mtimesetListener1 =  
	        new TimePickerDialog.OnTimeSetListener(){  
	        @Override  
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
	            // TODO Auto-generated method stub  
	            starth = hourOfDay;  
	            startm = minute; 
//	            LocalStorage.saveInt(MessageNotifyActivity.this, "starth", starth);
//	            LocalStorage.saveInt(MessageNotifyActivity.this, "startm", startm);
	            start.setText(new StringBuilder().append(starth).append(":").append(startm));
//	            updateDisplay();  
	        }    
	    };
	    private TimePickerDialog.OnTimeSetListener mtimesetListener2 =  
		        new TimePickerDialog.OnTimeSetListener(){  
		        @Override  
		        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
		            // TODO Auto-generated method stub  
		            endh = hourOfDay;  
		            endm = minute;  
//		            LocalStorage.saveInt(MessageNotifyActivity.this, "endh", endh);
//		            LocalStorage.saveInt(MessageNotifyActivity.this, "endm", endm);
		            end.setText(new StringBuilder().append(endh).append(":").append(endm));
//		            updateDisplay();  
		        }    
		    };  
}
