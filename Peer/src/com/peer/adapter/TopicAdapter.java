package com.peer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.activitymain.TopicHistoryActivity;
import com.peer.client.Topic;

public class TopicAdapter extends BaseAdapter {
	private Context mContext;
	private List<Topic> mlist;
	public TopicAdapter(Context mContext,List<Topic> list){
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
//				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
				if(checkNetworkState()){
					Intent intent=new Intent(mContext,TopicHistoryActivity.class);
					intent.putExtra("topicid",String.valueOf(topic.getTopicid()));
					mContext.startActivity(intent);
				}else{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}
				
			}
		});
		
		return convertView;
	}
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
	private class ViewHolder{
		LinearLayout click;
		TextView nikename;
		TextView descripe,time;
	}
}
