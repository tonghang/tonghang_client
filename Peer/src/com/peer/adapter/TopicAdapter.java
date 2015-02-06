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
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.activitymain.ChatRoomActivity;
import com.peer.activitymain.TopicHistoryActivity;
import com.peer.client.Topic;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.util.ChatRoomTypeUtil;

public class TopicAdapter extends FatherAdater {
	private Context mContext;
	private List<Topic> mlist;
	public TopicAdapter(Context mContext,List<Topic> list){
		super(mContext);
		this.mContext=mContext;
		this.mlist=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
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
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
			viewHolder.time=(TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.time.setVisibility(View.VISIBLE);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_skill);			
			viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_topic);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Topic topic=mlist.get(position);
				
		viewHolder.time.setText(topic.getCreate_time());
		viewHolder.nikename.setText(topic.getLabel_name());
		viewHolder.descripe.setText(topic.getSubject());
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				if(checkNetworkState()){
					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
					ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
					ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
					ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
					ChatRoomTypeUtil.getInstance().setUser(topic.getUser());
					ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
					String ownerid=null;
					try {
						ownerid = PeerUI.getInstance().getISessionManager().getUserId();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					if(topic.getUser().getUserid().equals(ownerid)){
						ChatRoomTypeUtil.getInstance().setIsowner(true);
					}else{
						ChatRoomTypeUtil.getInstance().setIsowner(false);
					}					
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					intent.putExtra("topicid",String.valueOf(topic.getTopicid()));
					mContext.startActivity(intent);
				}else{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}
				
			}
		});
		
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		TextView nikename;
		TextView descripe,time;
	}
}
