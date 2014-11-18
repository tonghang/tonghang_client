package com.peer.adapter;

import java.util.List;
import java.util.Map;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComeMessageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map> mList;
	public ComeMessageAdapter(Context mContext,List<Map> mList){
		this.mContext=mContext;
		this.mList=mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
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
		String type=(String) mList.get(position).get("type");		
		if(convertView==null){
			viewHolder=new ViewHolder();
			if(type.equals("person")){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listperson,null,false);				
				viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
				viewHolder.headpic.setLayoutParams(new RelativeLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.widgeheight), (int) mContext.getResources().getDimension(R.dimen.widgeheight)));
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int) mContext.getResources().getDimension(R.dimen.widgeheight_max));
				params.leftMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);
				params.rightMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);			
				viewHolder.click.setLayoutParams(params);
				convertView.setTag(viewHolder);
			}else if(type.equals("topic")){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						(int) mContext.getResources().getDimension(R.dimen.widgeheight_max));
				params.leftMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);
				params.rightMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);
				viewHolder.click.setLayoutParams(params);
				convertView.setTag(viewHolder);
			}
			
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(type.equals("person")){
			viewHolder.nikename.setText("昵称");
			viewHolder.descripe.setText("聊天内容，在这里记录最后一次的内容");
			viewHolder.click.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					mContext.startActivity(intent);
				}
			});
		}else{
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
		}	
		
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename;
		TextView descripe;
	}
}
