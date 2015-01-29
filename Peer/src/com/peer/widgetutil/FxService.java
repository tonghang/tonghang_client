package com.peer.widgetutil;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.constant.Constant;

public class FxService extends Service 
{	
	private String roomaddress,ownernike,theme,fromfloat,
	tagname,image,userId,topicid;
	int xdown;
	int ydown;
	int xup;
	int yup;
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    
	WindowManager mWindowManager;
	
	RoundImageView mFloatView;
	
	private static final String TAG = "FxService";
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");		
        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);		
	}
	@Override
	public IBinder onBind(Intent intent)
	{			
		return null;
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		image=intent.getStringExtra(Constant.IMAGE);
		ownernike=intent.getStringExtra(Constant.OWNERNIKE);
		theme=intent.getStringExtra(Constant.THEME);
		tagname=intent.getStringExtra(Constant.TAGNAME);
		userId=intent.getStringExtra(Constant.USERID);
		topicid=intent.getStringExtra(Constant.TOPICID);
		roomaddress=intent.getStringExtra(Constant.ROOMID);
		fromfloat=intent.getStringExtra(Constant.FROMFLOAT);
		createFloatView();
		super.onStart(intent, startId);
	}
	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		
		wmParams.type = LayoutParams.TYPE_PHONE; 
		
        wmParams.format = PixelFormat.RGBA_8888; 
       wmParams.flags = 

          LayoutParams.FLAG_NOT_FOCUSABLE

          ;
        
        wmParams.gravity = Gravity.TOP | Gravity.LEFT; 
       
        wmParams.x =mWindowManager.getDefaultDisplay().getWidth();
        wmParams.y =320;

        
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
      
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatView = (RoundImageView)mFloatLayout.findViewById(R.id.float_image);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        	
		LoadImageUtil.imageLoader.displayImage(image, mFloatView, LoadImageUtil.options);
       
		mFloatView.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{									
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	            	xdown=(int) event.getRawX();
	            	ydown=(int) event.getRawY();
	            	break;
	            case MotionEvent.ACTION_MOVE:
	            	wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
					
		            wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2-25;
		          
		            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
		            break;
	            case MotionEvent.ACTION_UP:
	            	xup=(int) event.getRawX();
	            	yup=(int) event.getRawY();
	            	if(-mFloatView.getMeasuredWidth()/2<xdown-xup&&xdown-xup<mFloatView.getMeasuredWidth()/2
	            			&&-(mFloatView.getMeasuredHeight()/2-25)<ydown-yup&&ydown-yup<(mFloatView.getMeasuredHeight()/2-25)){
	            		Intent intent=new Intent(FxService.this,ChatRoomActivity.class);
	            		intent.putExtra(Constant.IMAGE, image);
	            		intent.putExtra(Constant.OWNERNIKE, ownernike);
	            		intent.putExtra(Constant.THEME, theme);			
	            		intent.putExtra(Constant.TAGNAME, tagname);
	            		intent.putExtra(Constant.USERID, userId);
	            		intent.putExtra(Constant.ROOMID, roomaddress);
	            		intent.putExtra(Constant.TOPICID, topicid);
	            		intent.putExtra(Constant.FROMFLOAT, fromfloat);
	            		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
	            	}
					if(320<(int) event.getRawX()){
						 wmParams.x = 720;
						 wmParams.y = (int) event.getRawY();
						 mWindowManager.updateViewLayout(mFloatLayout, wmParams);
						 
					}else{
						 wmParams.x = 0;
						 wmParams.y = (int) event.getRawY();
						 mWindowManager.updateViewLayout(mFloatLayout, wmParams);						 
					}
					break;
				}
				return false;
			}
		});	
	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);
		}
	}
	
}
