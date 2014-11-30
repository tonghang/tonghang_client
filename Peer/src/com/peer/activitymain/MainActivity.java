package com.peer.activitymain;

import android.content.Intent;
import android.os.Bundle;
import com.peer.R;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{
	private HomeFragment homefragment;
	private ComeMsgFragment comemsgfragment;
	private FriendsFragment friendsfragment;
	private MyFragment myfragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	/* bottom layout*/
	private LinearLayout find,come,my,friends;
	private TextView tv_find,tv_come,tv_my,tv_friends;
	private ImageView findback,comeback,friendsback,myback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		init();		
	}
	private void init() {
		// TODO Auto-generated method stub
		/*init fragment*/
		homefragment=new HomeFragment();
		comemsgfragment=new ComeMsgFragment();
		friendsfragment=new FriendsFragment();
		myfragment=new MyFragment();
		fragments=new Fragment[]{
				homefragment,comemsgfragment,friendsfragment,myfragment
		};
		getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, homefragment)
			.add(R.id.fragment_container,comemsgfragment).hide(comemsgfragment)
			.show(homefragment).commit();
		/*init bottom layout*/
		find=(LinearLayout)findViewById(R.id.ll_find);		
		come=(LinearLayout)findViewById(R.id.ll_come);
		my=(LinearLayout)findViewById(R.id.ll_my);
		friends=(LinearLayout)findViewById(R.id.ll_friends);
		
		tv_find=(TextView)findViewById(R.id.tv_find);
		tv_come=(TextView)findViewById(R.id.tv_come);
		tv_friends=(TextView)findViewById(R.id.tv_friends);
		tv_my=(TextView)findViewById(R.id.tv_my);
		
		findback=(ImageView)findViewById(R.id.iv_backfind);
		comeback=(ImageView)findViewById(R.id.iv_backcome);
		friendsback=(ImageView)findViewById(R.id.iv_backfriends);
		myback=(ImageView)findViewById(R.id.iv_backmy);
			
		tv_find.setTextColor(getResources().getColor(R.color.bottomtextblue));		
		findback.setImageResource(R.drawable.find_label_press);
				
	}
	public void onTabClicked(View v) {
		// TODO Auto-generated method stub	
		tv_find.setTextColor(getResources().getColor(R.color.bottomtextgray));	
		tv_come.setTextColor(getResources().getColor(R.color.bottomtextgray));					
		tv_friends.setTextColor(getResources().getColor(R.color.bottomtextgray));
		tv_my.setTextColor(getResources().getColor(R.color.bottomtextgray));		
		
		findback.setImageResource(R.drawable.find_label_nol);
		comeback.setImageResource(R.drawable.come_mess_nol);
		friendsback.setImageResource(R.drawable.find_label_nol);
		myback.setImageResource(R.drawable.mysetting_nol);
		
		switch (v.getId()) {
		case R.id.ll_find:
			index=0;			
			tv_find.setTextColor(getResources().getColor(R.color.bottomtextblue));			
			findback.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_come:
			index=1;			
			tv_come.setTextColor(getResources().getColor(R.color.bottomtextblue));					
			comeback.setImageResource(R.drawable.come_mess_press);	
			break;
		case R.id.ll_friends:
			index=2;	
			tv_friends.setTextColor(getResources().getColor(R.color.bottomtextblue));
			friendsback.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_my:
			index=3;			
			tv_my.setTextColor(getResources().getColor(R.color.bottomtextblue));		
			myback.setImageResource(R.drawable.mysetting_press);
			break;
		default:
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		currentTabIndex = index;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
//			  	Intent intent1 = new Intent(HomePageActivity.this, FxService.class);
//				stopService(intent1);
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.addCategory(Intent.CATEGORY_HOME);
		        startActivity(intent);
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
	}
}
