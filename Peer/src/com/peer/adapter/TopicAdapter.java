package com.peer.adapter;

import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopicAdapter extends BaseAdapter {
	private Context mContext;
	public TopicAdapter(Context mContext){
		this.mContext=mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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
	public View getView(int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
			viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.time.setVisibility(View.VISIBLE);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
			viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.time.setText("2014-11-11");
		viewHolder.nikename.setText("技能标签");
		viewHolder.descripe.setText("聊天话题聊天话题聊天话题聊天话题聊天话题聊天话题聊天话题聊天话题聊天话题");
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
				Intent intent=new Intent(mContext,ChatRoomActivity.class);
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename;
		TextView descripe,time;
	}
}
