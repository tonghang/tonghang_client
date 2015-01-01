package com.peer.adapter;

import java.util.List;
import java.util.Map;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.widgetutil.LoadImageUtil;

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
			if(type.equals(Constant.USER)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listperson,null,false);				
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
			}else if(type.equals(Constant.TOPIC)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				
				convertView.setTag(viewHolder);
			}
			
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(type.equals(Constant.USER)){
			
			LoadImageUtil.imageLoader.displayImage(Constant.WEB_SERVER_ADDRESS+mList.get(position).get("image"), viewHolder.headpic, LoadImageUtil.options);				
			viewHolder.nikename.setText((String)mList.get(position).get("username"));
			List<String>list=(List<String>) mList.get(position).get("labels");
			String labels="";
			for(String s:list){				
					labels=labels+s;							
			}
			viewHolder.descripe.setText(labels);
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
			viewHolder.nikename.setText((String)mList.get(position).get("label_name"));
			viewHolder.descripe.setText((String)mList.get(position).get("subject"));
			
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
