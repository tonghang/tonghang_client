package com.peer.adapter;

import java.util.Random;

import com.peer.R;
import com.peer.activitymain.SearchActivity;
import com.peer.activitymain.SearchResultActivity;
import com.peer.constant.Constant;
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
	
	public SearchSkillAdapter(Context mContext){
		this.mContext=mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
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
		
		}
		Random random=new Random();
		int r=random.nextInt(255);
		int g=random.nextInt(255);
		int b=random.nextInt(255);
		
		viewHolder.view1.setBackgroundResource(R.drawable.border);		
		GradientDrawable myGrad = (GradientDrawable)viewHolder.view1.getBackground();
		myGrad.setColor(Color.rgb(r, g, b));
		viewHolder.view1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				((Activity) mContext).finish();
				SearchUtil.getInstance().setSearchtype(Constant.SEARCHUSER);
				Intent intent=new Intent(mContext,SearchResultActivity.class);
				mContext.startActivity(intent);
				
			}
		});
		
		
		int r2=random.nextInt(255);
		int g2=random.nextInt(255);
		int b2=random.nextInt(255);
		viewHolder.view2.setBackgroundResource(R.drawable.border);	
		GradientDrawable myGrad2 = (GradientDrawable)viewHolder.view2.getBackground();
		myGrad.setColor(Color.rgb(r2, g2, b2));
		viewHolder.view2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SearchUtil.getInstance().setSearchtype(Constant.SEARCHUSER);
				Intent intent=new Intent(mContext,SearchResultActivity.class);
				mContext.startActivity(intent);
				
			}
		});
		
		return convertView;
		
	}
	private class ViewHolder{
		TextView view1,view2;
		
	}
}
