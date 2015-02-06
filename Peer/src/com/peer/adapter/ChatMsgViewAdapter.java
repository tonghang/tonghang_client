package com.peer.adapter;

import java.util.List;

import org.springframework.http.ContentCodingType;

import com.peer.R;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.constant.Constant;
import com.peer.entity.ChatMsgEntity;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ChatMsgViewAdapter extends FatherAdater {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 1;
		int IMVT_TO_MSG = 0;
		int IMVT_OTH_MSG =2;
	}
	private Context context;
	private static final int ITEMCOUNT = 3;
	private List<ChatMsgEntity> coll;
	private LayoutInflater mInflater;
	
	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
		super(context);
		this.coll = coll;
		this.context=context;
		mInflater = LayoutInflater.from(context);
		LoadImageUtil.initImageLoader(context);
	}
	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	
	public int getItemViewType(int position) {
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()==1) {
			return IMsgViewType.IMVT_COM_MSG;
		} else if(entity.getMsgType()==0){
			return IMsgViewType.IMVT_TO_MSG;
		}else{  
			return IMsgViewType.IMVT_OTH_MSG;
		}
	}

	public int getViewTypeCount() {
		return ITEMCOUNT;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMsgEntity entity = coll.get(position);
		final int isComMsg = entity.getMsgType();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg==0) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
				viewHolder = new ViewHolder();
				viewHolder.tvSendTime = (TextView) convertView
						.findViewById(R.id.tv_sendtime);
		
				viewHolder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_chatcontent);
				viewHolder.tvContent.setTextColor(context.getResources().getColor(R.color.white));
				viewHolder.heapic=(ImageView)convertView.findViewById(R.id.iv_ownerhead);
				viewHolder.isComMsg = isComMsg;
				convertView.setTag(viewHolder);
			} else if(isComMsg==1){
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
				viewHolder = new ViewHolder();
				viewHolder.tvSendTime = (TextView) convertView
						.findViewById(R.id.tv_sendtime);
				
				viewHolder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_chatcontent);
				viewHolder.heapic=(ImageView)convertView.findViewById(R.id.iv_userhead);
				viewHolder.isComMsg = isComMsg;
				convertView.setTag(viewHolder);
			}
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
			LoadImageUtil.imageLoader.displayImage(entity.getImage(), viewHolder.heapic, LoadImageUtil.options);
			viewHolder.tvSendTime.setText(entity.getDate());			
			viewHolder.tvContent.setText(entity.getMessage());
			final String userId=entity.getUserId();
			viewHolder.heapic.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(checkNetworkState()){
						if(isComMsg==0){
							PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
							PersonpageUtil.getInstance().setPersonid(userId);
							Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
							context.startActivity(topersonalpage);
						}else{
							PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
							PersonpageUtil.getInstance().setPersonid(userId);
							Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
							context.startActivity(topersonalpage);
						}	
					}else{
						Toast.makeText(context, context.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}
						
				}
			});			
		return convertView;
	}
	static class ViewHolder {
		public ImageView heapic;
		public TextView tvSendTime;
		public TextView tvContent;
		public int isComMsg;
	}

}
