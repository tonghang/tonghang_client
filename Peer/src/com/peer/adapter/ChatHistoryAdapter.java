package com.peer.adapter;

import java.util.List;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.peer.R;
import com.readystatesoftware.viewbadger.BadgeView;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class ChatHistoryAdapter extends ArrayAdapter<EMConversation> {
	private Context mContext;
	private List<EMConversation> mList;
	public ChatHistoryAdapter(Context mContext,int textViewResourceId,List<EMConversation> mList){
		super(mContext, textViewResourceId, mList);		
		this.mContext=mContext;
		this.mList=mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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
//		String type=(String) mList.get(position).get("type");		
		if(convertView==null){
			viewHolder=new ViewHolder();
			// 获取与此用户/群组的会话
			EMConversation conversation = getItem(position);
			// 获取用户username或者群组groupid
			String username = conversation.getUserName();
			List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
			EMContact contact = null;
			boolean isGroup = false;
			for (EMGroup group : groups) {
				if (group.getGroupId().equals(username)) {
					isGroup = true;
					contact = group;
					break;
				}
			}						
			if(isGroup){
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
				}else{
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
					
					BadgeView bd=new BadgeView(mContext, viewHolder.click);
					if (conversation.getUnreadMsgCount() > 0) {				
						bd.setText(String.valueOf(conversation.getUnreadMsgCount()));
						bd.show();
					}else{
						bd.hide();
					}
//					if (conversation.getMsgCount() != 0) {
//						// 把最后一条消息的内容作为item的message内容
//						EMMessage lastMessage = conversation.getLastMessage();
//						TextMessageBody txtBody = (TextMessageBody) lastMessage.getBody();
//						viewHolder.descripe.setText(txtBody.getMessage());
//					}
					
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
		TextView nikename;
		TextView descripe;
	}
}
