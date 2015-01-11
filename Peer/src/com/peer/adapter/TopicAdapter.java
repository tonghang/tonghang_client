package com.peer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.R;
import com.peer.activitymain.TopicHistoryActivity;

public class TopicAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map> mlist;
	public TopicAdapter(Context mContext,List<Map> list){
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
		viewHolder.time.setText((String)mlist.get(position).get("created_at"));
		viewHolder.nikename.setText((String)mlist.get(position).get("label_name"));
		viewHolder.descripe.setText((String)mlist.get(position).get("subject"));
		viewHolder.click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
				Intent intent=new Intent(mContext,TopicHistoryActivity.class);
				intent.putExtra("topicid",String.valueOf(mlist.get(position).get("id")));
				mContext.startActivity(intent);
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
