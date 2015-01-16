package com.peer.adapter;

import java.util.List;
import java.util.Map;
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
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.client.Topic;
import com.peer.client.User;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.widgetutil.LoadImageUtil;
import com.readystatesoftware.viewbadger.BadgeView;

public class ChatHistoryAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map> userlist;
	public ChatHistoryAdapter(Context mContext,List<Map> mlist){
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
				final Topic topic=(Topic)userlist.get(position).get(Constant.TOPIC);
				EMConversation conversation = EMChatManager.getInstance().getConversation(topic.getHuangxin_group_id());				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
				
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);			
				viewHolder.time.setText(topic.getCreate_time());
				viewHolder.nikename.setText(topic.getLabel_name());
				viewHolder.descripe.setText(topic.getSubject());
				
				viewHolder.click.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
						ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
						ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
						ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
						ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
						
						User user=(User) userlist.get(position).get(Constant.USER);
						ChatRoomTypeUtil.getInstance().setUser(user);
						Intent intent=new Intent(mContext,ChatRoomActivity.class);
						mContext.startActivity(intent);
					}
				});
				
				convertView.setTag(viewHolder);
			}else if(type.equals(Constant.USER)){	
				final User user=(User) userlist.get(position).get(Constant.USER);
				EMConversation conversation = EMChatManager.getInstance().getConversation(user.getHuangxin_username());				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listperson,null,false);				
				viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
	
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				
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
	//			if (conversation.getMsgCount() != 0) {
	//				// 把最后一条消息的内容作为item的message内容
	//				EMMessage lastMessage = conversation.getLastMessage();
	//				TextMessageBody txtBody = (TextMessageBody) lastMessage.getBody();
	//				viewHolder.descripe.setText(txtBody.getMessage());
	//			}
				viewHolder.click.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						bd.hide();
						ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);
						ChatRoomTypeUtil.getInstance().setTitle(user.getUsername());
						ChatRoomTypeUtil.getInstance().setHuanxingId(user.getHuangxin_username());
						Intent intent=new Intent(mContext,ChatRoomActivity.class);
						mContext.startActivity(intent);							
					}
				});
				
				convertView.setTag(viewHolder);
			}
		
	}else {
		viewHolder = (ViewHolder) convertView.getTag();
	}
	
	
	return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,time;
		TextView descripe;
	}	
}
