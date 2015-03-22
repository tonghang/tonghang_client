package com.peer.adapter;

import java.util.List;
import java.util.Map;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;
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

public class HomepageAdapter extends FatherAdater {
	
	private Context mContext;
	private List<Map> mList;
	public HomepageAdapter( Context mContext,List<Map> mList){
		super(mContext);
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
	public View getView(int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;		
		String type=(String) mList.get(position).get("type");			
		if(type.equals(Constant.USER)){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listperson,null,false);				
			viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
			viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			User user=(User) mList.get(position).get(Constant.USER);
			setUserView(viewHolder,user);
			convertView.setTag(viewHolder);							
		}else if(type.equals(Constant.TOPIC)){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
			viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.skillname=(TextView)convertView.findViewById(R.id.tv_skill);			
			viewHolder.topic=(TextView)convertView.findViewById(R.id.tv_topic);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			Topic topic=(Topic)mList.get(position).get(Constant.TOPIC);
			setTopicView(viewHolder,topic,position);
			convertView.setTag(viewHolder);
		}
		if(convertView!=null){
			viewHolder = (ViewHolder)convertView.getTag();
		}		
		return convertView;
	}
	private void setTopicView(ViewHolder viewHolder, final Topic topic,final int position) {
		// TODO Auto-generated method stub
		viewHolder.time.setText(topic.getCreate_time());
		viewHolder.skillname.setText(topic.getLabel_name());
		viewHolder.topic.setText(topic.getSubject());
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!checkNetworkState()){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}else{
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
					
/*					ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
					ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
					ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
					ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
*/					
					User user=(User)mList.get(position).get(Constant.USER);
					String ownerid=null;
					try {
						ownerid = PeerUI.getInstance().getISessionManager().getUserId();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					if(user.getUserid().equals(ownerid)){
						ChatRoomTypeUtil.getInstance().setIsowner(true);
					}else{
						ChatRoomTypeUtil.getInstance().setIsowner(false);
					}
					topic.setUser(user);
					ChatRoomTypeUtil.getInstance().setTopic(topic);	
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					mContext.startActivity(intent);	
				}					
			}
		});		
	}	

	private void setUserView(ViewHolder viewHolder, final User user) {
		// TODO Auto-generated method stub
		LoadImageUtil.imageLoader.displayImage(user.getImage(), viewHolder.headpic, LoadImageUtil.options);						
		viewHolder.nikename.setText(user.getUsername());
		List<String>list=(List<String>) user.getLabels();
//		final boolean isfriends=Boolean.getBoolean(user.getIs_friends());
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
				if(!checkNetworkState()){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}else{
					/*					PersonpageUtil.getInstance().setPersonid(user.getUserid());
					PersonpageUtil.getInstance().setHuanxinId(user.getHuangxin_username());
					PersonpageUtil.getInstance().setPersonname(user.getUsername());
				
					if(isfriends){
						PersonpageUtil.getInstance().setPersonpagetype(Constant.FRIENDSPAGE);
					}else{
						PersonpageUtil.getInstance().setPersonpagetype(Constant.UNFRIENDSPAGE);
					}*/
					PersonpageUtil.getInstance().setUser(user);
					Intent intent=new Intent(mContext,PersonalPageActivity.class);
					mContext.startActivity(intent);	
				}
			}
		});
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,skillname;
		TextView descripe,topic,time;
	}
}
