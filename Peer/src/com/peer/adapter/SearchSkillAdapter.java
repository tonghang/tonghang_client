package com.peer.adapter;

import java.util.List;
import java.util.Random;

import com.peer.R;
import com.peer.activitymain.SearchActivity;
import com.peer.activitymain.SearchResultActivity;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.util.SearchUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchSkillAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mlist;
	
	public SearchSkillAdapter(Context mContext,List<String> list){
		this.mContext=mContext;
		this.mlist=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mlist.size()%2==1){
			return mlist.size()/2+1;
		}else{
			return mlist.size()/2;
		}
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_searchskill,null,false);
			viewHolder.view1=(TextView)convertView.findViewById(R.id.view1);
			viewHolder.view2=(TextView)convertView.findViewById(R.id.view2);
			Random random=new Random();
			int r=random.nextInt(255);
			int g=random.nextInt(255);
			int b=random.nextInt(255);			
			
			int weizhi = 0;
			if(position==0){
				weizhi=0;
			}else{
				if(weizhi<mlist.size()-1){
					weizhi=position+2;
				}			
			}			
			viewHolder.view1.setText(mlist.get(weizhi));						
			viewHolder.view1.setBackgroundResource(R.drawable.searchskillborder);		
			GradientDrawable myGrad = (GradientDrawable)viewHolder.view1.getBackground();
			myGrad.setColor(mContext.getResources().getColor(R.color.testcolor));
			viewHolder.view1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SearchUtil.getInstance().setSearchtype(Constant.SEARCHUSERBYLABEL);
					Intent intent=new Intent(mContext,SearchResultActivity.class);
					mContext.startActivity(intent);
					((Activity) mContext).finish();
				}
			});			
			int r2=random.nextInt(255);
			int g2=random.nextInt(255);
			int b2=random.nextInt(255);
			if(weizhi+1<mlist.size()){
				viewHolder.view2.setText(mlist.get(weizhi+1));		
				viewHolder.view2.setBackgroundResource(R.drawable.searchskillborder);	
				GradientDrawable myGrad2 = (GradientDrawable)viewHolder.view2.getBackground();
				myGrad.setColor(mContext.getResources().getColor(R.color.testcolor));
				viewHolder.view2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						SearchUtil.getInstance().setSearchtype(Constant.SEARCHUSERBYLABEL);
						Intent intent=new Intent(mContext,SearchResultActivity.class);
						mContext.startActivity(intent);						
						((Activity) mContext).finish();		
						
					}
				});
			}else{
				viewHolder.view2.setVisibility(View.INVISIBLE);
			}
			
			convertView.setTag(viewHolder);
		}else{
			convertView.getTag();
		}
		
		return convertView;
		
	}
	private class ViewHolder{
		TextView view1,view2;
		
	}
}
