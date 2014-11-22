package com.peer.adapter;

import java.util.List;

import com.peer.R;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.constant.Constant;
import com.peer.event.NewFriensEvent;
import com.peer.util.PersonpageUtil;

import de.greenrobot.event.EventBus;

import android.content.Context;
import android.content.Intent;
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
	private List<String> mlist;
	public NewfriendsAdapter(Context mContext,List<String> mlist){
		this.mContext=mContext;
		this.mlist=mlist;
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
		viewHolder.nikename.setText("昵称");
		viewHolder.descripe.setText("请求加为好友");	
		viewHolder.refuse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 EventBus.getDefault().post(new NewFriensEvent(position));
				 Toast.makeText(mContext, "拒绝添加为好友", 0).show();
			}
		});
		viewHolder.access.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 EventBus.getDefault().post(new NewFriensEvent(position));
			}
		});
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
				Intent intent=new Intent(mContext,PersonalPageActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,descripe,refuse,access;
		
	}


}
