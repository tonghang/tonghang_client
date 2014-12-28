package com.peer.activity;


import java.util.ArrayList;
import java.util.List;
import com.peer.R;
import com.peer.adapter.SkillAdapter;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.event.NewFriensEvent;
import com.peer.event.SkillEvent;

import de.greenrobot.event.EventBus;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MySkillActivity extends BasicActivity {
	private int Hadtag;
	private ListView mytaglistview;
	private LinearLayout creatTag,back;
	public static Handler handler;
	private TextView title;
	private EventBus mBus;
	private SkillAdapter adapter;
	private List<String> mlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myskill);
//		initdata();
		init();	
		registEventBus();
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.myskill));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		creatTag=(LinearLayout)findViewById(R.id.ll_createTag_mytag);
		creatTag.setOnClickListener(this);
		mytaglistview=(ListView)findViewById(R.id.lv_myskill);
		try {
			mlist=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter=new SkillAdapter(this,mlist);
		mytaglistview.setAdapter(adapter);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_createTag_mytag:
			if(Hadtag>4){
				ShowMessage("您已经有五个标签，不能再创建了");
				break;
			}else{
				if(checkNetworkState()){
					CreateTagDialog();
				}else{
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}					
			}				
			break;

		default:
			break;
		}
	}
	private void CreateTagDialog() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(MySkillActivity.this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySkillActivity.this);
        builder.setTitle(getResources().getString(R.string.register_tag)).setView(inputServer).setNegativeButton(
        		getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString().trim();
                       if(!TextUtils.isEmpty(inputName)){
                    	   if(inputName.length()<7){
	                      		 for(int i=0;i<mlist.size();i++){ 
	                      			 if(mlist.get(i).equals(inputName)){
	                      				 ShowMessage(getResources().getString(R.string.repetskill));
	                      			 }else{
	                      				createLable(inputName);
	                      			 } 
	                      		 }
	                      	}else{
	                      		ShowMessage(getResources().getString(R.string.skillname));
	                      	}                       
                       }else{
                    	   ShowMessage("请输入秀场名");
                       }                       
                    }
                });
        builder.show();			
	}
	public void createLable(final String label){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SessionListener callback=new SessionListener();
				try {
					mlist.add(label);
					PeerUI.getInstance().getISessionManager().registerLabel(mlist,callback);					
					adapter.notifyDataSetChanged();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}	
	private void initdata() {
		// TODO Auto-generated method stub
		mlist=new ArrayList<String>();
		for(int i=0;i<5;i++){
			mlist.add("friends");
		}
	}
	private void registEventBus() {
		// TODO Auto-generated method stub
		 mBus=EventBus.getDefault();
		/*
		 * Registration: three parameters are respectively, message subscriber (receiver), receiving method name, event classes
		 */
		 mBus.register(this, "getSkillEvent",SkillEvent.class);
	}
	private void getSkillEvent(final SkillEvent event){
		event.getPosition();
		event.getLabel();
		if(event.isIsdelete()){
			new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub					
					SessionListener callback=new SessionListener(); 
					mlist.remove(event.getPosition());
	            	 try {
						PeerUI.getInstance().getISessionManager().registerLabel(mlist,callback);
						PeerUI.getInstance().getISessionManager().setLabels(mlist);
	            	 } catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
	            	 }  
	            	 if (callback.getMessage().equals(Constant.CALLBACKSUCCESS)) {
	            		 MySkillActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									 adapter.notifyDataSetChanged();
								}
							});
	            	 }            		 
				}
			}).start(); 
		}else{
			 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub	
						 mlist.get(event.getPosition()).replace(mlist.get(event.getPosition()), event.getLabel());
             		   SessionListener callback=new SessionListener();           	 
                     	 try {
         					PeerUI.getInstance().getISessionManager().registerLabel(mlist,callback);
         					PeerUI.getInstance().getISessionManager().setLabels(mlist);
                     	 } catch (RemoteException e) {
         					// TODO Auto-generated catch block
         					e.printStackTrace();
                     	 }  
                     	 if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
                     		 MySkillActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									adapter.notifyDataSetChanged();
								}
							});
                     	 }
					}
				}).start();
		}
		
		
		
		
		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBus.unregister(this);
	}
}
