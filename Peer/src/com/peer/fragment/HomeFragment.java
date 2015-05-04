package com.peer.fragment;

import java.util.ArrayList;
import java.util.List;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peer.R;
import com.peer.activitymain.CreatTopicActivity;
import com.peer.activitymain.SearchActivity;
import com.peer.adapter.HomepageAdapter;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BasicFragment{
	private TextView createtopic;
	private LinearLayout search;
	private PullToRefreshListView mPullrefreshlistview;	
	public RelativeLayout errorItem;
	public TextView errorText;	
	private List list=new ArrayList();
	HomepageAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		RecommendTask task=new RecommendTask();
		task.execute();
	}
	private void init() {
		// TODO Auto-generated method stub
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		
		search=(LinearLayout)getView().findViewById(R.id.ll_search);		
		createtopic=(TextView)getView().findViewById(R.id.tv_createtopic);
		
		createtopic.setOnClickListener(this);		
		search.setOnClickListener(this);
			
		mPullrefreshlistview=(PullToRefreshListView)getView().findViewById(R.id.pull_refresh_homepage);
		
		
		RefreshListner();
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_search:
			Intent tosearch=new Intent(getActivity(),SearchActivity.class);
			startActivity(tosearch);			
			break;
		case R.id.tv_createtopic:
			Intent intent=new Intent();
			intent.setClass(getActivity(), CreatTopicActivity.class);
			startActivity(intent);
			break;
		}
	}
	private void RefreshListner() {
		// TODO Auto-generated method stub
		mPullrefreshlistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub					
					String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					RefreshTask task=new RefreshTask();
					task.execute("DownToRefresh");								
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				RefreshTask task=new RefreshTask();
				task.execute("UpToRefresh");
			}			
		});
	}
	private class RefreshTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);														
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SessionListener callback=new SessionListener();
			if(checkNetworkState()){
				if(arg0[0].equals("UpToRefresh")){				
	            	try {
					List uprefrsh=PeerUI.getInstance().getISessionManager().recommendByPage(callback);
					list.addAll(uprefrsh);
	            	} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						List downrefrsh=PeerUI.getInstance().getISessionManager().recommend(1, callback);
						list.clear();
						list.addAll(downrefrsh);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}			
			return callback.getMessage();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result!=null&&result.equals(Constant.CALLBACKSUCCESS)){
				adapter.notifyDataSetChanged();
				mPullrefreshlistview.onRefreshComplete();
			}else{
				mPullrefreshlistview.onRefreshComplete();
			}
		}		
	}
	private class RecommendTask extends AsyncTask<Void, Void, List>{

		@Override
		protected List doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SessionListener callback=new SessionListener();
         
            try {
            List listrecommend=PeerUI.getInstance().getISessionManager().recommend(1, callback);
			list.clear();
			list.addAll(listrecommend);
            } catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
		@Override
		protected void onPostExecute(List result) {
			// TODO Auto-generated method stub
			adapter=new HomepageAdapter(getActivity(),list);
			mPullrefreshlistview.setAdapter(adapter);
		}
	}
}
