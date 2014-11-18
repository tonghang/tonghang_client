package com.peer.activity;


import java.util.ArrayList;
import java.util.List;
import com.peer.R;
import com.peer.adapter.SkillAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MySkillActivity extends BasicActivity {
	private int Hadtag;
	private ListView mytaglistview;
	private LinearLayout creatTag,back;
	public static Handler handler;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myskill);
		init();		
	}
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.myskill));
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
		creatTag=(LinearLayout)findViewById(R.id.ll_createTag_mytag);
		creatTag.setOnClickListener(this);
		mytaglistview=(ListView)findViewById(R.id.lv_myskill);
		SkillAdapter adapter=new SkillAdapter(this);
		mytaglistview.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_createTag_mytag:
			if(Hadtag>4){
				ShowMessage("您已经有五个标签，不能再创建了");
				break;
			}else{
				if(checkNetworkState()){
					CreateTag();
				}else{
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}					
			}				
			break;

		default:
			break;
		}
	}
	private void CreateTag() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(MySkillActivity.this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySkillActivity.this);
        builder.setTitle("创建秀场").setView(inputServer).setNegativeButton(
        		getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString().trim();
                       if(!inputName.equals("")){
                    	   List<String> arr=new ArrayList<String>();
                           arr.add(inputName);
                        
                       }else{
                    	   ShowMessage("请输入秀场名");
                       }
                       
                    }
                });
        builder.show();			
	}
	
}
