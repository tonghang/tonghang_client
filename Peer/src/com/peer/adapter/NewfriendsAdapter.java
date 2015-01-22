package com.peer.adapter;

import java.util.List;

import com.peer.R;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.event.NewFriensEvent;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewfriendsAdapter extends BaseAdapter {
	private Context mContext;	
	private List<User> mlist;
	public NewfriendsAdapter(Context mContext,List<User> mlist){
		this.mContext=mContext;
		this.mlist=mlist;
		LoadImageUtil.initImageLoader(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_listnike_friends,null,false);
			viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
			viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			viewHolder.refuse=(TextView)convertView.findViewById(R.id.tv_refuse);
			viewHolder.refuse.setVisibility(View.VISIBLE);
			viewHolder.access=(TextView)convertView.findViewById(R.id.tv_access);
			viewHolder.access.setVisibility(View.VISIBLE);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LoadImageUtil.imageLoader.displayImage(mlist.get(position).getImage(), viewHolder.headpic, LoadImageUtil.options);
		viewHolder.nikename.setText(mlist.get(position).getUsername());
		viewHolder.descripe.setText(mlist.get(position).getReason());	
		viewHolder.refuse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							SessionListener callback=new SessionListener();
							PeerUI.getInstance().getISessionManager().refuseAddFriends(mlist.get(position).getInvitionid(),callback);
							if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
								 ((Activity)mContext).runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											EventBus.getDefault().post(new NewFriensEvent(position));
											Toast.makeText(mContext, "拒绝添加为好友", 0).show();
										}
									});
							}							
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		viewHolder.access.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							SessionListener callback=new SessionListener();
							PeerUI.getInstance().getISessionManager().agreeAddFriends(mlist.get(position).getInvitionid(),callback);
							if (callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
								 ((Activity)mContext).runOnUiThread(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										EventBus.getDefault().post(new NewFriensEvent(position));
									}
									 
								 });								
							}								
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				
			}
		});
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(checkNetworkState()){
					PersonpageUtil.getInstance().setPersonid(mlist.get(position).getUserid());	
					PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
					Intent intent=new Intent(mContext,PersonalPageActivity.class);
					mContext.startActivity(intent);
				}else{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}
			}
		});
		return convertView;
	}
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,descripe,refuse,access;
		
	}


}
