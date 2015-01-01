package com.peer.adapter;

import java.util.List;
import java.util.Map;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.client.User;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomepageAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Map> mList;
	public HomepageAdapter( Context mContext,List<Map> mList){
		this.mContext=mContext;
		this.mList=mList;
		LoadImageUtil.initImageLoader(mContext);
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
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		String type=(String) mList.get(position).get("type");	
		if(convertView==null){
			viewHolder=new ViewHolder();
			if(type.equals(Constant.USER)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listperson,null,false);				
				viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				LoadImageUtil.imageLoader.displayImage(Constant.WEB_SERVER_ADDRESS+mList.get(position).get("image"), viewHolder.headpic, LoadImageUtil.options);				
				viewHolder.nikename.setText((String)mList.get(position).get("username"));
				List<String>list=(List<String>) mList.get(position).get("labels");
				String labels="";
				for(String s:list){
					if(labels.equals("")){
						labels=s;	
					}else{
						labels=labels+" | "+s;
					}			
				}
				viewHolder.descripe.setText(labels);
				viewHolder.click.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub						
						PersonpageUtil.getInstance().setPersonid(String.valueOf(mList.get(position).get("id")));
						PersonpageUtil.getInstance().setHuanxinId((String)mList.get(position).get("huanxin_username"));
						PersonpageUtil.getInstance().setPersonname((String)mList.get(position).get("username"));
						if(LocalStorage.getBoolean(mContext, "istestui")){
							PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
							Intent intent=new Intent(mContext,PersonalPageActivity.class);
							mContext.startActivity(intent);
						}else{
							if((Boolean)mList.get(position).get("is_friend")){
								PersonpageUtil.getInstance().setPersonpagetype(Constant.FRIENDSPAGE);
							}else{
								PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
							}						
							Intent intent=new Intent(mContext,PersonalPageActivity.class);
							mContext.startActivity(intent);
						}						
					}
				});
			}else if(type.equals(Constant.TOPIC)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
				viewHolder.skillname=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.topic=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.skillname.setText((String)mList.get(position).get("label_name"));
				viewHolder.topic.setText((String)mList.get(position).get("subject"));
				viewHolder.click.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
						ChatRoomTypeUtil.getInstance().setHuanxingId(String.valueOf(mList.get(position).get("huanxin_group_id")));
						ChatRoomTypeUtil.getInstance().setTitle((String)mList.get(position).get("label_name"));
						ChatRoomTypeUtil.getInstance().setTheme((String)mList.get(position).get("subject"));
						ChatRoomTypeUtil.getInstance().setTopicId((String)mList.get(position).get("id"));
						
						if(LocalStorage.getBoolean(mContext, "istestui")){
							Intent intent=new Intent(mContext,ChatRoomActivity.class);
							mContext.startActivity(intent);
						}else{
							Map m=(Map)mList.get(position).get("user");
							User user=new User();
							user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
							user.setId((String)m.get("user_id"));
							user.setUsername((String)m.get("username"));
							ChatRoomTypeUtil.getInstance().setUser(user);
						
							Intent intent=new Intent(mContext,ChatRoomActivity.class);
							mContext.startActivity(intent);
						}
						
						
					}
				});
			}
			
		}	
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,skillname;
		TextView descripe,topic;
	}
}
