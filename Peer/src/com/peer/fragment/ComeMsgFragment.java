package com.peer.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.peer.R;
import com.peer.adapter.ComeMessageAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class ComeMsgFragment extends Fragment {
	private ListView ListView_come;
	public static final String KEY_MESSAGE = "message";
	public static boolean isForeground = true;	
	List<Map> list=new ArrayList<Map>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_comemsg, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initdata();
		init();	
	}
	private void init() {
		// TODO Auto-generated method stub					
		ListView_come=(ListView)getView().findViewById(R.id.lv_come);	
		ComeMessageAdapter adapter=new ComeMessageAdapter(getActivity(),list);
		ListView_come.setAdapter(adapter);
	}
	private void initdata() {
		// TODO Auto-generated method stub
		Map m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "topic");
		list.add(m);
		m=new HashMap<String, String>();
		m.put("type", "person");
		list.add(m);
	}

}
