package com.peer.adapter;

import java.util.List;

import org.springframework.http.ContentCodingType;

import cn.jpush.android.api.c;

import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.entity.ChatMsgEntity;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.util.ChatRoomTypeUtil;
import com.peer.util.PersonpageUtil;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ChatMsgViewAdapter extends FatherAdater {
	private List<User> list;
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
		if(position==1){//在第一次加载item时取得自己的好友列表（避免多次从server获取），以后用来判断某个用户是不是自己的好友
			FriendsTask task=new FriendsTask();
			task.execute();
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
							if(list==null||list.isEmpty()){
								User user=new User();
								user.setUserid(userId);
								user.setIs_friends(false);
								PersonpageUtil.getInstance().setUser(user);
							}else{
								for(int i=0;i<list.size();i++){
									if(userId.equals(list.get(i).getUserid())){
										 list.get(i).setIs_friends(true);					 
										 PersonpageUtil.getInstance().setUser(list.get(i));
										 break;
									}else{
										User user=new User();
										user.setUserid(userId);
										user.setIs_friends(false);
										PersonpageUtil.getInstance().setUser(user);
									}
								}
								PersonpageUtil.getInstance().setShouldRefresh(true);
								Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
								context.startActivity(topersonalpage);
							}
							
						}else{
							ToMypersonalpage(context);
						}	
					}else{
						Toast.makeText(context, context.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}
						
				}
			});			
		return convertView;
	}
	private class FriendsTask extends AsyncTask<Void, Void, List<User>>{

		@Override
		protected List<User> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub       
            try {
            	list=PeerUI.getInstance().getISessionManager().myFriends();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
			return list;
		}
		@Override
		protected void onPostExecute(List<User> result) {
			// TODO Auto-generated method stub
		}
	}
	private void ToMypersonalpage(Context context) {
		// TODO Auto-generated method stub
		String userid=null,huangxin_username=null;
		List<String> labels=null;
		try {
			userid=PeerUI.getInstance().getISessionManager().getUserId();
			huangxin_username=PeerUI.getInstance().getISessionManager().getHuanxingUser();
			labels=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*
//		PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
//		PersonpageUtil.getInstance().setPersonid(userid);
*/			
		LocalStorage.getString(context, Constant.EMAIL);
		UserDao userdao=new UserDao(context);
		UserBean userbean=userdao.findOne(LocalStorage.getString(context, Constant.EMAIL));
		User user=new User();
		user.setEmail(userbean.getEmail());
		user.setBirthday(userbean.getAge());
		user.setCity(userbean.getCity());
		user.setSex(userbean.getSex());
		user.setImage(userbean.getImage());
		user.setUsername(userbean.getNikename());
		user.setUserid(userid);
		user.setHuangxin_username(huangxin_username);
		user.setLabels(labels);
		PersonpageUtil.getInstance().setUser(user);
		Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
		context.startActivity(topersonalpage);
	}
	static class ViewHolder {
		public ImageView heapic;
		public TextView tvSendTime;
		public TextView tvContent;
		public int isComMsg;
	}

}
