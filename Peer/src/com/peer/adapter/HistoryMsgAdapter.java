package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.R;
import com.peer.client.User;
import com.peer.constant.Constant;
import com.peer.widgetutil.LoadImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryMsgAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map> mlist;
	public HistoryMsgAdapter(Context mContext, List<Map> mlist){
		this.mContext=mContext;	
		this.mlist=mlist;
		LoadImageUtil.initImageLoader(mContext);
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
	public View getView(int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_chathistory,null,false);				
			viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nike);			
			viewHolder.content=(TextView)convertView.findViewById(R.id.tv_descripe);
			convertView.setTag(viewHolder);
		}else{
			convertView.getTag();
		}
		User user=(User) mlist.get(position).get(Constant.USER);
		LoadImageUtil.imageLoader.displayImage(user.getImage(), viewHolder.headpic,LoadImageUtil.options);
		viewHolder.nikename.setText(user.getUsername());
		
//		viewHolder.content.setText((String)mlist.get(position).get("replybody"));
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,content;
	}
}
