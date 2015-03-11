package com.peer.adapter;

import java.util.List;
import java.util.Map;
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
import android.widget.Toast;
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.widgetutil.LoadImageUtil;
import com.readystatesoftware.viewbadger.BadgeView;

public class ChatHistoryAdapter extends FatherAdater {
	private Context mContext;
	private List<Map> userlist;
	public ChatHistoryAdapter(Context mContext,List<Map> mlist){
		super(mContext);
		this.mContext=mContext;
		this.userlist=mlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userlist.size();
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
		String type=(String) userlist.get(position).get("type");
		
		if(convertView==null){
			viewHolder=new ViewHolder();
			if(type.equals(Constant.TOPIC)){
				Topic topic=(Topic)userlist.get(position).get(Constant.TOPIC);				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);				
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				EMConversation conversation = EMChatManager.getInstance().getConversation(topic.getHuangxin_group_id());
				setTopicView(viewHolder,conversation,topic,position);				
				convertView.setTag(viewHolder);
			}else if(type.equals(Constant.USER)){	
				User user=(User) userlist.get(position).get(Constant.USER);
				EMConversation conversation = EMChatManager.getInstance().getConversation(user.getHuangxin_username());				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listperson,null,false);				
				viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);	
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				
				setUserView(viewHolder,conversation,user);		
				convertView.setTag(viewHolder);
			}
		
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}	
		return convertView;
	}
	private void setUserView(ViewHolder viewHolder,
			EMConversation conversation, final User user) {
		// TODO Auto-generated method stub
		LoadImageUtil.imageLoader.displayImage(user.getImage(), viewHolder.headpic, LoadImageUtil.options);
		
		viewHolder.nikename.setText(user.getUsername());
		EMMessage lastMessage = conversation.getLastMessage();
		TextMessageBody body=(TextMessageBody) lastMessage.getBody();
		viewHolder.descripe.setText(body.getMessage());
		final BadgeView bd=new BadgeView(mContext, viewHolder.click);
		if (conversation.getUnreadMsgCount() > 0) {				
			bd.setText(String.valueOf(conversation.getUnreadMsgCount()));
			bd.show();
		}else{
			bd.hide();
		}
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!checkNetworkState()){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}else{	
					bd.hide();
					ChatRoomTypeUtil.getInstance().setUserId(user.getUserid());
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
					ChatRoomTypeUtil.getInstance().setTitle(user.getUsername());
					ChatRoomTypeUtil.getInstance().setHuanxingId(user.getHuangxin_username());
					
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					mContext.startActivity(intent);
				}
			}
		});
		
	}

	private void setTopicView(ViewHolder viewHolder, EMConversation conversation,final Topic topic, final int position) {
		// TODO Auto-generated method stub
		viewHolder.time.setText(topic.getCreate_time());
		viewHolder.nikename.setText(topic.getLabel_name());
		viewHolder.descripe.setText(topic.getSubject());	
		final BadgeView bd=new BadgeView(mContext, viewHolder.click);
		if (conversation.getUnreadMsgCount() > 0) {				
			bd.setText(String.valueOf(conversation.getUnreadMsgCount()));
			bd.show();
		}else{
			bd.hide();
		}
		viewHolder.click.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!checkNetworkState()){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}else{							
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
					ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
					ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
					ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
					ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
					
					User user=(User)userlist.get(position).get(Constant.USER);
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
					ChatRoomTypeUtil.getInstance().setUser(user);	
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					mContext.startActivity(intent);
				}
			}
		});
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,time;
		TextView descripe;
	}	
}
