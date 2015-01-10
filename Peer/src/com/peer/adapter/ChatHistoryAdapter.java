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
				EMConversation conversation = EMChatManager.getInstance().getConversation((String)userlist.get(position).get("huanxin_group_id"));
				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
				viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
				
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						(int) mContext.getResources().getDimension(R.dimen.widgeheight_max));
				params.leftMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);
				params.rightMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);				
				
				viewHolder.time.setText((String)userlist.get(position).get("created_at"));
				viewHolder.nikename.setText((String)userlist.get(position).get("label_name"));
				viewHolder.descripe.setText((String)userlist.get(position).get("subject"));
				
				viewHolder.click.setLayoutParams(params);
				viewHolder.click.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
						ChatRoomTypeUtil.getInstance().setHuanxingId(String.valueOf(userlist.get(position).get("huanxin_group_id")));
						ChatRoomTypeUtil.getInstance().setTitle((String)userlist.get(position).get("label_name"));
						ChatRoomTypeUtil.getInstance().setTheme((String)userlist.get(position).get("subject"));
						ChatRoomTypeUtil.getInstance().setTopicId(String.valueOf(userlist.get(position).get("id")));
						Map m=(Map) userlist.get(position).get("user");
						User user=new User();
						user.setImage(Constant.WEB_SERVER_ADDRESS+(String)m.get("image"));
						user.setUsername((String)m.get("username"));
						user.setUserid((String)m.get("user_id"));
						ChatRoomTypeUtil.getInstance().setUser(user);
						Intent intent=new Intent(mContext,ChatRoomActivity.class);
						mContext.startActivity(intent);
					}
				});
				
				convertView.setTag(viewHolder);
			}else if(type.equals(Constant.USER)){					
				EMConversation conversation = EMChatManager.getInstance().getConversation((String)userlist.get(position).get("huanxin_username"));				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listperson,null,false);				
				viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
	
				viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
				viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
				viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
				viewHolder.click.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int) mContext.getResources().getDimension(R.dimen.widgeheight_max));
				params.leftMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);
				params.rightMargin=(int) mContext.getResources().getDimension(R.dimen.marginsize_around);			
				viewHolder.click.setLayoutParams(params);
				
				LoadImageUtil.imageLoader.displayImage(Constant.WEB_SERVER_ADDRESS+userlist.get(position).get("image"), viewHolder.headpic, LoadImageUtil.options);
				
				viewHolder.nikename.setText((String)userlist.get(position).get("username"));
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
						ChatRoomTypeUtil.getInstance().setTitle((String)userlist.get(position).get("username"));
						ChatRoomTypeUtil.getInstance().setHuanxingId((String)userlist.get(position).get("huanxin_username"));
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
