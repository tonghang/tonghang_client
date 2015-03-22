package com.peer.fragment;

import java.util.List;

import com.peer.R;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.MySkillActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.activity.SettingActivity;
import com.peer.activitymain.CreatTopicActivity;
import com.peer.activitymain.NewFriendsActivity;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.activitymain.SearchActivity;
import com.peer.client.User;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.util.PersonpageUtil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BasicFragment extends Fragment implements OnClickListener{
	public static final int UPDATESUCESS=9;	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_search:
			Intent tosearch=new Intent(getActivity(),SearchActivity.class);
			startActivity(tosearch);	
			break;
		case R.id.tv_createtopic:
			Intent creat=new Intent(getActivity(),CreatTopicActivity.class);
			startActivity(creat);
			break;
					
		case R.id.rl_myacount_my:
			Intent myacount=new Intent(getActivity(),MyAcountActivity.class);
			startActivity(myacount);
			break;
		case R.id.ll_personmessage_my:
			Intent personmessage=new Intent(getActivity(),PersonalMessageActivity.class);
			startActivityForResult(personmessage, UPDATESUCESS);
			break;
		case R.id.ll_mytag_my:
			Intent mytag=new Intent(getActivity(),MySkillActivity.class);
			startActivity(mytag);
			break;
		case R.id.rl_newfriends:
			Intent intent=new Intent(getActivity(),NewFriendsActivity.class);
			startActivity(intent);		
			break;
		case R.id.ll_setting_my:
			Intent setting=new Intent(getActivity(),SettingActivity.class);
			startActivity(setting);
			break;
		case R.id.rl_ponseralpage:
			ToMypersonalpage();			
			break;
		default:
			break;	
		}
	}
	private void ToMypersonalpage() {
		// TODO Auto-generated method stub
		String userid=null,huangxin_username=null;
		List<String> labels=null;
		try {
			userid=PeerUI.getInstance().getISessionManager().getUserId();
			huangxin_username=PeerUI.getInstance().getISessionManager().getHuanxingUser();
			labels=PeerUI.getInstance().getISessionManager().getLabels();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*
//		PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
//		PersonpageUtil.getInstance().setPersonid(userid);
*/			
		LocalStorage.getString(getActivity(), Constant.EMAIL);
		UserDao userdao=new UserDao(getActivity());
		UserBean userbean=userdao.findOne(LocalStorage.getString(getActivity(), Constant.EMAIL));
		User user=new User();
		user.setEmail(userbean.getEmail());
		user.setBirthday(userbean.getAge());
		user.setCity(userbean.getCity());
		user.setSex(userbean.getSex());
		user.setImage(userbean.getImage());
		user.setUsername(userbean.getNikename());
		user.setUserid(userid);
		user.setHuangxin_username(huangxin_username);
		user.setLabels(labels);
		PersonpageUtil.getInstance().setUser(user);
		Intent topersonalpage=new Intent(getActivity(),PersonalPageActivity.class);
		startActivity(topersonalpage);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != getActivity().RESULT_CANCELED) {
			switch (requestCode) {
			case UPDATESUCESS:
				ShowMessage(getResources().getString(R.string.updatesuccess));
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void ShowMessage(String message){
		Toast.makeText(getActivity(), message, 0).show();
	}
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
}
