package com.peer.adapter;

import com.peer.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SkillAdapter extends BaseAdapter {
	private Context mContext;
	private String type="";
	public SkillAdapter(Context mContext){
		this.mContext=mContext;
	}
	public SkillAdapter(Context mContext,String type){
		this.mContext=mContext;
		this.type=type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_skill,null,false);
			viewHolder.skillname=(TextView)convertView.findViewById(R.id.tv_skill);
			viewHolder.delete=(TextView)convertView.findViewById(R.id.tv_delete);
			viewHolder.update=(TextView)convertView.findViewById(R.id.tv_update);		
			if(type.equals("page")){
				viewHolder.delete.setVisibility(View.GONE);
				viewHolder.update.setVisibility(View.GONE);
			}
		}
		return convertView;
	}
	private class ViewHolder{
		TextView skillname,delete,update;
		
	}
}
