package com.peer.adapter;

import java.util.List;
import com.peer.R;
import com.peer.event.SkillEvent;
import de.greenrobot.event.EventBus;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SkillAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mlist;
	public SkillAdapter(Context mContext,List<String> mlist){
		this.mContext=mContext;
		this.mlist=mlist;
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
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_skill,null,false);
			viewHolder.skillname=(TextView)convertView.findViewById(R.id.tv_skill);
			viewHolder.delete=(TextView)convertView.findViewById(R.id.tv_delete);
			viewHolder.update=(TextView)convertView.findViewById(R.id.tv_update);		
			viewHolder.skillname.setText(mlist.get(position));
			viewHolder.delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(checkNetworkState()){
						deleteSkill(position);
					}else{
						Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}
					
				}
			});
			viewHolder.update.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(checkNetworkState()){
						updateSkill(position);
					}else{
						Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}
					
				}
			});							
			convertView.setTag(viewHolder);
		}else{
			convertView.getTag();
		}	
		
		return convertView;
	}
	private void deleteSkill(final int position){
		new AlertDialog.Builder(mContext).setTitle(mContext.getResources().getString(R.string.deleteskill))  
		.setMessage(mContext.getResources().getString(R.string.deletethisskill)) .setNegativeButton(mContext.getResources().getString(R.string.cancel), null) 
		 .setPositiveButton(mContext.getResources().getString(R.string.sure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){ 
            	 EventBus.getDefault().post(new SkillEvent(position,mlist.get(position),true));           	      	        	 
             }
		 })
		 .show();  
	}
	private void updateSkill(final int position) {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(mContext);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.updateskill)).setView(inputServer).setNegativeButton(
        		mContext.getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(mContext.getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String inputName = inputServer.getText().toString().trim();
                        boolean issamelabel=false;
                        if(!TextUtils.isEmpty(inputName)){                    	   
                    	   for(int i=0;i<mlist.size();i++){
                    		  if( inputName.equals( mlist.get(i))){
                    			  issamelabel=true;
                    			  break;
                    			  }                    		   
                    	   }
                    	   if(!issamelabel){
                    		   EventBus.getDefault().post(new SkillEvent(position,inputName,false));                  		  
                    	   }                   	
                       }      
                    }
                });
        builder.show();			
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
		TextView skillname,delete,update;
		
	}
}
