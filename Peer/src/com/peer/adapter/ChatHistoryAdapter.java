package com.peer.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.widgetutil.LoadImageUtil;
import com.readystatesoftware.viewbadger.BadgeView;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class ChatHistoryAdapter extends ArrayAdapter<EMConversation> {
	private Context mContext;
	private List<EMConversation> mList;
	private List<EMConversation> copyConversationList;
	private ConversationFilter conversationFilter;
	private List<Map> userlist;
	
	public ChatHistoryAdapter(Context mContext,int textViewResourceId,List<EMConversation> mList){
		super(mContext, textViewResourceId, mList);		
		this.mContext=mContext;
		this.mList=mList;
		copyConversationList = new ArrayList<EMConversation>();
		copyConversationList.addAll(mList);
		
		LoadImageUtil.initImageLoader(mContext);
	}
	
	public void setUsermsg(List userlist){
		this.userlist=userlist;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		String type=(String) userlist.get(position).get("type");	
		if(convertView==null){
			viewHolder=new ViewHolder();
			// 获取与此用户/群组的会话
			EMConversation conversation = getItem(position);
			// 获取用户username或者群组groupid
//			final String username = conversation.getUserName();

									
			if(type.equals(Constant.TOPIC)){
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
							ChatRoomTypeUtil.getInstance().setTopicId((String)userlist.get(position).get("id"));
							
							Intent intent=new Intent(mContext,ChatRoomActivity.class);
							mContext.startActivity(intent);
						}
					});
					
					convertView.setTag(viewHolder);
				}else if(type.equals(Constant.USER)){
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
//					if (conversation.getMsgCount() != 0) {
//						// 把最后一条消息的内容作为item的message内容
//						EMMessage lastMessage = conversation.getLastMessage();
//						TextMessageBody txtBody = (TextMessageBody) lastMessage.getBody();
//						viewHolder.descripe.setText(txtBody.getMessage());
//					}
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

	@Override
	public Filter getFilter() {
		if (conversationFilter == null) {
			conversationFilter = new ConversationFilter(mList);
		}
		return conversationFilter;
	}
	
	private class ConversationFilter extends Filter {
		List<EMConversation> mOriginalValues = null;

		public ConversationFilter(List<EMConversation> mList) {
			mOriginalValues = mList;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				mOriginalValues = new ArrayList<EMConversation>();
			}
			if (prefix == null || prefix.length() == 0) {
				results.values = copyConversationList;
				results.count = copyConversationList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalValues.size();
				final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

				for (int i = 0; i < count; i++) {
					final EMConversation value = mOriginalValues.get(i);
					String username = value.getUserName();
					
					EMGroup group = EMGroupManager.getInstance().getGroup(username);
					if(group != null){
						username = group.getGroupName();
					}

					// First match against the whole ,non-splitted value
					if (username.startsWith(prefixString)) {
						newValues.add(value);
					} else{
						  final String[] words = username.split(" ");
	                        final int wordCount = words.length;

	                        // Start at index 0, in case valueText starts with space(s)
	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
	                                newValues.add(value);
	                                break;
	                            }
	                        }
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mList.clear();
			mList.addAll((List<EMConversation>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

	}
}
