package com.peer.adapter;

import com.peer.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewfriendsAdapter extends BaseAdapter {
	private Context mContext;	
	public NewfriendsAdapter(Context mContext){
		this.mContext=mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_listnike_friends,null,false);
			viewHolder.headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			viewHolder.nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
			viewHolder.descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
			viewHolder.click=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			viewHolder.refuse=(TextView)convertView.findViewById(R.id.tv_refuse);
			viewHolder.refuse.setVisibility(View.VISIBLE);
			viewHolder.access=(TextView)convertView.findViewById(R.id.tv_access);
			viewHolder.access.setVisibility(View.VISIBLE);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nikename.setText("昵称");
		viewHolder.descripe.setText("请求加为好友");		
		return convertView;
	}
	private class ViewHolder{
		LinearLayout click;
		ImageView headpic;
		TextView nikename,descripe,refuse,access;
		
	}


}
